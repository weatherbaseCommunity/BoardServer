package com.example.makeboard0629.model;

import lombok.Data;

@Data
public class GoogleUser {
    public String azp;
    public String aud;
    public String sub;
    public String scope;
    public String exp;
    public String expires_in;
    public String email;
    public String email_verified;
    public String access_type;
}
