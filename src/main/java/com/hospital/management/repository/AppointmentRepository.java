package com.hospital.management.repository;

import com.hospital.management.entity.Appointment;
import com.hospital.management.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientIdOrderByAppointmentDateDescTimeSlotDesc(Long patientId);

    List<Appointment> findByDoctorIdOrderByAppointmentDateDescTimeSlotDesc(Long doctorId);

    List<Appointment> findTop3ByPatientIdOrderByAppointmentDateDesc(Long patientId);

    // Conflict prevention query: checks if an active (non-cancelled) appointment exists for a doctor at a given date/time
    boolean existsByDoctorIdAndAppointmentDateAndTimeSlotAndStatusNot(
            Long doctorId, LocalDate appointmentDate, String timeSlot, AppointmentStatus status);

    // Count appointments by status
    long countByStatus(AppointmentStatus status);

    // Aggregation query to find busiest specialty (admin dashboard analytics)
    @Query("SELECT a.doctor.specialty, COUNT(a) FROM Appointment a GROUP BY a.doctor.specialty ORDER BY COUNT(a) DESC")
    List<Object[]> findBusiestSpecialties();
}
