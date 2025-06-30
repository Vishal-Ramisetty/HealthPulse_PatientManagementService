package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {

    public static PatientResponseDTO toDTO(Patient p) {
        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(p.getId().toString());
        patientResponseDTO.setName(p.getName());
        patientResponseDTO.setEmail(p.getEmail());
        patientResponseDTO.setAddress(p.getAddress());
        patientResponseDTO.setDateOfBirth(p.getDateOfBirth().toString());
        return patientResponseDTO;
    }

    public static Patient toEntity(PatientRequestDTO p) {
        Patient patient = new Patient();
        patient.setName(p.getName());
        patient.setEmail(p.getEmail());
        patient.setAddress(p.getAddress());
        patient.setDateOfBirth(LocalDate.parse(p.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(p.getRegisteredDate()));
        return patient;
    }
}
