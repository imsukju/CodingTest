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
@Qualifier("commentService")
public class CommentServiceImpl implements CommentService{

	@Autowired
	@Qualifier("commentDao")
	CommentDao commentDao;
	
	@Override
	public void post(Comment entity, EntityCallback<Comment> callback) {
		callback.post(entity);
		
	}

	@Override
	public Comment findById(Long id, EntityCallback<Comment> callback) {
		// TODO Auto-generated method stub
		return callback.findById(id);
	}

	@Override
	public List<Comment> findAll(EntityCallback<Comment> callback) {
		// TODO Auto-generated method stub
		return callback.findAll();
	}

	@Override
	public void update(Comment entity, EntityCallback<Comment> callback, String title, String text) {
		// TODO Auto-generated method stub
		callback.update(entity, title, text);
		
	}

	@Override
	public void delete(Comment entity, EntityCallback<Comment> callback) {
		// TODO Auto-generated method stub
		callback.delete(entity);
	}

	@Override
	public List<Comment> findByPostid(Long postid) {
		// TODO Auto-generated method stub
		return commentDao.findByPostid(postid);
	}

}
