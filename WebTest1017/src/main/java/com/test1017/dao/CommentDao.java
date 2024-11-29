package com.test1017.dao;

import java.util.List;

import com.test1017.entity.Comment;
import com.test1017.entity.Post;

public interface CommentDao {
	void save(Comment post);
	Comment findById(Long id);
	List<Comment> findAll();
	void delete(Comment post);
	void update(Comment entity, String text);
	List<Comment> findByPostid(Long postid);
}
