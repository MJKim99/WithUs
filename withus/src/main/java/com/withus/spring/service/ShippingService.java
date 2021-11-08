package com.withus.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.withus.spring.domain.FundedDTO;
import com.withus.spring.domain.UserShpDAO;
import com.withus.spring.domain.UserShpDTO;

@Service
public class ShippingService {

	UserShpDAO shpdao;

	@Autowired
	public void setDao(UserShpDAO dao) {
		this.shpdao = dao;
	}

	public ShippingService() {
	}

	// 글 목록 조회.
	public List<UserShpDTO> selectByShp() {
		return shpdao.selectByShp();
	}

	// dvnum
	public UserShpDTO dvnumByShp(String prnum) {
		return shpdao.dvnumByShp(prnum);
	}
}
