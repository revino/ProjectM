package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Item;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.ChannelDto;
import com.woong.projectmanager.dto.ItemDto;
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
    public Item createItem(ItemDto itemDto){

        Item item = itemDto.toEntity();
        Users writer = usersRepository.findByEmail(itemDto.getWriterEmail()).orElseThrow();
        Channel channel = channelRepository.findById(itemDto.getChannelId()).orElseThrow();

        //item 작성자 설정
        item.setWriter(writer);

        //item 추가
        channel.addItem(item);

        return itemRepository.save(item);

    }

    @Transactional
    public void removeItem(Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow();

        itemRepository.delete(item);
    }

    public List<ItemDto> getItemList(Long channelId){

        Channel channel = channelRepository.findById(channelId).orElseThrow();

        //DTO 생성하여 반환
        List<ItemDto> itemDtoList = new ArrayList<>();

        for(var el : channel.getItemList()){
            ItemDto itemDto = new ItemDto();

            itemDto.setName(el.getName());
            itemDto.setStatus(el.getStatus());
            itemDto.setStartDate(el.getStartDate());
            itemDto.setEndDate(el.getEndDate());
            itemDto.setWriterEmail(el.getWriter().getEmail());
            itemDto.setChannelId(el.getChannel().getId());

            itemDtoList.add(itemDto);
        }

        return itemDtoList;

    }
}
