package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.UserIdDoesNotExistsException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    PatientRepository patientRepository;

    // Here no need of auto-wired as a constructor with single parameter has auto-dependency injection
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOS = patients.stream().map(patient1 -> PatientMapper.toDTO(patient1)).toList();
        return patientResponseDTOS;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        Patient patient = PatientMapper.toEntity(patientRequestDTO);
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        patientRepository.save(patient);
        return PatientMapper.toDTO(patient);
    }

    public PatientResponseDTO updatePatient(UUID id, @Valid PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new UserIdDoesNotExistsException("Patient not found with id  - "+ id));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        patientRequestDTO.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patientRepository.save(patient);
        return PatientMapper.toDTO(patient);

    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
