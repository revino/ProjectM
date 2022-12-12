package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.UserPrincipal;
import com.woong.projectmanager.exception.EmailSignInFailedException;
import com.woong.projectmanager.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Users users = usersRepository.findByEmail(userEmail).orElseThrow(EmailSignInFailedException::new);

        return UserPrincipal.create(users);
    }
}
