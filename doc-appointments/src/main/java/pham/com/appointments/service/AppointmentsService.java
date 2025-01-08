package pham.com.appointments.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pham.com.appointments.dto.AppointmentsDTO;
import pham.com.appointments.exception.ResourceNotFoundException;
import pham.com.appointments.model.Appointments;
import pham.com.appointments.repository.AppointmentsRepository;
import pham.com.doctors.repository.DoctorsRepository;
import pham.com.doctors.model.Doctors;
import pham.com.doctors.model.Specialization;
import pham.com.patients.model.Patients;
import pham.com.patients.repository.PatientsRepository;

import java.util.List;

@Service
public class AppointmentsService {
    private final AppointmentsRepository appointmentsRepository;
    private static final Logger logger = LoggerFactory.getLogger(AppointmentsService.class);
    private final PatientsRepository patientsRepository;
    private final DoctorsRepository doctorsRepository;


    @Autowired
    public AppointmentsService(AppointmentsRepository appointmentsRepository, PatientsRepository patientsRepository, DoctorsRepository doctorsRepository) {
        this.appointmentsRepository = appointmentsRepository;
        this.patientsRepository = patientsRepository;
        this.doctorsRepository = doctorsRepository;
    }


    public List<AppointmentsDTO> getAllAppointments() {
        logger.info("getting all appointments");
        List<Appointments> allAppointments = appointmentsRepository.findAll();
        if (allAppointments.isEmpty()) {
            logger.warn("No Appointments found");
        } else {
            logger.info("Found {} appointments", allAppointments.size());
        } return allAppointments.stream().map(this::toDTO).toList();
    }


    public AppointmentsDTO getAppointmentById(Long id) {
        logger.info("Getting appointment by id {}", id);
        Appointments appointment = appointmentsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Appointment with ID {} not found", id);
                    return new ResourceNotFoundException("Appointment not found");
                });
        return toDTO(appointment);
    }


    // only make appointment with doctor active
    // patient with age > 18 can not make appointment with pediatrician doctor

    public AppointmentsDTO createAppointment(AppointmentsDTO appointmentsDTO) {
        Long doctorId = appointmentsDTO.getDoctorId();
        Doctors doctor = doctorsRepository.findById(doctorId).orElseThrow(
                () -> new ResourceNotFoundException("Doctor not found")
        );
        if (!doctor.isIs_active()) {
            throw new IllegalStateException("Doctor is not active");
        }

        Long patientId = appointmentsDTO.getPatientId();
        Patients patient = patientsRepository.findById(patientId).orElseThrow(
                () -> new ResourceNotFoundException("Patient not found")
        );
        if (patient.getAge() > 18 && doctor.getSpecialization() == Specialization.PEDIATRIC) {
            throw new IllegalArgumentException("Patient cannot make an appointment with a pediatrician doctor");
        }

        logger.info("Creating appointment for patient ID: {} with doctor ID: {}", patientId, doctorId);

        Appointments appointments = toEntity(appointmentsDTO);
        Appointments savedAppointments = appointmentsRepository.save(appointments);

        logger.info("Saved appointment with ID: {}", savedAppointments.getId());
        return toDTO(savedAppointments);
    }


    public AppointmentsDTO updateAppointment(Long id, AppointmentsDTO dto) {
        logger.info("Updating appointment {}", dto);
        Appointments existingAppoint = appointmentsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Appointment with ID {} not found", id);
                    return new ResourceNotFoundException("Appointment not found");
                });
        existingAppoint.setAppointmentDate(dto.getAppointmentDate());
        Appointments updated = appointmentsRepository.save(existingAppoint);
        logger.info("Updated appointment {}", updated);
        return toDTO(updated);
    }


    public void deleteAppointment(Long id) {
        logger.info("Deleting appointment {}", id);
        Appointments appointment = appointmentsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Appointment with ID {} not found or has been deleted", id);
                    return new ResourceNotFoundException("Appointment not found");
                });
        appointmentsRepository.delete(appointment);
        logger.info("Deleted appointment {}", id);
    }


    public List<AppointmentsDTO> getAppointmentByPatient(Long patientId) {
        logger.info("Getting appointment by patient {}", patientId);
        if (patientId == null || !patientsRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("No appointments found for the specified patient");
        }
        List<Appointments> appointmentsByPatient = appointmentsRepository.findByPatientId(patientId);
        if (appointmentsByPatient.isEmpty()) {
            logger.warn("No Appointments with this patient found");
        } else {
            logger.info("Found {} appointments of with this patient", appointmentsByPatient.size());
        }
        return appointmentsByPatient.stream().map(this::toDTO).toList();
    }


    private AppointmentsDTO toDTO(Appointments appointments) {
        logger.info("Converting appointments to DTO");
        AppointmentsDTO dto = new AppointmentsDTO();
        dto.setId(appointments.getId());
        dto.setPatientId(appointments.getPatient().getId());
        dto.setDoctorId(appointments.getDoctor().getId());
        dto.setService(appointments.getServices());
        dto.setAppointmentDate(appointments.getAppointmentDate());
        logger.info("Converted appointments to DTO");
        return dto;
    }


    private Appointments toEntity(AppointmentsDTO dto) {
        logger.info("Converting appointments DTO to Entity");
        Appointments entity = new Appointments();
        Patients patient = patientsRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        Doctors doctor = doctorsRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        entity.setId(dto.getId());
        entity.setPatient(patient);
        entity.setDoctor(doctor);
        entity.setServices(dto.getService());
        entity.setAppointmentDate(dto.getAppointmentDate());
        logger.info("Converted appointments DTO to Entity");
        return entity;
    }

}
