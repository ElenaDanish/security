package ru.bft.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.bft.security.config.UsernamePwdAuthenticationProvider;
import ru.bft.security.model.LoginRequest;
import ru.bft.security.model.LoginResponse;
import ru.bft.security.service.CustomUserDetails;
import ru.bft.security.utils.JwtUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class Controller {

    @Autowired
    private CustomUserDetails userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsernamePwdAuthenticationProvider usernamePwdAuthenticationProvider;

    @GetMapping("/hello")
    public String getHello() {
        return "hello";
    }

    @GetMapping("/myInfo")
    public ResponseEntity<?> getMyInfo() {
        return ResponseEntity.ok("There will be insurer info");
    }

    @GetMapping("/contact")
    public ResponseEntity<?> getContact() {
        return ResponseEntity.ok("There will be contact info");
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = usernamePwdAuthenticationProvider.
                    authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        //генерация токена
        String token = jwtUtils.generateTokenFromUserName(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        LoginResponse response = new LoginResponse(token, userDetails.getUsername(), roles);
        return ResponseEntity.ok(response);
    }

}
