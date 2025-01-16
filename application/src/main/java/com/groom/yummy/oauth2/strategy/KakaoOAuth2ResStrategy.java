package com.groom.yummy.oauth2.strategy;

import com.groom.yummy.oauth2.dto.KakaoResponse;
import com.groom.yummy.oauth2.dto.OAuth2Response;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KakaoOAuth2ResStrategy implements OAuth2ResStrategy{

    @Override
    public String getProviderName() {
        return "kakao";
    }

    @Override
    public OAuth2Response createOAuth2Response(Map<String, Object> attributes) {
        return new KakaoResponse(attributes);
    }
}
