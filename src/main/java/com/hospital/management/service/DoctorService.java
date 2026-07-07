package com.hospital.management.service;

import com.hospital.management.dto.DoctorDTO;
import com.hospital.management.dto.UserDTO;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.User;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public DoctorService(DoctorRepository doctorRepository, UserRepository userRepository, UserService userService) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::convertToDoctorDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DoctorDTO> getActiveDoctors() {
        return doctorRepository.findByIsActiveTrue().stream()
                .map(this::convertToDoctorDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DoctorDTO> getActiveDoctorsBySpecialty(String specialty) {
        return doctorRepository.findBySpecialtyAndIsActiveTrue(specialty).stream()
                .map(this::convertToDoctorDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        return convertToDoctorDTO(doctor);
    }

    @Transactional(readOnly = true)
    public DoctorDTO getDoctorByUserId(Long userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found for user: " + userId));
        return convertToDoctorDTO(doctor);
    }

    @Transactional
    public DoctorDTO updateDoctor(Long id, DoctorDTO dto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

        doctor.setSpecialty(dto.getSpecialty());
        doctor.setQualification(dto.getQualification());
        doctor.setExperienceYears(dto.getExperienceYears());

        if (dto.getUser() != null) {
            User user = doctor.getUser();
            user.setName(dto.getUser().getName());
            userRepository.save(user);
        }

        Doctor savedDoctor = doctorRepository.save(doctor);
        return convertToDoctorDTO(savedDoctor);
    }

    @Transactional
    public void softDeleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        
        // Deactivate doctor profile record
        doctor.setActive(false);
        doctorRepository.save(doctor);

        // Propagate deactivation to login user account
        userService.softDeleteUser(doctor.getUser().getId());
    }

    private DoctorDTO convertToDoctorDTO(Doctor doctor) {
        User user = doctor.getUser();
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.isActive())
                .build();

        return DoctorDTO.builder()
                .id(doctor.getId())
                .user(userDTO)
                .specialty(doctor.getSpecialty())
                .qualification(doctor.getQualification())
                .experienceYears(doctor.getExperienceYears())
                .isActive(doctor.isActive())
                .build();
    }
}
