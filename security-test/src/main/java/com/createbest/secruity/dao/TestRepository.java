package com.createbest.secruity.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.createbest.secruity.entity.AppUser;
import com.createbest.secruity.entity.TestB;

public interface TestRepository extends JpaRepository<TestB, Integer> {

	
	TestB findByUid(String id);
	
}
