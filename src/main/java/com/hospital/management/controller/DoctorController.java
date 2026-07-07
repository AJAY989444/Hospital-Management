package com.hospital.management.controller;

import com.hospital.management.config.CustomUserDetails;
import com.hospital.management.dto.AppointmentDTO;
import com.hospital.management.entity.AppointmentStatus;
import com.hospital.management.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@Tag(name = "Doctor Operations", description = "Endpoints for doctors to view bookings and write consultation notes")
public class DoctorController {

    private final AppointmentService appointmentService;

    public DoctorController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/appointments")
    @Operation(summary = "Get assigned appointments", description = "Retrieve list of all appointments mapped to the logged-in doctor.")
    public ResponseEntity<List<AppointmentDTO>> getMyAppointments(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<AppointmentDTO> appointments = appointmentService.getDoctorAppointments(userDetails.getUser().getId());
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/appointments/{id}/status")
    @Operation(summary = "Update appointment status and notes", description = "Allows doctors to modify status (CONFIRMED, COMPLETED, CANCELLED) and submit notes.")
    @ApiResponse(responseCode = "200", description = "Appointment updated successfully")
    @ApiResponse(responseCode = "404", description = "Appointment not found")
    public ResponseEntity<AppointmentDTO> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam("status") AppointmentStatus status,
            @RequestParam(value = "notes", required = false) String notes) {
        
        AppointmentDTO updated = appointmentService.updateAppointmentStatus(id, status, notes);
        return ResponseEntity.ok(updated);
    }
}
