package com.example.makeboard0629.model;

import lombok.Getter;

@Getter
public class OAuth2Token {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String id_token;
    private Integer expires_in;
    private String scope;
    private Integer refresh_token_expires_in;
    private String error;
    private String error_description;
}