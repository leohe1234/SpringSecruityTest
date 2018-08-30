package com.createbest.secruity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.createbest.secruity.dao.TestRepository;
import com.createbest.secruity.entity.TestB;
import com.createbest.secruity.utils.JwtTokenUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityTestApplicationTests {

	@Autowired
	private JwtTokenUtils jwtTokenUtils;
	
	@Autowired
	private TestRepository testRepository;
	@Test
	public void contextLoads() {
		

	}
	
	
	@Test
	public void testUuid()
	{
		TestB entity  = new TestB();
		entity.setName("test"+System.currentTimeMillis());
		testRepository.save(entity);
	}

	@Test
	public void testToken()
	{

		String token = jwtTokenUtils.generateToken("heminlin");
		System.out.println("token:" + token);
		
		System.out.println("name from token " + jwtTokenUtils.getUsernameFromToken(token));
		
		System.out.println("name from token 2" + jwtTokenUtils.getUsernameFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoZW1pbmxpbiIsImNyZWF0ZWQiOjE1MzU1MzYyMzUxMTgsImV4cCI6MTUzNjE0MTAzNX0.VuWvry5iOe4jHFHhrT_a_Z49yHRxL73Gzf4zNslnJhEkloLcFPeuBsEYhIAveL9I5Am-Y9GXqIIw3q6vH_tMNK"));
	}
}
