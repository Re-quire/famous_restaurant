package com.groom.yummy.oauth2.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.groom.yummy.user.User;

import java.util.Collection;
import java.util.Map;

@Getter
public class LoginUser implements UserDetails, OAuth2User{
    private final User user;
    private Map<String, Object> attributes;

    public LoginUser(User user){
        this.user = user;
    }
    public LoginUser(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getName() {
        return user.getNickname();
    }

    public String getRole(){
        return user.getRole();
    }

    public Long getUserId(){return user.getId();}

    @Override
    public String getPassword() {
        return null;
    }
}