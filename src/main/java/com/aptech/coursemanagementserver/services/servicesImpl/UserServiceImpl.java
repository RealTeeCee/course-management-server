package com.aptech.coursemanagementserver.services.servicesImpl;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.repositories.UserRepository;
import com.aptech.coursemanagementserver.services.authServices.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void deleteById(long userId) {
        userRepository.deleteById(userId);
    }

}
