package com.movieBackend.controllers;

import com.movieBackend.dtos.RoleDto;
import com.movieBackend.dtos.RoleManagementDto;
import com.movieBackend.services.abstracts.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PutMapping("/role")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<String> updateUserRole(@RequestBody RoleManagementDto roleManagementDto) {
        userRoleService.updateUserRole(roleManagementDto);
        return ResponseEntity.ok("The user's role has been successfully updated.");
    }

    @PostMapping("/role")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<String> saveRole(@RequestBody RoleDto roleDto) {
        userRoleService.saveRole(roleDto);
        return ResponseEntity.ok("New role added successfully!");
    }

}

