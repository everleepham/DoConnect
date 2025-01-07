package pham.com.doctors.dto;

import pham.com.doctors.model.Specialization;
import jakarta.validation.constraints.*;

public class DoctorsDTO {

    private Long id;

    @NotNull(message = "First name required")
    private String fname;

    @NotNull(message = "Last name required")
    private String lname;

    @NotNull(message = "Email required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Specialization is required")
    private Specialization specialization;

    @NotNull(message = "Status is required")
    private boolean is_active;

    // Getter Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public DoctorsDTO(Long id, String fname, String lname, String email, Specialization specialization, boolean is_active) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.specialization = specialization;
        this.is_active = is_active;
    }

    public DoctorsDTO() {
    }
}