package com.woong.projectmanager.repository;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Optional<Channel> findByName(String name);

    @Query("select distinct c from Channel c join fetch c.manager where c.id = :id ")
    Optional<Channel> findByIdWithManager(@Param(value = "id") Long id);

    @Query("select distinct c from Channel c " +
            "left join fetch c.manager " +
            "left join fetch c.memberList m " +
            "left join fetch m.user " +
            "where c.id = :id ")
    Optional<Channel> findByIdWithAllMember(@Param(value = "id") Long id);

    @Query("select distinct c from Channel c " +
            "left join fetch c.manager " +
            "left join fetch c.itemList ui " +
            "left join fetch ui.channel " +
            "where c.id = :id ")
    Optional<Channel> findByIdWithAllItem(@Param(value = "id") Long id);

}
