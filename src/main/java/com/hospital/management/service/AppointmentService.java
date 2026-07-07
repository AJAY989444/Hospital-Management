package com.hospital.management.service;

import com.hospital.management.dto.AppointmentDTO;
import com.hospital.management.dto.AppointmentRequest;
import com.hospital.management.entity.*;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.exception.SchedulingConflictException;
import com.hospital.management.dto.AiSuggestionDTO;
import com.hospital.management.repository.AiSuggestionRepository;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AiSuggestionRepository aiSuggestionRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            AiSuggestionRepository aiSuggestionRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.aiSuggestionRepository = aiSuggestionRepository;
    }

    @Transactional
    public AppointmentDTO bookAppointment(Long patientUserId, AppointmentRequest request) {
        Patient patient = patientRepository.findByUserId(patientUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found for user: " + patientUserId));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));

        if (!doctor.isActive()) {
            throw new IllegalArgumentException("Cannot book appointment with an inactive doctor");
        }

        // Conflict check: Check if doctor already has a booked slot (status NOT cancelled) at that date and time
        boolean hasConflict = appointmentRepository.existsByDoctorIdAndAppointmentDateAndTimeSlotAndStatusNot(
                doctor.getId(), request.getAppointmentDate(), request.getTimeSlot(), AppointmentStatus.CANCELLED);

        if (hasConflict) {
            throw new SchedulingConflictException("The selected time slot is already booked for this doctor");
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.getAppointmentDate())
                .timeSlot(request.getTimeSlot())
                .status(AppointmentStatus.PENDING)
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        return convertToAppointmentDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getPatientAppointments(Long patientUserId) {
        Patient patient = patientRepository.findByUserId(patientUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
        return appointmentRepository.findByPatientIdOrderByAppointmentDateDescTimeSlotDesc(patient.getId()).stream()
                .map(this::convertToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getDoctorAppointments(Long doctorUserId) {
        Doctor doctor = doctorRepository.findByUserId(doctorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));
        return appointmentRepository.findByDoctorIdOrderByAppointmentDateDescTimeSlotDesc(doctor.getId()).stream()
                .map(this::convertToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::convertToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointmentDTO updateAppointmentStatus(Long appointmentId, AppointmentStatus status, String notes) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        appointment.setStatus(status);
        if (notes != null) {
            appointment.setNotes(notes);
        }

        Appointment saved = appointmentRepository.save(appointment);
        return convertToAppointmentDTO(saved);
    }

    @Transactional
    public void updateAiSummary(Long appointmentId, String aiSummary) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        appointment.setAiSummary(aiSummary);
        appointmentRepository.save(appointment);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getAdminAnalytics() {
        long total = appointmentRepository.count();
        long cancelled = appointmentRepository.countByStatus(AppointmentStatus.CANCELLED);
        double cancellationRate = total > 0 ? ((double) cancelled / total) * 100 : 0.0;

        List<Object[]> busiestSpecialties = appointmentRepository.findBusiestSpecialties();
        String busiestSpecialty = "N/A";
        if (!busiestSpecialties.isEmpty()) {
            busiestSpecialty = (String) busiestSpecialties.get(0)[0];
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAppointments", total);
        stats.put("cancellationRate", Math.round(cancellationRate * 100.0) / 100.0);
        stats.put("busiestSpecialty", busiestSpecialty);
        return stats;
    }

    @Transactional(readOnly = true)
    public List<AiSuggestionDTO> getAiSuggestionsAuditLog() {
        return aiSuggestionRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToAiSuggestionDTO)
                .collect(Collectors.toList());
    }

    private AiSuggestionDTO convertToAiSuggestionDTO(AiSuggestion suggestion) {
        return AiSuggestionDTO.builder()
                .id(suggestion.getId())
                .patientName(suggestion.getPatient().getUser().getName())
                .symptomsText(suggestion.getSymptomsText())
                .suggestedSpecialty(suggestion.getSuggestedSpecialty())
                .urgencyLevel(suggestion.getUrgencyLevel())
                .reasoning(suggestion.getReasoning())
                .createdAt(suggestion.getCreatedAt())
                .build();
    }

    private AppointmentDTO convertToAppointmentDTO(Appointment appointment) {
        return AppointmentDTO.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatient().getId())
                .patientName(appointment.getPatient().getUser().getName())
                .doctorId(appointment.getDoctor().getId())
                .doctorName(appointment.getDoctor().getUser().getName())
                .specialty(appointment.getDoctor().getSpecialty())
                .appointmentDate(appointment.getAppointmentDate())
                .timeSlot(appointment.getTimeSlot())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .aiSummary(appointment.getAiSummary())
                .createdAt(appointment.getCreatedAt())
                .build();
    }
}
