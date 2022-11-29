package com.javaprogram.modulespringcore.services;

import java.util.List;
import java.util.Optional;

import com.javaprogram.modulespringcore.models.User;
import com.javaprogram.modulespringcore.repositories.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> getUserById(long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userRepository.findByName(name, pageSize, pageNum);
    }

    public User createUser(User user) {
        return userRepository.create(user);
    }

    public Optional<User> updateUser(User user) {
        return userRepository.update(user);
    }

    public boolean deleteUser(long userId) {
        return userRepository.deleteById(userId);
    }
}
