package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Item;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.request.ItemAddRequestDto;
import com.woong.projectmanager.dto.response.ItemResponseDto;
import com.woong.projectmanager.exception.AccessTokenFailedException;
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

    @Transactional
    public ItemResponseDto createItem(ItemAddRequestDto itemAddRequestDto, String writerEmail){

        Item item = itemAddRequestDto.toEntity();
        Users writer = usersRepository.findByEmail(writerEmail).orElseThrow();
        Channel channel = channelRepository.findById(itemAddRequestDto.getChannelId()).orElseThrow();

        //item 작성자 설정
        item.setWriter(writer);

        //item 추가
        channel.addItem(item);

        //
        itemRepository.save(item);

        return new ItemResponseDto(item);
    }

    @Transactional
    public ItemResponseDto updateItem(Long itemId,ItemAddRequestDto itemAddRequestDto, String requestEmail){

        Item item = itemRepository.findById(itemId).orElseThrow();

        if(!item.getWriter().getEmail().equals(requestEmail)){
            throw new AccessTokenFailedException(requestEmail);
        }

        item.update(itemAddRequestDto);

        itemRepository.save(item);

        ItemResponseDto itemResponseDto = new ItemResponseDto(item);

        return itemResponseDto;
    }

    @Transactional
    public ItemResponseDto removeItem(Long itemId,String requestEmail){

        Item item = itemRepository.findById(itemId).orElseThrow();

        if(!item.getWriter().getEmail().equals(requestEmail)){
            throw new AccessTokenFailedException(requestEmail);
        }

        itemRepository.delete(item);

        return new ItemResponseDto(item);
    }

    public List<ItemResponseDto> getItemList(Long channelId){

        Channel channel = channelRepository.findById(channelId).orElseThrow();

        //DTO 생성하여 반환
        List<ItemResponseDto> itemDtoList = new ArrayList<>();

        for(var el : channel.getItemList()){
            ItemResponseDto itemResponseDto = new ItemResponseDto(el);
            itemDtoList.add(itemResponseDto);
        }

        return itemDtoList;

    }

    public List<ItemResponseDto> getItemList(Channel channel){

        //DTO 생성하여 반환
        List<ItemResponseDto> itemDtoList = new ArrayList<>();

        for(var el : channel.getItemList()){
            ItemResponseDto itemResponseDto = new ItemResponseDto(el);
            itemDtoList.add(itemResponseDto);
        }

        return itemDtoList;

    }
}
