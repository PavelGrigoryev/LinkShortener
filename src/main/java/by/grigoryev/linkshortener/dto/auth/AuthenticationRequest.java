package by.grigoryev.linkshortener.dto.auth;

public record AuthenticationRequest(
        String email,
        String password
) {
}
