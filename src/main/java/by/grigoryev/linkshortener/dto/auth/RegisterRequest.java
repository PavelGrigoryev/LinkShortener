package by.grigoryev.linkshortener.dto.auth;

public record RegisterRequest(
        String firstname,
        String lastname,
        String email,
        String password
) {
}
