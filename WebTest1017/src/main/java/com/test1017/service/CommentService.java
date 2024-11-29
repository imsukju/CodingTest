package com.test1017.service;

import java.util.List;

import com.test1017.entity.Comment;

public interface CommentService extends EntityService<Comment,EntityCallback<Comment>>{
	List<Comment> findByPostid(Long postid);


}
