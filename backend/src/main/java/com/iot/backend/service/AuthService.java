package com.iot.backend.service;

import com.iot.backend.dto.AuthResponse;
import com.iot.backend.dto.LoginRequest;
import com.iot.backend.dto.RegisterRequest;
import com.iot.backend.model.User;
import com.iot.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final String ADMIN_USERNAME = "admin";

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthResponse(null, null, null, false, "El nombre de usuario ya existe");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthResponse(null, null, null, false, "El correo electrónico ya existe");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPasswordHash(hashedPassword);

        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);

        return new AuthResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getRole(),
                true,
                "Usuario registrado correctamente"
        );
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null) {
            return new AuthResponse(null, null, null, false, "Usuario no encontrado");
        }

        boolean passwordOk =
                passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if (!passwordOk) {
            return new AuthResponse(null, null, null, false, "Contraseña incorrecta");
        }

        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                true,
                "Inicio de sesión correcto"
        );
    }
}