
package com.example.hospitalmanager.controller;

import com.example.hospitalmanager.dto.DoctorCreateRequest;
import com.example.hospitalmanager.dto.StaffResponse;
import com.example.hospitalmanager.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping("/doctors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffResponse> createDoctor(
            @Valid @RequestBody DoctorCreateRequest request
    ) {
        StaffResponse response = staffService.createDoctor(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/doctors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StaffResponse>> getAllDoctors() {

        return ResponseEntity.ok(staffService.getAllDoctors());
    }
}
