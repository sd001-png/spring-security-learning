package com.subhoTech.springboot3security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.subhoTech.springboot3security.config.UserInfoUserDetails;
import com.subhoTech.springboot3security.entity.UserInfo;
import com.subhoTech.springboot3security.repository.UserInfoRepository;


@Component
public class UserInfoUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserInfoRepository repo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<UserInfo> userInfo= repo.findByName(username);
		
		return userInfo.map(UserInfoUserDetails:: new )
		       .orElseThrow(()->new UsernameNotFoundException("User Not Found "+username));
		
		
		
	}
	
	public String addUser(UserInfo user) {
		user.setPassword(encoder.encode(user.getPassword()));
		repo.save(user);
		return "user added to db";
	}
	
	

}

