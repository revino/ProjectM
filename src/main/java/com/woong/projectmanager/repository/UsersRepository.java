package com.woong.projectmanager.repository;

import com.woong.projectmanager.domain.Channel;
import com.woong.projectmanager.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    @Query("select u from Users u left join fetch u.channelList where u.email = :email ")
    Optional<Users> findByEmailWithChannelList(@Param(value = "email")String email);

    @Query("select distinct u from Users u left join fetch u.currentChannel where u.email = :email")
    Optional<Users> findByEmailWithChannel(@Param(value = "email")String email);

    @Query("select u from Users u left join fetch u.channelList left join fetch u.currentChannel where u.email = :email")
    Optional<Users> findByEmailWithAllChannel(@Param(value = "email")String email);

}
