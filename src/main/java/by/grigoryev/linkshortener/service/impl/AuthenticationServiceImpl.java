package by.grigoryev.linkshortener.service.impl;

import by.grigoryev.linkshortener.dto.AuthenticationRequest;
import by.grigoryev.linkshortener.dto.AuthenticationResponse;
import by.grigoryev.linkshortener.dto.RegisterRequest;
import by.grigoryev.linkshortener.exception.UserEmailNotFoundException;
import by.grigoryev.linkshortener.model.Role;
import by.grigoryev.linkshortener.model.User;
import by.grigoryev.linkshortener.repository.UserRepository;
import by.grigoryev.linkshortener.security.JwtService;
import by.grigoryev.linkshortener.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        log.info("register " + savedUser);
        log.info("register with token " + jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserEmailNotFoundException("User with email " + request.getEmail() + " not found"));
        String jwtToken = jwtService.generateToken(user);
        log.info("authenticate " + user);
        log.info("authenticate with token " + jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
