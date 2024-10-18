package com.test1017.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.test1017.dao.Postdao;
import com.test1017.entity.Post;
import com.test1017.service.DefaultTextFilter;
import com.test1017.service.EntityCallback;
import com.test1017.service.PostService;



@Service
@Transactional //boolean readOnly() default false;
@Qualifier("postService")
public class PostServiceImpl implements PostService {
    @Inject 
    private DefaultTextFilter textFilter;
    
    @Autowired
    private Postdao postDao;


    public DefaultTextFilter getTextFilter() { return textFilter; }


	@Override
	public void post(Post post, EntityCallback<Post> callback) {
		callback.post(post);
		
	}


	@Override
	public Post findById(Long id, EntityCallback<Post> callback) {
		return callback.findById(id);
	}


	@Override
	public List<Post> findAll(EntityCallback<Post> callback) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void update(Post entity, EntityCallback<Post> callback,String title, String text) {
		// TODO Auto-generated method stub
		callback.update(entity,title,text);
	}


	@Override
	public void delete(Post entity, EntityCallback<Post> callback) {
		// TODO Auto-generated method stub
		callback.delete(entity);
	}


	@Override
	public List<Post> findPostsByPage(int page, int pageSize) {
		return postDao.findPostsByPage(page, pageSize);
	}


	@Override
	public long countPosts() {
		// TODO Auto-generated method stub
		if(postDao.findAll() != null)
		{
			return postDao.findAll().size();
		}
		
		return 0;
	}


	@Override
	public List<Post> searchPostsByName(String name, int page, int pageSize) {
		List<Post> posts = postDao.searchPostsByName(name, page, pageSize);
		return posts;
	}


	@Override
	public long countPostsByName(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

    
    
}