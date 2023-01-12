package by.grigoryev.linkshortener.service;

import by.grigoryev.linkshortener.dto.auth.AuthenticationRequest;
import by.grigoryev.linkshortener.dto.auth.AuthenticationResponse;
import by.grigoryev.linkshortener.dto.auth.RegisterRequest;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

}
