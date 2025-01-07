package pham.com.doctors.repository;

import org.springframework.stereotype.Repository;
import pham.com.doctors.model.Doctors;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface DoctorsRepository extends JpaRepository<Doctors, Long> {
    boolean existsByEmail(String email);

    List<Doctors> findByLnameContainingOrFnameContaining(String lname, String fname);
    List<Doctors> findDoctorsBySpecializationContaining(String specialty);
}
