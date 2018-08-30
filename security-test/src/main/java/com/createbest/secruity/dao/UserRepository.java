package com.createbest.secruity.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.createbest.secruity.entity.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Integer> {

	AppUser findByUsername(String name);
	AppUser findById(String id);
	
}
