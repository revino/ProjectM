package com.woong.projectmanager.dto;

import com.woong.projectmanager.domain.RoleType;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.provider.LoginProviderType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private LoginProviderType loginProviderType;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, LoginProviderType loginProviderType){

        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.loginProviderType = loginProviderType;
    }

    public static OAuthAttributes of(LoginProviderType oauthProviderType, String userNameAttributeName, Map<String, Object> attributes){

        if(oauthProviderType == LoginProviderType.GOOGLE){
            return ofGoogle(userNameAttributeName, attributes);

        }

        return null;

    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .loginProviderType(LoginProviderType.GOOGLE)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Users toEntity(){
        return Users.builder()
                .nickName(name)
                .email(email)
                .picture(picture)
                .loginProviderType(loginProviderType)
                .role(RoleType.USER)
                .build();
    }
}
