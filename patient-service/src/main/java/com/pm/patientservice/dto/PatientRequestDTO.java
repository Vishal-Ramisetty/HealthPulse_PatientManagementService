package com.pm.patientservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PatientRequestDTO {

    @NotBlank(message = "Name is a required Field")
    private String name;
    @NotBlank(message = "Address is a required Field")
    private String address;
    @NotBlank(message = "email is a mandatory Field")
    @Email(message = "Email id Should be of a valid format eg: abc@yahoo.com")
    private String email;
    @NotNull(message = "Date of birth is a required Field")
    private String dateOfBirth;
    @NotNull(message = "Registered date is a required Field")
    private String registeredDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }


}
