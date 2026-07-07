package com.hospital.management.service;

import com.hospital.management.dto.*;
import com.hospital.management.entity.*;
import com.hospital.management.exception.*;
import com.hospital.management.repository.*;
import com.hospital.management.config.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public UserService(
            UserRepository userRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public UserDTO registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already registered: " + request.getEmail());
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        if (request.getRole() == UserRole.PATIENT) {
            Patient patient = Patient.builder()
                    .user(savedUser)
                    .age(request.getAge() != null ? request.getAge() : 0)
                    .gender(request.getGender() != null ? request.getGender() : "Not Specified")
                    .bloodGroup(request.getBloodGroup() != null ? request.getBloodGroup() : "N/A")
                    .phone(request.getPhone() != null ? request.getPhone() : "")
                    .isActive(true)
                    .build();
            patientRepository.save(patient);
        } else if (request.getRole() == UserRole.DOCTOR) {
            Doctor doctor = Doctor.builder()
                    .user(savedUser)
                    .specialty(request.getSpecialty() != null ? request.getSpecialty() : "General Physician")
                    .qualification(request.getQualification() != null ? request.getQualification() : "MBBS")
                    .experienceYears(request.getExperienceYears() != null ? request.getExperienceYears() : 0)
                    .isActive(true)
                    .build();
            doctorRepository.save(doctor);
        }

        return convertToUserDTO(savedUser);
    }

    @Transactional
    public AuthResponse login(AuthRequest request) {
        // This will authenticate user credentials or throw BadCredentialsException
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getEmail()));

        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail(), user.getRole().name());
        String refreshTokenStr = jwtTokenProvider.generateRefreshToken(user.getEmail());

        // Invalidate old refresh tokens before saving new one (rotation)
        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush(); // ensure DELETE is flushed before INSERT

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(refreshTokenStr)
                .expiryDate(Instant.now().plusMillis(604800000)) // 7 days
                .build();
        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenStr)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }

    @Transactional
    public AuthResponse refreshAccessToken(String refreshTokenVal) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenVal)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found or invalid"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired. Please login again.");
        }

        User user = refreshToken.getUser();
        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }

    @Transactional
    public void softDeleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        user.setActive(false);
        userRepository.save(user);

        if (user.getRole() == UserRole.PATIENT) {
            patientRepository.findByUserId(userId).ifPresent(p -> {
                p.setActive(false);
                patientRepository.save(p);
            });
        } else if (user.getRole() == UserRole.DOCTOR) {
            doctorRepository.findByUserId(userId).ifPresent(d -> {
                d.setActive(false);
                doctorRepository.save(d);
            });
        }
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.isActive())
                .build();
    }
}
