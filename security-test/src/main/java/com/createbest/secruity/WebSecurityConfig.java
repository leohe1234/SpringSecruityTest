package com.createbest.secruity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.createbest.secruity.filter.JwtTokenFilter;
import com.createbest.secruity.service.JwtUserDetailService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	JwtUserDetailService jwtUserDetailService;

	@Autowired
	public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder
				// 设置UserDetailsService
				.userDetailsService(jwtUserDetailService)
				// 使用BCrypt进行密码的hash
				.passwordEncoder(passwordEncoder());
	}

	// 装载BCrypt密码编码器
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtUserDetailService);
	}


	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity
		.authorizeRequests()
        .antMatchers("/auth").authenticated()       // 需携带有效 token
        .antMatchers("/admin").hasAuthority("admin")   // 需拥有 admin 这个权限
        .antMatchers("/ADMIN").hasRole("ADMIN")     // 需拥有 ADMIN 这个身份
        .antMatchers("/register").permitAll()
        .antMatchers("/login").permitAll()
        .anyRequest().authenticated()       // 允许所有请求通过
        .and()
        .csrf()
        .disable()                      // 禁用 Spring Security 自带的跨域处理
        .sessionManagement()                        // 定制我们自己的 session 策略
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 调整为让 Spring Security 不创建和使用 session

		httpSecurity.addFilterBefore(authenticationTokenFilterBean(),JwtTokenFilter.class);
	}


	@Bean
	public JwtTokenFilter authenticationTokenFilterBean() throws Exception {
		JwtTokenFilter authenticationTokenFilter = new JwtTokenFilter(authenticationManagerBean());
		authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
		return authenticationTokenFilter;
	}
	

}
