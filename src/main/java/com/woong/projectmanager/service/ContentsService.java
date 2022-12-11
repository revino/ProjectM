package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Contents;
import com.woong.projectmanager.domain.Item;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.ContentsDto;
import com.woong.projectmanager.dto.ItemDto;
import com.woong.projectmanager.repository.ChannelRepository;
import com.woong.projectmanager.repository.ContentsRepository;
import com.woong.projectmanager.repository.ItemRepository;
import com.woong.projectmanager.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ContentsService {

    private final ContentsRepository contentsRepository;

    private final UsersRepository usersRepository;

    private final ChannelRepository channelRepository;

    private final ItemRepository itemRepository;

    @Transactional
    public Contents createContents(ContentsDto contentsDto){

        //작성자 및 아이템 조회
        Users writer = usersRepository.findByEmail(contentsDto.getWriterEmail()).orElseThrow();
        Item item = itemRepository.findById(contentsDto.getItemId()).orElseThrow();

        //컨텐츠 생성
        Contents contents = Contents.createContents(writer, item, contentsDto.getContents());

        return contentsRepository.save(contents);
    }

    public List<ContentsDto> getContentsList(Long itemId){

        Item item = itemRepository.findById(itemId).orElseThrow();
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        var contentsList = contentsRepository.findAllByItem(item, sort).orElseThrow();

        //DTO 생성하여 반환
        List<ContentsDto> contentsDtoList = new ArrayList<>();

        for(var el : contentsList){
            ContentsDto contentsDto = new ContentsDto();

            contentsDto.setContents(el.getContents());
            contentsDto.setWriterEmail(el.getWriter().getEmail());
            contentsDto.setItemId(el.getId());

            contentsDtoList.add(contentsDto);
        }

        return contentsDtoList;

    }
}
