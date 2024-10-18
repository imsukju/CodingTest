package com.test1017.service;

import java.util.List;

import com.test1017.entity.Post;


public interface PostService extends EntityService<Post, EntityCallback<Post>> {
	
//	Post findById(Long postId);
	
	List<Post> findPostsByPage(int page, int pageSize); // 페이지별 게시글 조회
    long countPosts(); // 전체 게시글 개수 조회
    
    // 검색 기능 추가
    List<Post> searchPostsByName(String name, int page, int pageSize);

    long countPostsByName(String name);
}