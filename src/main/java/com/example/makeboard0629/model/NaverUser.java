package com.example.makeboard0629.model;

import lombok.Data;

@Data
public class NaverUser {
    public String resultcode;
    public String message;
    public Response response;

    @Data
    public class Response{
        public String id;
        public String nickname;
        public String name;
        public String email;
        public String gender;
        public String age;
        public String birthday;
        public String profile_image;
        public String birthyear;
        public String mobile;
    }
}
