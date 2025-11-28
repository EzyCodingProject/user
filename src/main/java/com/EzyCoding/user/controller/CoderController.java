package com.EzyCoding.user.controller;

import com.EzyCoding.user.model.Coder;
import com.EzyCoding.user.service.CoderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class CoderController {
    @Autowired
    private CoderService coderService;
    @Autowired
    private OAuth2AuthorizedClientService clientService;
    // Create doctor
//    @GetMapping("/get")
//    public List<Coder> getAllCoders() {
//        return coderService.getAllCoders();
//    }

    @GetMapping("/token")
    public String getToken(@AuthenticationPrincipal OAuth2AuthenticationToken auth) {

        OAuth2AuthorizedClient client =
                clientService.loadAuthorizedClient(
                        auth.getAuthorizedClientRegistrationId(),
                        auth.getName()
                );

        String accessToken = client.getAccessToken().getTokenValue();
        String idToken = auth.getPrincipal().getAttribute("id_token");

        return accessToken;
    }

    @GetMapping("/")
    public String getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2AuthorizedClient authorizedClient = clientService.loadAuthorizedClient(
                    oauthToken.getAuthorizedClientRegistrationId(), // e.g., "google"
                    oauthToken.getName()                        // The principal name (user ID/email)
            );

            if (authorizedClient != null) {
                String accessTokenValue = authorizedClient.getAccessToken().getTokenValue();
                System.out.println(accessTokenValue);
                return "Access Token: " + accessTokenValue;
            }
            // The access token is typically managed by Spring Security internally,
            // but can be loaded via the AuthorizedClientService as shown above.
            // Direct access to the token might be more complex without the service,
            // as Spring abstracts the token management.
        }
        return coderService.getCurrentUser(principal);
    }

    @GetMapping("/check/{handle}")
    public String getHandle(@PathVariable String handle){
        System.out.println("Running");
        return coderService.getHandle(handle);
    }
}
