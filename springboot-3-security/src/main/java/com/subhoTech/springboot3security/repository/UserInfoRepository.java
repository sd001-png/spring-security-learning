package com.subhoTech.springboot3security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.subhoTech.springboot3security.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer>{

	Optional<UserInfo> findByName(String username);

}
