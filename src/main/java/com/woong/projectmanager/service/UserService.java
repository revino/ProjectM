package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.UserRefreshToken;
import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.UserChannel;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.ChannelCreateRequestDto;
import com.woong.projectmanager.dto.ChannelResponseDto;
import com.woong.projectmanager.dto.UserSignUpRequestDto;
import com.woong.projectmanager.dto.UserSignInRequestDto;
import com.woong.projectmanager.exception.AccessTokenFailedException;
import com.woong.projectmanager.exception.EmailSignInFailedException;
import com.woong.projectmanager.exception.PasswordSignInFailedException;
import com.woong.projectmanager.properties.AppProperties;
import com.woong.projectmanager.provider.JwtTokenProvider;
import com.woong.projectmanager.repository.AccountRefreshTokenRepository;
import com.woong.projectmanager.repository.ChannelRepository;
import com.woong.projectmanager.repository.UserChannelRepository;
import com.woong.projectmanager.repository.UsersRepository;
import com.woong.projectmanager.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final ChannelRepository channelRepository;
    private final UserChannelRepository userChannelRepository;
    private final AccountRefreshTokenRepository accountRefreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;

    @Transactional
    public Users signUp(UserSignUpRequestDto userDto){

        String password = passwordEncoder.encode(userDto.getPassword());

        userDto.setPassword(password);

        return usersRepository.save(userDto.toEntity());
    }

    public String signIn(UserSignInRequestDto userSignInRequestDto){

        Users users = usersRepository.findByEmail(userSignInRequestDto.getEmail()).orElseThrow(EmailSignInFailedException::new);

        //패스워드 확인
        if(!passwordEncoder.matches(userSignInRequestDto.getPassword(), users.getPassword())){
            throw new PasswordSignInFailedException();
        }

        return getAccessToken(users);
    }

    @Transactional
    public void addChannel(String email, Long channelId){
        Users member = usersRepository.findByEmail(email).orElseThrow();
        Channel channel = channelRepository.findById(channelId).orElseThrow();

        //유저채널 생성
        UserChannel userChannel = UserChannel.createUserChannel(member, channel);

        //채널 추가
        member.addChannel(userChannel);
    }

    @Transactional
    public void removeChannel(String email, Long channelId){
        Users member = usersRepository.findByEmail(email).orElseThrow();
        Channel channel = channelRepository.findById(channelId).orElseThrow();

        //유저채널 찾기
        UserChannel userChannel = userChannelRepository.findByUserAndChannel(member, channel).orElseThrow();

        //채널 제거
        member.removeChannel(userChannel);
        usersRepository.save(member);
    }

    public Users findUserEmail(String email){
        Users user = usersRepository.findByEmail(email).orElseThrow();

        return user;
    }

    public List<ChannelResponseDto> getChannelList(String email){
        Users user = usersRepository.findByEmail(email).orElseThrow();

        //Dto 생성하여 반환
        List<ChannelResponseDto> channelDtoList = new ArrayList<>();

        for(var el : user.getChannelList()){
            ChannelResponseDto channelDto = new ChannelResponseDto();
            Channel channel = el.getChannel();
            channelDto.setManagerEmail(channel.getManager().getEmail());
            channelDto.setName(channel.getName());
            channelDtoList.add(channelDto);
        }

        return channelDtoList;
    }

    private String getAccessToken(Users users){
        return jwtTokenProvider.createRefreshToken(
                users.getEmail(),
                users.getRole(),
                appProperties.getAuth().getTokenExpiry());
    }

    @Transactional
    public void makeRefreshToken(String id, HttpServletRequest request, HttpServletResponse response){
        //Refresh token create
        String refreshToken = jwtTokenProvider.createRefreshToken(
                appProperties.getAuth().getRefreshTokenSecret(),
                appProperties.getAuth().getRefreshTokenExpiry());

        //Refresh token DB Save
        UserRefreshToken userRefreshToken = accountRefreshTokenRepository.findByUserId(id).
                orElse(accountRefreshTokenRepository.save(
                        UserRefreshToken.builder()
                                .userId(id)
                                .refreshToken(refreshToken).build()));

        userRefreshToken.setRefreshToken(refreshToken);

        int cookieMaxAge = (int) (appProperties.getAuth().getRefreshTokenExpiry() / 1000 / 60); //minute
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken, cookieMaxAge);
    }

    @Transactional
    public String gerUserEmail(HttpServletRequest request){

        String accessToken = jwtTokenProvider.resolveToken(request);

        //Access Token 없으면 반환
        if(accessToken.isEmpty()){
            throw new AccessTokenFailedException();
        }

        return jwtTokenProvider.getUserEmail(accessToken);
    }
}
