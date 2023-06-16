package com.aptech.coursemanagementserver.services.authServices;

import java.util.List;
import java.util.Optional;

import com.aptech.coursemanagementserver.models.User;

public interface UserService {
    public Optional<User> findById(long id);

    public void deleteById(long userId);

    Optional<User> findByEmail(String email);

    public boolean checkIsUser();

    public User findCurrentUser();

    public void save(User user);

    public boolean isContains(final List<User> list, final String email);
}
