package com.test1017.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.test1017.dao.CommentDao;
import com.test1017.entity.Comment;
import com.test1017.service.CommentService;
import com.test1017.service.EntityCallback;

@Service
@Qualifier("commentServiceCallbcak")
public class CommentCallbackimpl implements EntityCallback<Comment>{

	@Autowired
	@Qualifier("commentDao")
	CommentDao commentDao;



	@Override
	public void post(Comment entity) {
		// TODO Auto-generated method stub
		commentDao.save(entity);
	}

	@Override
	public Comment findById(Long id) {
		// TODO Auto-generated method stub
		return commentDao.findById(id);
	}

	@Override
	public List<Comment> findAll() {
		// TODO Auto-generated method stub
		return commentDao.findAll();
	}

	@Override
	public void update(Comment entity, String title, String text) {
		// TODO Auto-generated method stub
		commentDao.update(entity, text);
	}

	@Override
	public void delete(Comment entity) {
		// TODO Auto-generated method stub
		commentDao.delete(entity);
	}
	
}
