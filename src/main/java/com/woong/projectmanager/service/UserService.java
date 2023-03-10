package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.UserRefreshToken;
import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.UserChannel;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.request.UserSettingRequestDto;
import com.woong.projectmanager.dto.response.ChannelResponseDto;
import com.woong.projectmanager.dto.request.UserSignUpRequestDto;
import com.woong.projectmanager.dto.request.UserSignInRequestDto;
import com.woong.projectmanager.dto.response.UserResponseDto;
import com.woong.projectmanager.exception.*;
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
import java.util.List;
import java.util.stream.Collectors;

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
    public UserResponseDto signUp(UserSignUpRequestDto userDto){

        String password = passwordEncoder.encode(userDto.getPassword());

        userDto.setPassword(password);

        if(usersRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new SignupFailedException("?????? ????????? ????????? ????????????.");
        }

        Users users = usersRepository.save(userDto.toEntity());

        return new UserResponseDto(users);
    }

    public String signIn(UserSignInRequestDto userSignInRequestDto){

        Users users = usersRepository.findByEmail(userSignInRequestDto.getEmail()).orElseThrow(()-> new EmailSignInFailedException("?????? ?????? ?????? ???????????????."));

        //???????????? ??????
        if(!passwordEncoder.matches(userSignInRequestDto.getPassword(), users.getPassword())){
            throw new PasswordSignInFailedException();
        }

        return getAccessToken(users);
    }

    @Transactional
    public List<ChannelResponseDto> subscribeChannel(String email, Long channelId){
        Users member = usersRepository.findByEmailWithChannelList(email).orElseThrow(()->new UserFindFailedException("???????????? ?????? ???????????????."));
        Channel channel = channelRepository.findById(channelId).orElseThrow(()->new ChannelFindFailedException("???????????? ?????? ?????? ?????????."));

        //?????? ?????? ??????
        if(member.getChannelList().stream().anyMatch(e-> e.getChannel().getId().equals(channel.getId()))){
            throw new ChannelSubscribeFailedException("?????? ?????? ?????? ???????????????.");
        }

        //???????????? ??????
        UserChannel userChannel = UserChannel.createUserChannel(member, channel);

        //?????? ??????
        member.addChannel(userChannel);

        return getChannelList(member);
    }

    @Transactional
    public List<ChannelResponseDto> unSubscribeChannel(String email, Long channelId){
        Users member = usersRepository.findByEmailWithChannelList(email).orElseThrow(()->new UserFindFailedException("???????????? ?????? ???????????????."));

        //???????????? ??????
        UserChannel userChannel = member
                .getChannelList().stream()
                .filter(e-> e.getChannel().getId().equals(channelId))
                .findAny().orElseThrow(() -> new ChannelSubscribeFailedException("?????? ??? ?????? ???????????????."));

        //?????? ??????
        member.removeChannel(userChannel);
        usersRepository.save(member);

        return getChannelList(member);
    }

    @Transactional
    public void subscribeChannelWithoutCheck(Users user, Channel channel){

        //???????????? ??????
        UserChannel userChannel = UserChannel.createUserChannel(user, channel);

        //?????? ??????
        user.addChannel(userChannel);

        return;
    }

    @Transactional
    public ChannelResponseDto setCurrentChannel(String email, Long channelId){
        Users member = usersRepository.findByEmailWithChannel(email).orElseThrow(()->new UserFindFailedException("???????????? ?????? ???????????????."));
        Channel channel = channelRepository.findByIdWithManager(channelId).orElseThrow(()->new ChannelFindFailedException("???????????? ?????? ?????? ?????????."));

        //?????? ?????? ??????
        if(userChannelRepository.findByUserAndChannel(member, channel).isEmpty()){
            throw new ChannelSubscribeFailedException("?????? ???????????? ???????????????.");
        }

        //?????? ??????
        member.setCurrentChannel(channel);

        return new ChannelResponseDto(channel);
    }

    public UserResponseDto getUserInfo(String email){
        Users user = usersRepository.findByEmailWithAllChannel(email).orElseThrow(()->new UserFindFailedException("???????????? ?????? ???????????????."));

        return new UserResponseDto(user);
    }

    public List<ChannelResponseDto> getChannelList(String email){
        Users user = usersRepository.findByEmailWithChannelList(email).orElseThrow(()->new UserFindFailedException("???????????? ?????? ???????????????."));

        //Dto ???????????? ??????
        List<ChannelResponseDto> channelDtoList = user
                                                .getChannelList().stream()
                                                .map(el -> new ChannelResponseDto(el.getChannel()))
                                                .collect(Collectors.toList());

        return channelDtoList;
    }

    public List<ChannelResponseDto> getChannelList(Users user){

        //Dto ???????????? ??????
        List<ChannelResponseDto> channelDtoList = user
                .getChannelList().stream()
                .map(el -> new ChannelResponseDto(el.getChannel()))
                .collect(Collectors.toList());

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
        UserRefreshToken userRefreshToken = accountRefreshTokenRepository.findByUserId(id).orElse(null);

        if(userRefreshToken == null){
            userRefreshToken = accountRefreshTokenRepository.save(
                    UserRefreshToken.builder()
                            .userId(id)
                            .refreshToken(refreshToken).build());
        }
        else {
            userRefreshToken.setRefreshToken(refreshToken);
        }

        int cookieMaxAge = (int) (appProperties.getAuth().getRefreshTokenExpiry() / 1000 / 60); //minute
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken, cookieMaxAge);
    }

    @Transactional
    public String getUserEmail(HttpServletRequest request){

        String accessToken = jwtTokenProvider.resolveToken(request);

        //Access Token ????????? ??????
        if(accessToken.isEmpty()){
            throw new AccessTokenFailedException();
        }

        String email = jwtTokenProvider.getUserEmail(accessToken);

        if(email.isEmpty()){
            throw new AccessTokenFailedException();
        }

        return email;
    }

    @Transactional
    public UserResponseDto getUserEmail(String email){

        Users users = usersRepository.findByEmail(email).orElseThrow(()->new UserFindFailedException("???????????? ?????? ???????????????."));

        UserResponseDto userResponseDto = new UserResponseDto(users);

        return userResponseDto;
    }

    @Transactional
    public UserResponseDto setUserSettings(String email, UserSettingRequestDto userSettingRequestDto){

        Users user = usersRepository.findByEmail(email).orElseThrow(()->new UserFindFailedException("???????????? ?????? ???????????????."));

        //?????? ??????
        user.changeSetting(userSettingRequestDto);

        //Dto ???????????? ??????
        UserResponseDto userResponseDto = new UserResponseDto(user);

        return userResponseDto;
    }
}
