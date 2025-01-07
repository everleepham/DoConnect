package pham.com.patients.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pham.com.patients.model.Patients;

@Repository
public interface PatientsRepository extends JpaRepository<Patients, Long> {
    boolean existsByEmail(String email);
}
