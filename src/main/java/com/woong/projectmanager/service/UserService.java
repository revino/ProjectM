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
            throw new SignupFailedException("이미 가입된 계정이 있습니다.");
        }

        Users users = usersRepository.save(userDto.toEntity());

        return new UserResponseDto(users);
    }

    public String signIn(UserSignInRequestDto userSignInRequestDto){

        Users users = usersRepository.findByEmail(userSignInRequestDto.getEmail()).orElseThrow(()-> new EmailSignInFailedException("존재 하지 않는 계정입니다."));

        //패스워드 확인
        if(!passwordEncoder.matches(userSignInRequestDto.getPassword(), users.getPassword())){
            throw new PasswordSignInFailedException();
        }

        return getAccessToken(users);
    }

    @Transactional
    public List<ChannelResponseDto> addChannel(String email, Long channelId){
        Users member = usersRepository.findByEmail(email).orElseThrow(()->new UserFindFailedException("존재하지 않는 유저입니다."));
        Channel channel = channelRepository.findById(channelId).orElseThrow(()->new ChannelFindFailedException("존재하지 않는 채널 입니다."));

        //중복 구독 체크
        if(!checkSubscribe(member, channel)){
            throw new ChannelSubscribeFailedException("이미 구독 중인 채널입니다.");
        }

        //유저채널 생성
        UserChannel userChannel = UserChannel.createUserChannel(member, channel);

        //채널 추가
        member.addChannel(userChannel);

        ChannelResponseDto channelResponseDto = new ChannelResponseDto(channel);

        return getChannelList(member);
    }

    @Transactional
    public List<ChannelResponseDto> addChannel(Users user, Channel channel){

        //유저채널 생성
        UserChannel userChannel = UserChannel.createUserChannel(user, channel);

        //중복 구독 체크
        if(!checkSubscribe(user, channel)){
            throw new ChannelSubscribeFailedException("이미 구독 중인 채널입니다.");
        }

        //채널 추가
        user.addChannel(userChannel);

        return getChannelList(user);
    }

    @Transactional
    public ChannelResponseDto setCurrentChannel(String email, Long channelId){
        Users member = usersRepository.findByEmail(email).orElseThrow(()->new UserFindFailedException("존재하지 않는 유저입니다."));
        Channel channel = channelRepository.findById(channelId).orElseThrow(()->new ChannelFindFailedException("존재하지 않는 채널 입니다."));

        //중복 구독 체크
        if(checkSubscribe(member, channel)){
            throw new ChannelSubscribeFailedException("변경 불가능한 채널입니다.");
        }

        //채널 설정
        member.setCurrentChannel(channel);

        return new ChannelResponseDto(channel);
    }

    @Transactional
    public List<ChannelResponseDto> removeChannel(String email, Long channelId){
        Users member = usersRepository.findByEmail(email).orElseThrow(()->new UserFindFailedException("존재하지 않는 유저입니다."));
        Channel channel = channelRepository.findById(channelId).orElseThrow(()->new ChannelFindFailedException("존재하지 않는 채널 입니다."));

        //유저채널 찾기
        UserChannel userChannel = userChannelRepository.findByUserAndChannel(member, channel)
                .orElseThrow(() -> new ChannelSubscribeFailedException("찾을 수 없는 채널입니다."));

        //채널 제거
        member.removeChannel(userChannel);
        usersRepository.save(member);

        return getChannelList(member);
    }

    public UserResponseDto findUserEmail(String email){
        Users user = usersRepository.findByEmail(email).orElseThrow(()->new UserFindFailedException("존재하지 않는 유저입니다."));

        return new UserResponseDto(user);
    }

    public List<ChannelResponseDto> getChannelList(String email){
        Users user = usersRepository.findByEmail(email).orElseThrow(()->new UserFindFailedException("존재하지 않는 유저입니다."));

        //Dto 생성하여 반환
        List<ChannelResponseDto> channelDtoList = user
                                                .getChannelList().stream()
                                                .map(el -> new ChannelResponseDto(el.getChannel())).collect(Collectors.toList());

        return channelDtoList;
    }

    public List<ChannelResponseDto> getChannelList(Users user){

        //Dto 생성하여 반환
        List<ChannelResponseDto> channelDtoList = user
                .getChannelList().stream()
                .map(el -> new ChannelResponseDto(el.getChannel())).collect(Collectors.toList());

        return channelDtoList;
    }

    private String getAccessToken(Users users){
        return jwtTokenProvider.createRefreshToken(
                users.getEmail(),
                users.getRole(),
                appProperties.getAuth().getTokenExpiry());
    }

    private boolean checkSubscribe(Users user, Channel channel){

        return userChannelRepository.findByUserAndChannel(user, channel).isEmpty();

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

        //Access Token 없으면 반환
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

        Users users = usersRepository.findByEmail(email).orElseThrow(()->new UserFindFailedException("존재하지 않는 유저입니다."));

        UserResponseDto userResponseDto = new UserResponseDto(users);

        return userResponseDto;
    }

    @Transactional
    public UserResponseDto setUserSettings(String email, UserSettingRequestDto userSettingRequestDto){

        Users user = usersRepository.findByEmail(email).orElseThrow(()->new UserFindFailedException("존재하지 않는 유저입니다."));

        //설정 변경
        user.changeSetting(userSettingRequestDto);

        //Dto 생성하여 반환
        UserResponseDto userResponseDto = new UserResponseDto(user);

        return userResponseDto;
    }
}
