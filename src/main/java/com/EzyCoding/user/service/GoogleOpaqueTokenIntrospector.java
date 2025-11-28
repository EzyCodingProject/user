package com.EzyCoding.user.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;

@Service
public class GoogleOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private static final String TOKEN_INFO_URL =
            "https://oauth2.googleapis.com/tokeninfo?access_token={token}";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {

        ResponseEntity<Map> response =
                restTemplate.getForEntity(TOKEN_INFO_URL, Map.class, token);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw invalid("Invalid response from Google");
        }

        Map<String, Object> claims = response.getBody();
        if (claims == null) {
            throw invalid("Empty response from Google");
        }

        if (claims.containsKey("error")) {
            throw invalid("Token rejected: " + claims.get("error"));
        }

        // 🔥 FIX: convert exp, iat to Instant
        convertNumericClaim(claims, "exp");
        convertNumericClaim(claims, "iat");

        Collection<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new DefaultOAuth2AuthenticatedPrincipal(claims, authorities);
    }

    private void convertNumericClaim(Map<String, Object> claims, String claimName) {
        if (!claims.containsKey(claimName)) return;

        Object value = claims.get(claimName);

        try {
            if (value instanceof String) {
                long epoch = Long.parseLong((String) value);
                claims.put(claimName, Instant.ofEpochSecond(epoch));
            } else if (value instanceof Number) {
                long epoch = ((Number) value).longValue();
                claims.put(claimName, Instant.ofEpochSecond(epoch));
            }
        } catch (Exception e) {
            // If conversion fails, drop the claim or keep original
            claims.remove(claimName);
        }
    }

    private OAuth2AuthenticationException invalid(String message) {
        return new OAuth2AuthenticationException(
                new OAuth2Error("invalid_token", message, null)
        );
    }
}