package pham.com.appointments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pham.com.appointments.model.Appointments;

import java.util.List;

@Repository
public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {
    List<Appointments> findByPatientId(Long patientId);
}
