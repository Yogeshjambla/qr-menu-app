package com.food.DAO;

import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.food.entity.Admin;
import com.food.repository.AdminRepository;

@Component
public class AdminDAOImpl implements AdminDAO
{
	@Autowired
AdminRepository adminRepository;
	@Override
	public Admin insertadmin(Admin admin) {
		return adminRepository.save(admin);
		
	}

}
