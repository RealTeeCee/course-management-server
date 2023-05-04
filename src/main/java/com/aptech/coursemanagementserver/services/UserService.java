package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.models.Role;
import com.aptech.coursemanagementserver.models.User;

public interface UserService {
    User saveUser(User user);

    Role saveRole(Role role);

    void addRoleToUser(String email, String roleName);

    User getUser(String email); // Email is UNIQUE

    List<User> getUsers();
}
