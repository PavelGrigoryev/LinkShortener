package by.grigoryev.linkshortener.dto.auth;

public record AuthenticationResponse(
        String token,
        String tokenExpiration
) {
}
