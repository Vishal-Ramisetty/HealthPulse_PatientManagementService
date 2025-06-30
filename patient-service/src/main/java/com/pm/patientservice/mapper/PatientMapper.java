package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.model.Patient;

public class PatientMapper {

    public static PatientResponseDTO toDTO(Patient p){
        PatientResponseDTO patientResponseDTO=new PatientResponseDTO();
        patientResponseDTO.setId(p.getId().toString());
        patientResponseDTO.setName(p.getName());
        patientResponseDTO.setEmail(p.getEmail());
        patientResponseDTO.setAddress(p.getAddress());
        patientResponseDTO.setDateOfBirth(p.getDateOfBirth().toString());
        return patientResponseDTO;
    }
}
