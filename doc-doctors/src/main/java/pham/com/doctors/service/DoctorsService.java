package pham.com.doctors.service;


import pham.com.doctors.dto.DoctorsDTO;
import pham.com.doctors.exception.ResourceNotFoundException;
import pham.com.doctors.exception.IllegalArgumentException;
import pham.com.doctors.model.Doctors;
import pham.com.doctors.repository.DoctorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorsService {
    public final DoctorsRepository doctorsRepository;
    private static final Logger logger = LoggerFactory.getLogger(DoctorsService.class);

    @Autowired
    public DoctorsService(DoctorsRepository doctorsRepository) {
        this.doctorsRepository = doctorsRepository;
    }

    public List<DoctorsDTO> getAllDoctors() {
        logger.info("Getting all Doctors");
        List<Doctors> allDoctors = doctorsRepository.findAll();
        if (allDoctors.isEmpty()) {
            logger.error("No doctors found");
        } else {
            logger.info("Found {} doctors", allDoctors.size());
        }
        return allDoctors.stream()
                .filter(Doctors::getIs_active)
                .map(this::toDTO).toList();
    }

    public DoctorsDTO getDoctorById(Long id) {
        logger.info("Getting Patient by ID {}", id);
        Doctors doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Doctor with ID {} not found", id);
                    return new ResourceNotFoundException("Doctor not found");
                });
        return toDTO(doctor);
    }

    public DoctorsDTO saveDoctors(DoctorsDTO doctorsDTO) {
        Doctors entity = toEntity(doctorsDTO);
        logger.info("Saving Doctor {}", entity);
        if (doctorsRepository.existsByEmail(entity.getEmail())) {
            logger.warn("Doctor with email {} already exists", entity.getEmail());
            throw new IllegalArgumentException("Doctor with email: " + entity.getEmail() + " already exists");
        }
        Doctors savedDoctor = doctorsRepository.save(entity);
        logger.info("Saved Doctor {}", savedDoctor);
        return toDTO(savedDoctor);
    }

    public List<DoctorsDTO> searchDoctorsByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            logger.warn("Keyword cannot be null or empty");
            throw new IllegalArgumentException("First name or last name must be provided");
        }

        String trimmedKeyword = keyword.trim().toLowerCase();

        List<Doctors> doctorsFound = doctorsRepository.findByLnameContainingOrFnameContaining(trimmedKeyword, trimmedKeyword);

        if (doctorsFound.isEmpty()) {
            logger.warn("No doctors found with name containing: {}", keyword);
        } else {
            logger.info("Found {} doctor(s) with name containing: {}", doctorsFound.size(), keyword);
        }

        return doctorsFound.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public List<DoctorsDTO> searchDoctorBySpecialization(String specialization) {
        if (specialization == null || specialization.trim().isEmpty()) {
            logger.warn("Specialization cannot be null or empty");
            throw new IllegalArgumentException("Specialization cannot be null or empty");
        }

        String trimmedSpecialization = specialization.trim().toLowerCase();

        List<Doctors> doctorsFoundBySpe = doctorsRepository.findDoctorsBySpecializationContaining(trimmedSpecialization);

        if (doctorsFoundBySpe.isEmpty()) {
            logger.warn("No doctors found with specialization: {}", trimmedSpecialization);
        } else {
            logger.info("Found {} doctor(s) with specialization: {}", doctorsFoundBySpe.size(), trimmedSpecialization);
        }

        return doctorsFoundBySpe.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public DoctorsDTO updateDoctorStatus(Long id, DoctorsDTO doctorsDTO) {
        logger.info("Updating doctor's status with ID {}", id);
        Doctors existingDoc = doctorsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Doctor with ID {} not found", id);
                    return new ResourceNotFoundException("Doctor not found");
                });
        existingDoc.setIs_active(doctorsDTO.getIs_active());
        logger.info("Doctor's status with ID {} has been updated successfully", id);
        return toDTO(doctorsRepository.save(existingDoc));
    }

    private DoctorsDTO toDTO(Doctors entity) {
        logger.info("Converting doctor {} to DTO", entity);
        DoctorsDTO dto = new DoctorsDTO();
        dto.setId(entity.getId());
        dto.setFname(entity.getFname());
        dto.setLname(entity.getLname());
        dto.setEmail(entity.getEmail());
        dto.setSpecialization(entity.getSpecialization());
        dto.setIs_active(entity.getIs_active());
        logger.info("Converted doctor {} to DTO", dto);
        return dto;

    }

    private Doctors toEntity(DoctorsDTO dto) {
        logger.info("Converting Doctors to Entity {}", dto);
        Doctors entity = new Doctors();
        entity.setId(dto.getId());
        entity.setLname(dto.getLname());
        entity.setEmail(dto.getEmail());
        entity.setSpecialization(dto.getSpecialization());
        entity.setIs_active(dto.getIs_active());
        logger.info("Converted Doctors to Entity {}", entity);
        return entity;
    }

}
