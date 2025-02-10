package com.movieBackend.services.abstracts;

import com.movieBackend.dtos.RoleDto;
import com.movieBackend.dtos.RoleManagementDto;

public interface UserRoleService {

    void updateUserRole(RoleManagementDto roleManagementDto);

    void saveRole(RoleDto roleDto);
}
