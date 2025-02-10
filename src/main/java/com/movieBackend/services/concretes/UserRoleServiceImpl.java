package com.movieBackend.services.concretes;

import com.movieBackend.dtos.RoleDto;
import com.movieBackend.dtos.RoleManagementDto;
import com.movieBackend.entities.Role;
import com.movieBackend.entities.User;
import com.movieBackend.entities.UserRole;
import com.movieBackend.repositories.RoleRepository;
import com.movieBackend.repositories.UserRepository;
import com.movieBackend.repositories.UserRoleRepository;
import com.movieBackend.services.abstracts.UserRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void updateUserRole(RoleManagementDto roleManagementDto) {
        Optional<User> userOptional = userRepository.findById(roleManagementDto.getUserId());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found.");
        }

        User user = userOptional.get();
        Role newRole = roleRepository.findByRoleName(roleManagementDto.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found."));

        UserRole userRole = new UserRole();

        userRole.setUser(user);
        userRole.setRole(newRole);

        user.getUserRoles().add(userRole);

        userRepository.save(user);
    }

    @Override
    public void saveRole(RoleDto roleDto) {
        Role role = new Role();
        role.setRoleName(roleDto.getRoleName());

        roleRepository.save(role);
    }

}
