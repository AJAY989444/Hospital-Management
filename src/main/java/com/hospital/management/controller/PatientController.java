package com.hospital.management.controller;

import com.hospital.management.config.CustomUserDetails;
import com.hospital.management.dto.AppointmentDTO;
import com.hospital.management.dto.AppointmentRequest;
import com.hospital.management.dto.DoctorDTO;
import com.hospital.management.service.AppointmentService;
import com.hospital.management.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@Tag(name = "Patient Operations", description = "Endpoints for patients to book slots, list history, and search doctors")
public class PatientController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public PatientController(AppointmentService appointmentService, DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    @GetMapping("/appointments")
    @Operation(summary = "Get personal appointments", description = "Retrieve list of all appointments scheduled by the logged-in patient.")
    public ResponseEntity<List<AppointmentDTO>> getMyAppointments(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<AppointmentDTO> appointments = appointmentService.getPatientAppointments(userDetails.getUser().getId());
        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/appointments")
    @Operation(summary = "Book an appointment slot", description = "Schedules a doctor consultation slot, preventing double booking overlaps.")
    @ApiResponse(responseCode = "201", description = "Appointment booked successfully")
    @ApiResponse(responseCode = "409", description = "Time slot already booked for the selected doctor")
    public ResponseEntity<AppointmentDTO> bookAppointment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody AppointmentRequest request) {
        
        AppointmentDTO booking = appointmentService.bookAppointment(userDetails.getUser().getId(), request);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    @GetMapping("/doctors")
    @Operation(summary = "Find available active doctors", description = "Lists active doctors, with optional query filters by clinical specialty.")
    public ResponseEntity<List<DoctorDTO>> getDoctors(@RequestParam(value = "specialty", required = false) String specialty) {
        List<DoctorDTO> doctors;
        if (specialty != null && !specialty.trim().isEmpty()) {
            doctors = doctorService.getActiveDoctorsBySpecialty(specialty);
        } else {
            doctors = doctorService.getActiveDoctors();
        }
        return ResponseEntity.ok(doctors);
    }
}
