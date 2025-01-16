package com.groom.yummy.oauth2.service;

import com.groom.yummy.oauth2.dto.OAuth2Response;
import com.groom.yummy.oauth2.strategy.OAuth2ResFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.groom.yummy.user.User;
import com.groom.yummy.user.UserAuthService;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuth2ResFactory oAuth2ResFactory;
    private final UserAuthService userAuthService;

    public CustomOAuth2UserService(OAuth2ResFactory oAuth2ResFactory, UserAuthService userAuthService) {
        this.oAuth2ResFactory = oAuth2ResFactory;
        this.userAuthService = userAuthService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = oAuth2ResFactory.createOAuth2Response(registrationId, oAuth2User.getAttributes());
        String email = oAuth2Response.getEmail();

        Optional<User> user = userAuthService.findAuthUserByEmail(email);

        return null;
    }
}
