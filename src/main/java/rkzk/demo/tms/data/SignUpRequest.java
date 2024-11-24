package rkzk.demo.tms.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignUpRequest(
        @NotNull
        String username,
        @Email
        @NotNull
        String email,
        @NotNull
        String password) {
}
