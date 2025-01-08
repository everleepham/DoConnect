package pham.com.appointments.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import pham.com.doctors.model.Doctors;
import pham.com.patients.model.Patients;

import java.time.LocalDateTime;

@Entity
public class Appointments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull
    private Patients patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull
    private Doctors doctor;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Service is required")
    private Services services;

    @NotNull(message = "Date is required")
    @Future(message = "Appointment date must be in the future")
    @Column(name = "appointment_date")
    private LocalDateTime appointmentDate;

    // Constructor
    public Appointments() {
    }

    public Appointments(Patients patient, Doctors doctor, Services services, LocalDateTime appointmentDate) {
        this.patient = patient;
        this.doctor = doctor;
        this.services = services;
        this.appointmentDate = appointmentDate;
    }

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patients getPatient() {
        return patient;
    }

    public void setPatient(Patients patient) {
        this.patient = patient;
    }

    public Doctors getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctors doctor) {
        this.doctor = doctor;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
