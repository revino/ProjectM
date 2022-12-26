package com.woong.projectmanager.service;

import com.woong.projectmanager.domain.Contents;
import com.woong.projectmanager.domain.Item;
import com.woong.projectmanager.domain.Users;
import com.woong.projectmanager.dto.request.ContentsAddRequestDto;
import com.woong.projectmanager.dto.response.ContentsResponseDto;
import com.woong.projectmanager.exception.ItemFindFailedException;
import com.woong.projectmanager.exception.UserFindFailedException;
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
    public ContentsResponseDto createContents(ContentsAddRequestDto contentsDto, String requestEmail){

        //작성자 및 아이템 조회
        Users writer = usersRepository.findByEmail(requestEmail).orElseThrow(() -> new UserFindFailedException("일치하는 유저 정보가 없습니다."));
        Item item = itemRepository.findById(contentsDto.getItemId()).orElseThrow(()-> new ItemFindFailedException("일치하는 아이템이 없습니다."));

        //컨텐츠 생성
        Contents contents = Contents.createContents(writer, item, contentsDto.getContents());

        //컨첸트 저장
        contentsRepository.save(contents);

        return new ContentsResponseDto(contents);
    }

    public List<ContentsResponseDto> getContentsList(Long itemId){

        Item item = itemRepository.findById(itemId).orElseThrow(()-> new ItemFindFailedException("일치하는 아이템이 없습니다."));
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        var contentsList = contentsRepository.findAllByItem(item, sort).orElseThrow();

        //DTO 생성하여 반환
        List<ContentsResponseDto> contentsDtoList = new ArrayList<>();

        for(var el : contentsList){
            ContentsResponseDto contentsDto = new ContentsResponseDto(el);
            contentsDtoList.add(contentsDto);
        }

        return contentsDtoList;

    }
}
