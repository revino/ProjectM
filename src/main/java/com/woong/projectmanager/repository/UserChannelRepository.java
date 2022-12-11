package com.woong.projectmanager.repository;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.UserChannel;
import com.woong.projectmanager.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserChannelRepository extends JpaRepository<UserChannel, Long> {
    Optional<UserChannel> findByUserAndChannel(Users users, Channel channel);
}
