package com.test1017.dao;

import java.util.List;

import com.test1017.entity.Post;

public interface Postdao {
	void save(Post post);
	Post findById(Long id);
	List<Post> findAll();
	void delete(Post post);
	void update(Post entity,String title, String text);
	List<Post> searchPostsByName(String name, int page, int pageSize);
	long countPostsByName(String name);
	public List<Post> findPostsByPage(int page, int pageSize);
}
