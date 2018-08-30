package com.createbest.secruity.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.createbest.secruity.bean.BaseReq;
import com.createbest.secruity.dao.UserRepository;
import com.createbest.secruity.entity.AppUser;
import com.createbest.secruity.utils.JwtTokenUtils;


@RestController
public class MainControler {
	@Autowired
	UserRepository  userRepository;

	@Autowired
	JwtTokenUtils tokenUtils;
	
	@PostMapping("/login")
	@ResponseBody
	public BaseReq login(@RequestBody AppUser req)
	{
		
		AppUser u = userRepository.findByUsername(req.getUsername());
		BaseReq rsp = new BaseReq();
		String token = tokenUtils.generateToken(req.getUsername());
		rsp.setToken(token);
		return rsp;
	}
	
	@PostMapping("/register")
	@ResponseBody
	public AppUser register(@RequestBody AppUser req)
	{
		AppUser u = userRepository.findByUsername(req.getUsername());
		if(u == null)
			u = userRepository.save(req);
		else
			u = userRepository.save(u);
		
		String token = tokenUtils.generateToken(req.getUsername());
		System.out.println("token : "+ token);
		return u;
	}
	
	@PostMapping("/query_test")
	@ResponseBody
	public AppUser query(@RequestBody AppUser req)
	{
		AppUser u = userRepository.findByUsername(req.getUsername());
		return u;
	}
}
	
	
