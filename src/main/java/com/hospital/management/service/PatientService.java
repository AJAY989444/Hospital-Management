package com.hospital.management.service;

import com.hospital.management.dto.PatientDTO;
import com.hospital.management.dto.UserDTO;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.User;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public PatientService(PatientRepository patientRepository, UserRepository userRepository, UserService userService) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::convertToPatientDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return convertToPatientDTO(patient);
    }

    @Transactional(readOnly = true)
    public PatientDTO getPatientByUserId(Long userId) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found for user: " + userId));
        return convertToPatientDTO(patient);
    }

    @Transactional
    public PatientDTO updatePatient(Long id, PatientDTO dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        patient.setAge(dto.getAge());
        patient.setGender(dto.getGender());
        patient.setBloodGroup(dto.getBloodGroup());
        patient.setPhone(dto.getPhone());

        if (dto.getUser() != null) {
            User user = patient.getUser();
            user.setName(dto.getUser().getName());
            userRepository.save(user);
        }

        Patient savedPatient = patientRepository.save(patient);
        return convertToPatientDTO(savedPatient);
    }

    @Transactional
    public void softDeletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        
        // Deactivate patient record
        patient.setActive(false);
        patientRepository.save(patient);

        // Deactivate associated user login
        userService.softDeleteUser(patient.getUser().getId());
    }

    private PatientDTO convertToPatientDTO(Patient patient) {
        User user = patient.getUser();
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.isActive())
                .build();

        return PatientDTO.builder()
                .id(patient.getId())
                .user(userDTO)
                .age(patient.getAge())
                .gender(patient.getGender())
                .bloodGroup(patient.getBloodGroup())
                .phone(patient.getPhone())
                .isActive(patient.isActive())
                .build();
    }
}
