package com.woong.projectmanager.repository;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Optional<Channel> findByName(String name);
}
