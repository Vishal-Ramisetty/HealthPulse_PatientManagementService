package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private PatientService patientService;

    @Autowired
    public void PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        List<PatientResponseDTO> patientResponseDTOS = patientService.getAllPatients();
        return ResponseEntity.ok().body(patientResponseDTOS);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createNewPatient(@Valid @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO patientResponseDTOS = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTOS);
    }

}
