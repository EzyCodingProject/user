package com.EzyCoding.user.service;

import com.EzyCoding.user.model.Coder;
import com.EzyCoding.user.producer.HandleMessageProducer;
import com.EzyCoding.user.repository.CoderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CoderService {
    @Autowired
    private CoderRepository coderRepository;
    @Value("${api.handle.check.url}")
    String checkHandleApiUrl;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HandleMessageProducer handleMessageProducer;
//    @Autowired
//    private UserCredentialRepository userCredentialRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    AuthenticationManager authenticationManager;
//    @Autowired
//    JWTService jwtService;
//
//    public ResponseEntity<String> createUser(UserCredential credential) {
//        try {
//            // Optional: check if the user credential already exists
//            if (userCredentialRepository.existsByAccountName(credential.getAccountName())) {
//                return ResponseEntity.status(HttpStatus.CONFLICT)
//                        .body("User already exists");
//            }
//            credential.setPassword(passwordEncoder.encode(credential.getPassword()));
//            userCredentialRepository.save(credential);
//            log.info("Saving credential to db: {} with the id: {}", credential.getAccountName(), credential.getCredentialId());
//
//            Coder user = new Coder(credential.getCredentialId());
//            coderRepository.save(user);
//
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body("User created successfully");
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("User creation failed: " + e.getMessage());
//        }
//    }
//
//    public String login(UserCredential credential){
//        Authentication authentication = authenticationManager
//                .authenticate(new UsernamePasswordAuthenticationToken(credential.getAccountName(),
//                        credential.getPassword()));
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return "Invalid username or password";
//        }
//        return jwtService.generateToken(credential.getAccountName());
//    }
//    public List<Coder> getAllCoders() {
//        return coderRepository.findAll();
//    }
    public String getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return "No user logged in";
        }
        String tokenId = principal.getAttribute("sub");
        System.out.println(tokenId);
        return principal.getAttribute("email");
    }

    public String getHandle(String handle){
        System.out.println("Handle: " + handle);
        String url = String.format(checkHandleApiUrl, handle);
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                // handleMessageProducer.sendHandle(handle);
                return "Success";
            } else {
                return "Failed";
            }
        } catch (Exception ex) {
            return "Failed";
        }
    }


}
