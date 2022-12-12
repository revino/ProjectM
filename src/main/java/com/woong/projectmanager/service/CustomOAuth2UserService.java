package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.OAuthAttributes;
import com.woong.projectmanager.dto.UserPrincipal;
import com.woong.projectmanager.provider.LoginProviderType;
import com.woong.projectmanager.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UsersRepository usersRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        //로그인 진행중인 서비스를 구분하는 코드
        LoginProviderType providerType = LoginProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        //OAuth2 로그인 진행시 키가 되는 필드값
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(providerType, userNameAttributeName, oAuth2User.getAttributes());

        Users users = saveOrUpdate(attributes);

        return UserPrincipal.create(users, oAuth2User.getAttributes());

    }

    private Users saveOrUpdate(OAuthAttributes attributes){
        Users users = usersRepository.findByEmail(attributes.getEmail())
                .orElse(attributes.toEntity());

        return usersRepository.save(users);
    }
}
