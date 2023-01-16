package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.*;
import com.woong.projectmanager.dto.request.ItemAddRequestDto;
import com.woong.projectmanager.dto.response.ChannelResponseDto;
import com.woong.projectmanager.dto.response.ItemResponseDto;
import com.woong.projectmanager.exception.*;
import com.woong.projectmanager.repository.AlarmUserItemRepository;
import com.woong.projectmanager.repository.ChannelRepository;
import com.woong.projectmanager.repository.ItemRepository;
import com.woong.projectmanager.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    private final UsersRepository usersRepository;

    private final ChannelRepository channelRepository;

    private final AlarmUserItemRepository alarmUserItemRepository;

    @Transactional
    public ItemResponseDto createItem(ItemAddRequestDto itemAddRequestDto, String writerEmail){

        Item item = itemAddRequestDto.toEntity();
        Users writer = usersRepository.findByEmail(writerEmail).orElseThrow(()->new UserFindFailedException("존재하지 않는 유저입니다."));
        Channel channel = channelRepository.findByIdWithAllItem(itemAddRequestDto.getChannelId()).orElseThrow(()->new ChannelFindFailedException("존재하지 않는 채널 입니다."));

        //item 작성자 설정
        item.setWriter(writer);

        //item 추가
        channel.addItem(item);

        //
        itemRepository.save(item);

        return new ItemResponseDto(item);
    }

    @Transactional
    public ItemResponseDto updateItem(Long itemId, ItemAddRequestDto itemAddRequestDto, String requestEmail){

        Item item = itemRepository.findByIdWithWriterAndChannel(itemId).orElseThrow(()-> new ItemFindFailedException("일치하는 아이템이 없습니다."));
        String managerEmail = item.getWriter().getEmail();

        if(!managerEmail.equals(requestEmail)){
            throw new AuthenticationFailedException("수정 권한이 없습니다. " + managerEmail);
        }

        item.update(itemAddRequestDto);

        itemRepository.save(item);

        ItemResponseDto itemResponseDto = new ItemResponseDto(item);

        return itemResponseDto;
    }

    @Transactional
    public ItemResponseDto removeItem(Long itemId, String requestEmail){

        Item item = itemRepository.findByIdWithAll(itemId).orElseThrow(()-> new ItemFindFailedException("일치하는 아이템이 없습니다."));
        String managerEmail = item.getWriter().getEmail();

        if(!managerEmail.equals(requestEmail)){
            throw new AuthenticationFailedException("수정 권한이 없습니다. " + managerEmail);
        }

        itemRepository.delete(item);

        return new ItemResponseDto(item);
    }

    public List<ItemResponseDto> getItemList(Long channelId){

        Channel channel = channelRepository.findByIdWithAllItem(channelId).orElseThrow(()->new ChannelFindFailedException("존재하지 않는 채널 입니다."));

        //DTO 생성하여 반환
        List<ItemResponseDto> itemDtoList = new ArrayList<>();

        for(var el : channel.getItemList()){
            ItemResponseDto itemResponseDto = new ItemResponseDto(el);
            itemDtoList.add(itemResponseDto);
        }

        return itemDtoList;

    }

    public ItemResponseDto getItem(Long itemId){

        Item item = itemRepository.findByIdWithWriterAndChannel(itemId).orElseThrow(()-> new ItemFindFailedException("일치하는 아이템이 없습니다."));

        return new ItemResponseDto(item);

    }

    @Transactional
    public ItemResponseDto addAlarmUser(String email, Long itemId){
        Users member = usersRepository.findByEmail(email).orElseThrow(()->new UserFindFailedException("존재하지 않는 유저입니다."));
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new ItemFindFailedException("일치하는 아이템이 없습니다."));

        //중복 구독 체크
        if(!checkSubscribeAlarm(member, item)){
            throw new ChannelSubscribeFailedException("이미 알람 설정 중인 아이템입니다.");
        }

        //유저채널 생성
        AlarmUserItem alarmUserItem = AlarmUserItem.createAlarmUserItem(member, item);

        //채널 추가
        member.addAlarmItem(alarmUserItem);

        ItemResponseDto itemResponseDto = new ItemResponseDto(item);

        return itemResponseDto;
    }

    @Transactional
    public ItemResponseDto removeAlarmUser(String email, Long itemId){
        Users member = usersRepository.findByEmail(email).orElseThrow(()->new UserFindFailedException("존재하지 않는 유저입니다."));
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new ItemFindFailedException("일치하는 아이템이 없습니다."));

        //유저아이템 찾기
        AlarmUserItem alarmUserItem = alarmUserItemRepository.findByUserAndItem(member, item)
                .orElseThrow(() -> new ChannelSubscribeFailedException("찾을 수 없는 아이템입니다."));

        //채널 제거
        member.removeAlarmItem(alarmUserItem);
        usersRepository.save(member);

        return new ItemResponseDto(item);
    }

    private boolean checkSubscribeAlarm(Users user, Item item){

        return alarmUserItemRepository.findByUserAndItem(user, item).isEmpty();

    }
}
