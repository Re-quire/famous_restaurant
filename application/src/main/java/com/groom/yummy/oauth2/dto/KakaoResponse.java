package com.groom.yummy.oauth2.dto;


import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return ((Map)attributes.get("kakao_account")).get("email").toString();
    }

    @Override
    public String getName() {
        return ((Map)attributes.get("properties")).get("nickname").toString();
    }
}
