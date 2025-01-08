package pham.com.doctors.dto;

import pham.com.doctors.model.Specialization;
import jakarta.validation.constraints.*;

public class DoctorsDTO {
    @NotNull
    private Long id;

    private String fname;
    private String lname;
    private String email;
    private Specialization specialization;
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

    public boolean isIs_active() {
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