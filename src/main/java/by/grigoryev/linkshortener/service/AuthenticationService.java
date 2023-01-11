package by.grigoryev.linkshortener.service;

import by.grigoryev.linkshortener.dto.AuthenticationRequest;
import by.grigoryev.linkshortener.dto.AuthenticationResponse;
import by.grigoryev.linkshortener.dto.RegisterRequest;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

}
