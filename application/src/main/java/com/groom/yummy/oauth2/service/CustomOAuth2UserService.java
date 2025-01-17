package com.groom.yummy.oauth2.service;

import com.groom.yummy.facade.UserFacade;
import com.groom.yummy.oauth2.auth.LoginUser;
import com.groom.yummy.oauth2.dto.OAuth2Response;
import com.groom.yummy.oauth2.strategy.OAuth2ResFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.groom.yummy.user.User;

import java.util.Optional;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuth2ResFactory oAuth2ResFactory;
    private final UserFacade userFacade;

    public CustomOAuth2UserService(OAuth2ResFactory oAuth2ResFactory, UserFacade userFacade) {
        this.oAuth2ResFactory = oAuth2ResFactory;
        this.userFacade = userFacade;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = oAuth2ResFactory.createOAuth2Response(registrationId, oAuth2User.getAttributes());
        String email = oAuth2Response.getEmail();
        String nickname = oAuth2Response.getName();
        Optional<User> optionalUser = userFacade.findAuthUserByEmail(email);
        User user = userFacade.findOrCreateUser(optionalUser, nickname, email);
        log.info("loadUser : {}", user.getEmail());
        return new LoginUser(user, oAuth2User.getAttributes());
    }
}
