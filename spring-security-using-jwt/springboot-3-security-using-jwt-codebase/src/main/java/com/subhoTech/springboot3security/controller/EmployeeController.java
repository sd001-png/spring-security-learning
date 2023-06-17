package com.subhoTech.springboot3security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.subhoTech.springboot3security.dto.AuthRequest;
import com.subhoTech.springboot3security.entity.UserInfo;
import com.subhoTech.springboot3security.service.JwtService;
import com.subhoTech.springboot3security.service.UserInfoUserDetailsService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RestController
@RequestMapping("/emp")
public class EmployeeController {
	
	@Autowired
	private UserInfoUserDetailsService svc;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@GetMapping("/hi")
	public String getMsg() {
		return "hello";
	}
	
	@GetMapping("/bye")
	public String getMsg2() {
		return "bye";
	}
	
	@GetMapping("/welcome")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String getWelcome() {
		return "welcome";
	}
	
	@GetMapping("welcome/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String getEmpById(@PathVariable String id) {
		return "employee"+ id;
	}
	
	@PostMapping("/user")
	public String addUser(@RequestBody UserInfo user) {
		
		return svc.addUser(user);
	}
	
	@PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }


    }
	

}
