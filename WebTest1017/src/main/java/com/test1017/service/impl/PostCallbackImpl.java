package com.test1017.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.test1017.dao.Postdao;
import com.test1017.entity.Post;
import com.test1017.service.EntityCallback;




@Service
@Qualifier("postCallbackImpl")
public class PostCallbackImpl implements EntityCallback<Post>{

	@Autowired
	@Qualifier("postDao")
	private Postdao postDao;
	
	public void post(Post post) {
		Assert.notNull(post, "post can't be null");
		postDao.save(post);			
	}

	@Override
	public Post findById(Long id) {
		return postDao.findById(id);
	}

	@Override
	public List<Post> findAll() {
		return postDao.findAll();
	}	

	@Override
	public void delete(Post post) {
		postDao.delete(post);
	}

	@Override
	public void update(Post entity,String title, String text) {
		postDao.update(entity,title,text);
		
	}

}
