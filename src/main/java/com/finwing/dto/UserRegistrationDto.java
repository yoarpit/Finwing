package com.finwing.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates getters, setters, toString, etc.
@NoArgsConstructor // Generates the empty constructor: UserRegistrationDto()
@AllArgsConstructor // Generates a constructor with all fields
public class UserRegistrationDto {
    private String userName;
    private String fullName;
    private String email;
    private String password;
    private String mobile_no;
    private String gender;
    private String currency;
    private Boolean is_active;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private java.time.LocalDateTime created_at;

   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
   private java.time.LocalDateTime update_it;

}