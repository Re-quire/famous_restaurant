package com.groom.yummy.oauth2.strategy;

import com.groom.yummy.oauth2.dto.OAuth2Response;

import java.util.Map;

public interface OAuth2ResStrategy {
    String getProviderName();
    OAuth2Response createOAuth2Response(Map<String, Object> attributes);
}
