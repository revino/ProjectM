package com.woong.projectmanager.repository;

import com.woong.projectmanager.domain.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserChannelRepository extends JpaRepository<UserChannel, Long> {
    Optional<UserChannel> findByUserAndChannel(Users users, Channel channel);

}
