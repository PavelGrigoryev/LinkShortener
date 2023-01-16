package by.grigoryev.linkshortener.controller;

import by.grigoryev.linkshortener.dto.auth.AuthenticationRequest;
import by.grigoryev.linkshortener.dto.auth.AuthenticationResponse;
import by.grigoryev.linkshortener.dto.auth.RegisterRequest;
import by.grigoryev.linkshortener.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "The Authentication API")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Register", tags = "Authentication",
            requestBody = @io.swagger.v3.oas.annotations.parameters
                    .RequestBody(description = "RequestBody for register",
                    content = @Content(schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "firstname": "Pavel",
                                      "lastname": "Shishkin",
                                      "email": "Georgiy@mail.com",
                                      "password": "1234"
                                    }
                                    """))
            )
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(
            summary = "Authenticate", tags = "Authentication",
            requestBody = @io.swagger.v3.oas.annotations.parameters
                    .RequestBody(description = "RequestBody for authenticate",
                    content = @Content(schema = @Schema(implementation = AuthenticationRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "email": "Georgiy@mail.com",
                                      "password": "1234"
                                    }
                                    """))
            )
    )
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
