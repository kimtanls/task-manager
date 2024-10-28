package com.example.task_management.controller.auth;

import com.example.task_management.dto.AuthRequest;
import com.example.task_management.dto.AuthResponse;
import com.example.task_management.dto.SignupRequest;
import com.example.task_management.dto.UserDto;
import com.example.task_management.entity.User;
import com.example.task_management.enums.UserRole;
import com.example.task_management.repository.UserRepository;
import com.example.task_management.service.auth.AuthService;
import com.example.task_management.service.jwt.UserService;
import com.example.task_management.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser (@RequestBody SignupRequest signupRequest){
        if (authService.hasUserWithEmail(signupRequest.getEmail())){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("user already exist with email");
        }
        UserDto createdUserDto = authService.signupUser(signupRequest);
        if (createdUserDto == null)
            return ResponseEntity.status(HttpStatus.CREATED).body("user not created");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(),
                    authRequest.getPassword()));
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Incorrect user name or password");
        }
        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authRequest.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(authRequest.getEmail());
        final String jwtToken = jwtUtil.generateToken(userDetails);
        AuthResponse authResponse = new AuthResponse();
        if (optionalUser.isPresent()){
            authResponse.setJwt(jwtToken);
            authResponse.setUserId(optionalUser.get().getId());
            authResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authResponse;
    }
}
