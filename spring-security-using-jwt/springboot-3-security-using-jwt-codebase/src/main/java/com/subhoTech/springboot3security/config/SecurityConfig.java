package com.subhoTech.springboot3security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.subhoTech.springboot3security.filter.JwtAuthFilter;
import com.subhoTech.springboot3security.service.UserInfoUserDetailsService;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import org.springframework.beans.factory.annotation.Autowired; 

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	 @Autowired
	 private JwtAuthFilter authFilter;
	
	//authentication
	@Bean
	public UserDetailsService userDetailsService() {
		
		/*UserDetails admin=User.withUsername("subho")
				.password(encoder.encode("pwd1"))
				.roles("ADMIN")
				.build();
		
		UserDetails user=User.withUsername("john")
				.password(encoder.encode("pwd2"))
				.roles("USER")
				.build();
		
		return new InMemoryUserDetailsManager(admin,user);*/
		
		return new UserInfoUserDetailsService();
		
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//post request working without login but h2-console not working
		/*return http.csrf().disable()
		           .authorizeHttpRequests()
		           .requestMatchers("/emp/hi","/emp/user").permitAll()
		           .and()
		           .authorizeHttpRequests().requestMatchers("/emp/welcome/**").authenticated()
		           .and().formLogin()
		           .and().build();*/
		
		
		//post request not working without login but h2-console  working
		/*return http
		        .authorizeHttpRequests()
		        .requestMatchers(HttpMethod.POST,"/emp/user/**").permitAll()
		        .requestMatchers(toH2Console()).permitAll()
		        .requestMatchers(HttpMethod.GET,"/emp/hi").permitAll()
		        .requestMatchers(HttpMethod.GET,"/emp/welcome/**").authenticated()
		        //.anyRequest().authenticated()
		        .and()
		        .formLogin()
		        .and()
		        .csrf().ignoringRequestMatchers(toH2Console())
		        .and()
		        .headers().frameOptions().sameOrigin()
		        .and()
		        .build();*/
		
		//both post and h2 console working
		 /*http.csrf().disable()
		           .authorizeHttpRequests()
		           .requestMatchers(HttpMethod.POST,"/emp/user").permitAll()
		           .requestMatchers(HttpMethod.GET,"/emp/hi").permitAll()
		           .requestMatchers(toH2Console()).permitAll()
		           .requestMatchers("/emp/welcome/**").authenticated();
		           //.and().formLogin()
		           //.and().build();
		 http.headers().frameOptions().disable();
		 
		 return http.build();*/
		
		//form based login everything working except jwt
		/*return http
		 .csrf().disable()
        .authorizeHttpRequests()
        .requestMatchers(HttpMethod.POST,"/emp/user").permitAll()
        .requestMatchers(HttpMethod.GET,"/emp/hi").permitAll()
        .requestMatchers(HttpMethod.POST,"/emp/authenticate").permitAll()
        .requestMatchers(toH2Console()).permitAll()
        .requestMatchers("/emp/welcome/**").authenticated()
        .and()
        .headers().frameOptions().disable()
        .and().formLogin()
        .and().build();
*/
		//for JWT , not able to view db
		return http
				 .csrf().disable()
		        .authorizeHttpRequests()
		        .requestMatchers(HttpMethod.POST,"/emp/user").permitAll()
		        .requestMatchers(HttpMethod.GET,"/emp/hi").permitAll()
		        .requestMatchers(HttpMethod.POST,"/emp/authenticate").permitAll()
		        .requestMatchers(toH2Console()).permitAll()
		        .requestMatchers("/emp/welcome/**").authenticated()
		        .and()
		        .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
		
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		 DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
		 daoAuthenticationProvider.setUserDetailsService(userDetailsService());
		 daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	        return config.getAuthenticationManager();
	}
	
	

}
