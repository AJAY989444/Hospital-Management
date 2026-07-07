package com.hospital.management.controller;

import com.hospital.management.config.CustomUserDetails;
import com.hospital.management.dto.*;
import com.hospital.management.entity.UserRole;
import com.hospital.management.service.AppointmentService;
import com.hospital.management.service.DoctorService;
import com.hospital.management.service.PatientService;
import com.hospital.management.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class WebViewController {

    private final UserService userService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    public WebViewController(
            UserService userService,
            DoctorService doctorService,
            PatientService patientService,
            AppointmentService appointmentService) {
        this.userService = userService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/")
    public String index(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            UserRole role = userDetails.getUser().getRole();
            if (role == UserRole.ADMIN) return "redirect:/admin/dashboard";
            if (role == UserRole.DOCTOR) return "redirect:/doctor/dashboard";
            if (role == UserRole.PATIENT) return "redirect:/patient/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            return "redirect:/";
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            return "redirect:/";
        }
        return "auth/register";
    }

    @GetMapping("/patient/dashboard")
    public String patientDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails, 
            @RequestParam(value = "specialty", required = false) String specialty,
            Model model) {
        
        Long userId = userDetails.getUser().getId();
        PatientDTO patient = patientService.getPatientByUserId(userId);
        List<AppointmentDTO> appointments = appointmentService.getPatientAppointments(userId);
        
        // Load active doctors, filter by specialty if provided
        List<DoctorDTO> doctors;
        if (specialty != null && !specialty.trim().isEmpty()) {
            doctors = doctorService.getActiveDoctorsBySpecialty(specialty);
        } else {
            doctors = doctorService.getActiveDoctors();
        }

        // Get unique list of Specialties for dropdown select filter
        List<String> specialties = doctorService.getActiveDoctors().stream()
                .map(DoctorDTO::getSpecialty)
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("patient", patient);
        model.addAttribute("appointments", appointments);
        model.addAttribute("doctors", doctors);
        model.addAttribute("specialties", specialties);
        model.addAttribute("selectedSpecialty", specialty);
        
        return "patient/dashboard";
    }

    @GetMapping("/doctor/dashboard")
    public String doctorDashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long userId = userDetails.getUser().getId();
        DoctorDTO doctor = doctorService.getDoctorByUserId(userId);
        List<AppointmentDTO> appointments = appointmentService.getDoctorAppointments(userId);

        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointments);
        
        return "doctor/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        List<DoctorDTO> doctors = doctorService.getAllDoctors();
        List<PatientDTO> patients = patientService.getAllPatients();
        List<AppointmentDTO> appointments = appointmentService.getAllAppointments();
        Map<String, Object> analytics = appointmentService.getAdminAnalytics();
        List<AiSuggestionDTO> aiSuggestions = appointmentService.getAiSuggestionsAuditLog();

        model.addAttribute("doctors", doctors);
        model.addAttribute("patients", patients);
        model.addAttribute("appointments", appointments);
        model.addAttribute("analytics", analytics);
        model.addAttribute("aiSuggestions", aiSuggestions);
        
        return "admin/dashboard";
    }
}
