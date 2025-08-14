package com.Ijse.EventEase.service;

import com.Ijse.EventEase.dto.AuthDTO;
import com.Ijse.EventEase.dto.AuthResponceDto;
import com.Ijse.EventEase.dto.RegisterDTO;
import com.Ijse.EventEase.entity.User;
import com.Ijse.EventEase.enums.Role;
import com.Ijse.EventEase.repository.UserRepository;
import com.Ijse.EventEase.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
   private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponceDto authenticate(AuthDTO authDTO) {
        // validate credentials
        User user= userRepository.findByEmail(authDTO.getEmail())
                .orElseThrow(()->new RuntimeException(authDTO.getEmail()+"User not found"));
        // check password
        if (!passwordEncoder.matches(
                authDTO.getPassword(),
                user.getPassword())){
            throw new BadCredentialsException("Invalid credentials");
        }
        // generate token
        String token=jwtUtil.generateToken(authDTO.getEmail());
        return new AuthResponceDto(token);
    }

    @Override
    public String registerUser(RegisterDTO registerDTO) {
        if (userRepository.findByEmail(registerDTO.getEmail())
                .isPresent()){
            throw new RuntimeException(registerDTO.getEmail()+"UserEmail already exists");
        }
        User user=User.builder()
                .name(registerDTO.getName())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(Role.valueOf(registerDTO.getRole()))
                .build();
        userRepository.save(user);
        return "User registered successfully";
    }
}
