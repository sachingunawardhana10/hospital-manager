
package com.example.hospitalmanager.service;

import com.example.hospitalmanager.dto.DoctorCreateRequest;
import com.example.hospitalmanager.dto.StaffResponse;
import com.example.hospitalmanager.exception.ApiException;
import com.example.hospitalmanager.model.AppUser;
import com.example.hospitalmanager.model.Role;
import com.example.hospitalmanager.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StaffService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StaffService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public StaffResponse createDoctor(DoctorCreateRequest request) {

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Username is already taken"
            );
        }

        AppUser doctor = new AppUser();
        doctor.setUsername(request.username());
        doctor.setPasswordHash(
                passwordEncoder.encode(request.password())
        );
        doctor.setRole(Role.DOCTOR);

        AppUser savedDoctor = userRepository.save(doctor);

        return convertToResponse(savedDoctor);
    }

    @Transactional(readOnly = true)
    public List<StaffResponse> getAllDoctors() {

        return userRepository.findAllByRole(Role.DOCTOR)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private StaffResponse convertToResponse(AppUser user) {
        return new StaffResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
