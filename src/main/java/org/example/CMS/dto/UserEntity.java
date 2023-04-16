package org.example.CMS.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserEntity {

    private String username;
    private String password;
    private List<String> roles;
}
