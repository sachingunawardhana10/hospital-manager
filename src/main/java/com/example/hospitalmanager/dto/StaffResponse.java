
package com.example.hospitalmanager.dto;

import com.example.hospitalmanager.model.Role;

public record StaffResponse(
        Long id,
        String username,
        Role role
) {}
