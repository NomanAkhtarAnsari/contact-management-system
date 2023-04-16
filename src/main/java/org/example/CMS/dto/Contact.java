package org.example.CMS.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    @JsonIgnore
    private Long id;

    @NotNull(message = "First name should not be null")
    @Size(min = 2, max = 50, message = "First name length should be between 2 to 50 characters")
    private String firstName;

    @NotNull(message = "Last name should not be null")
    @Size(min = 2, max = 50, message = "Last name length should be between 2 to 50 characters")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    private String email;

    @Pattern(regexp = "^[6789]\\d{9}$")  //To validate Indian phone number
    private String phoneNumber;
}
