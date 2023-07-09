package com.aptech.coursemanagementserver.controllers;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.GLOBAL_EXCEPTION;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.coursemanagementserver.dtos.CourseInterface;
import com.aptech.coursemanagementserver.dtos.PermissionDto;
import com.aptech.coursemanagementserver.dtos.RegisterRequestDto;
import com.aptech.coursemanagementserver.dtos.UserDto;
import com.aptech.coursemanagementserver.dtos.UserProfileDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.enums.Role;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.exceptions.IsExistedException;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.models.Permissions;
import com.aptech.coursemanagementserver.models.Roles;
import com.aptech.coursemanagementserver.models.Token;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.models.UserPermission;
import com.aptech.coursemanagementserver.repositories.CourseRepository;
import com.aptech.coursemanagementserver.repositories.TokenRepository;
import com.aptech.coursemanagementserver.repositories.UserPermissionRepository;
import com.aptech.coursemanagementserver.services.authServices.AuthenticationService;
import com.aptech.coursemanagementserver.services.authServices.CurrentUser;
import com.aptech.coursemanagementserver.services.authServices.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "User Endpoints")
public class UserController {
    private final CourseRepository courseRepository;
    private final TokenRepository tokenRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserPermissionRepository userPermissionRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<UserProfileDto> getCurrentUser(@CurrentUser User user) {
        var myPermission = user.getUserPermissions().stream()
                .filter(up -> up.getUser().getId() == user.getId()).map(p -> p.getPermissionName()).toList();

        var userProfileDto = UserProfileDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .name(user.getName())
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .type(AntType.success)
                .role(user.getRole())
                .permissions(myPermission)
                .isNotify(user.isNotify())
                .status(user.getUserStatus())
                .created_at(user.getCreated_at())
                .message("Get current logged in user success.")
                .build();
        return ResponseEntity.ok(userProfileDto);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<UserDto>> findAllHasRoleUSER() {
        try {
            return ResponseEntity.ok(userService.findAllHasRoleUSER());

        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (IsExistedException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(GLOBAL_EXCEPTION);
        }
    }

    @GetMapping("/user/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<UserDto>> findAllExceptRoleADMIN() {
        try {
            return ResponseEntity.ok(userService.findAllExceptRoleADMIN());

        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (IsExistedException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(GLOBAL_EXCEPTION);
        }
    }

    @GetMapping("/role")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<Roles>> findAllRoleExceptRoleADMIN() {
        try {
            List<Roles> roles = userService.findAllRoleExceptRoleADMIN();
            return ResponseEntity.ok(roles);

        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (IsExistedException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(GLOBAL_EXCEPTION);
        }
    }

    @GetMapping("/permission")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<Permissions>> findAllPermissionExceptPermissionADMIN() {
        try {
            return ResponseEntity.ok(userService.findAllPermissionExceptPermissionADMIN());

        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (IsExistedException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(GLOBAL_EXCEPTION);
        }
    }

    @PostMapping("/user/organize")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<BaseDto> createOrganizationUser(@RequestBody RegisterRequestDto dto) {

        try {
            if (dto.getRole() == Role.ADMIN) {
                throw new BadRequestException("Admin is unique from our organize.");
            }
            authenticationService.generateTokenWithoutVerify(authenticationService.register(dto));
            return ResponseEntity.ok(BaseDto.builder().message("Register successfully.").type(AntType.success).build());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (IsExistedException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(GLOBAL_EXCEPTION);
        }
    }

    // @PutMapping("/user/organize")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<BaseDto> updateOrganizationUser(@RequestBody
    // RegisterRequestDto dto) {

    // try {
    // if (dto.getRole() == Role.ADMIN) {
    // throw new BadRequestException("Admin is unique from our organize.");
    // }
    // authenticationService.generateTokenWithoutVerify(authenticationService.register(dto));
    // return ResponseEntity.ok(BaseDto.builder().message("Register
    // successfully.").type(AntType.success).build());
    // } catch (BadRequestException e) {
    // throw new BadRequestException(e.getMessage());
    // } catch (IsExistedException e) {
    // throw new BadRequestException(e.getMessage());
    // } catch (Exception e) {
    // throw new BadRequestException(GLOBAL_EXCEPTION);
    // }
    // }

    @PutMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE', 'USER')")
    public ResponseEntity<BaseDto> updateUser(@RequestBody UserDto dto) {

        try {
            User user = userService.findById(dto.getId()).orElseThrow(() -> new NoSuchElementException(
                    "The user with userId: [" + dto.getId() + "] is not exist."));

            user.setFirst_name(dto.getFirst_name())
                    .setLast_name(dto.getLast_name())
                    .setName(dto.getName())
                    .setImageUrl(dto.getImageUrl())
                    .setUserStatus(dto.getStatus());

            userService.save(user);

            return ResponseEntity
                    .ok(BaseDto.builder().message("Update user successfully.").type(AntType.success).build());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(GLOBAL_EXCEPTION);
        }
    }

    @PutMapping("/user/update-permission")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<BaseDto> updateUserPermission(@RequestBody PermissionDto dto) {

        try {
            // 1. update role (table user) of Employee from to manager
            // 2. Delete permission (table user_permission) of user with role Employee
            // 3. Insert permission of role manager for user into table user_permission
            User user = userService.findById(dto.getUserId()).orElseThrow(() -> new NoSuchElementException(
                    "The user with userId: [" + dto.getUserId() + "] is not exist."));
            user.setRole(dto.getRole().getName());
            userService.save(user);

            userPermissionRepository.deleteUserPermission(user.getId());

            List<UserPermission> userPermissions = new ArrayList<>();
            for (Permissions permission : dto.getPermissions()) {
                UserPermission userPermission = new UserPermission();
                userPermission.setPermission(permission);
                userPermission.setPermissionName(permission.getPermission());
                userPermission.setUser(user);
                userPermission.setUsername(user.getUsername());
                userPermissions.add(userPermission);
            }

            userPermissionRepository.saveAll(userPermissions);

            return ResponseEntity
                    .ok(BaseDto.builder().message("Update user successfully.").type(AntType.success).build());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(GLOBAL_EXCEPTION);
        }
    }

    @PutMapping("/user/notify")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<BaseDto> updateUserNotify(@RequestBody UserDto dto) {

        try {
            User user = userService.findById(dto.getId()).orElseThrow(() -> new NoSuchElementException(
                    "The user with userId: [" + dto.getId() + "] is not exist."));

            user.setNotify(dto.isNotify());
            userService.save(user);

            return ResponseEntity
                    .ok(BaseDto.builder().message("Update notify successfully.").type(AntType.success).build());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(GLOBAL_EXCEPTION);
        }
    }

    @DeleteMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<BaseDto> deleteUser(long userId) {

        try {
            List<CourseInterface> allCourseOfUser = courseRepository.findAllCoursesByUserId(userId);
            if (allCourseOfUser.stream().anyMatch(c -> c.getEnrollmentCount() > 0) == true) {
                throw new BadRequestException("Can't delete user 've already had enrollment");
            }
            userService.findById(userId).orElseThrow(() -> new NoSuchElementException(
                    "The user with userId: [" + userId + "] is not exist."));

            List<Token> tokens = tokenRepository.findAllTokenByUserId(userId);
            tokenRepository.deleteAll(tokens);
            userService.deleteById(userId);

            return ResponseEntity
                    .ok(BaseDto.builder().message("Delete user successfully.").type(AntType.success).build());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException(GLOBAL_EXCEPTION);
        }
    }
}
