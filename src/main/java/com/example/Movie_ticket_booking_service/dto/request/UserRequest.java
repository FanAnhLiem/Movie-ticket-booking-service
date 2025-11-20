package com.example.Movie_ticket_booking_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;
    private String confirmPassword;
    private String name;
    private String phone;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthday;

    @AssertTrue(message = "Mật khẩu và xác nhận mật khẩu không khớp")
    public boolean isPasswordConfirmed() {
        if (password == null || confirmPassword == null) return false;
        return password.equals(confirmPassword);
    }
}
