
package com.example.hospitalmanager.dto;

import com.example.hospitalmanager.model.Role;

public record UserResponse(

        Long id,

        String username,

        Role role

) {}
