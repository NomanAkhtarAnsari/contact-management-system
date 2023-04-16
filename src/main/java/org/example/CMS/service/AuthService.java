package org.example.CMS.service;

import org.example.CMS.database.UserDao;
import org.example.CMS.dto.CredentialsDTO;
import org.example.CMS.dto.TokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.example.CMS.security.JwtTokenProvider;

@Service
@Slf4j
public class AuthService {

    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(UserDao userDao,
                       JwtTokenProvider jwtTokenProvider) {
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ResponseEntity<TokenDTO> validateUserAndGenerateToken(CredentialsDTO credentialsDTO) {
        try {
            userDao.findByUsernameAndPassword(credentialsDTO.getUsername(), credentialsDTO.getPassword())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + credentialsDTO.getUsername()));
            String token = jwtTokenProvider.createToken(credentialsDTO.getUsername());
            TokenDTO tokenDto = new TokenDTO(token);
            return ResponseEntity.ok(tokenDto);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
