package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.UserDto;
import com.aptech.coursemanagementserver.models.Permissions;
import com.aptech.coursemanagementserver.models.Roles;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.repositories.PermissionsRepository;
import com.aptech.coursemanagementserver.repositories.RolesRepository;
import com.aptech.coursemanagementserver.repositories.UserRepository;
import com.aptech.coursemanagementserver.services.authServices.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PermissionsRepository permissionsRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        return auth.getPrincipal() == "anonymousUser" ? null : (User) auth.getPrincipal();

    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<UserDto> findAllHasRoleUSER() {
        List<User> users = userRepository.findAllHasRoleUSER();

        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = toDto(user);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public List<User> findAllExceptRoleUSER() {
        List<User> users = userRepository.findAllExceptRoleUSER();

        // List<UserDto> userDtos = new ArrayList<>();
        // for (User user : users) {
        // UserDto userDto = toDto(user);
        // userDtos.add(userDto);
        // }
        return users;
    }

    public List<Roles> findAllRoleExceptRoleADMIN() {
        return rolesRepository.findAllRoleExceptRoleADMIN();
    }

    public List<Permissions> findAllPermissionExceptPermissionADMIN() {
        return permissionsRepository.findAllPermissionExceptPermissionADMIN();
    }

    @Override
    public List<UserDto> findAllExceptRoleADMIN() {

        List<User> users = userRepository.findAllExceptRoleADMIN(findCurrentUser().getId());

        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = toDto(user);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public boolean checkIsUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = false;
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            isUser = true;
        }
        return isUser;
    }

    public boolean isContains(final List<User> list, final String email) {
        return list.stream().anyMatch(o -> o.getEmail().equals(email));
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteById(long userId) {
        userRepository.deleteById(userId);
    }

    private UserDto toDto(User user) {
        var permission = user.getUserPermissions().stream()
                .filter(up -> up.getUser().getId() == user.getId()).map(p -> p.getPermissionName())
                .collect(Collectors.toSet());

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .name(user.getName())
                .role(user.getRole())
                .permissions(permission)
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .email(user.getEmail())
                .password(user.getPassword())
                .isVerified(user.isVerified())
                .imageUrl(user.getImageUrl())
                .status(user.getUserStatus())
                .created_at(user.getCreated_at())
                .updated_at(user.getUpdated_at())
                .build();
        return userDto;
    }
}
