
package com.finwing.service;

import com.finwing.dto.UserRegistrationDto;
import com.finwing.entity.User;
import com.finwing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            return "EMAIL_EXISTS";
        }
        if (userRepository.existsByUsername(dto.getUserName())) {
            return "USERNAME_EXISTS";
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFullName(dto.getFullName());
        user.setCurrency(dto.getCurrency());
        user.setUsername(dto.getUserName());
        user.setGender(dto.getGender());
        user.setMobileNo(dto.getMobileNo());
        user.setIsActive(true);
        userRepository.save(user);
        return "SUCCESS";
    }

    public Optional<User> loginUser(String email, String rawPassword) {
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optUser.isPresent() && passwordEncoder.matches(rawPassword, optUser.get().getPassword())) {
            return optUser;
        }
        return Optional.empty();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
