package com.test1017.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.test1017.dto.CommentDTO;
import com.test1017.dto.PostDTO;
import com.test1017.dto.UserDTO;
import com.test1017.entity.Comment;
import com.test1017.entity.Post;
import com.test1017.entity.User;

public class PostDTOConverter {
    // Comment 엔티티 -> CommentDTO로 변환
    public static PostDTO toDto(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setPostid(post.getPostid());
        postDTO.setTitle(post.getTitle());
        postDTO.setText(post.getText());

        List<CommentDTO> c1 = post.getCommentList().stream()
        		.map(comment -> new CommentDTO(comment.getCommentid(),comment.getText(),comment.getPost().getPostid())).collect(Collectors.toList());
       
        postDTO.setCommentList(c1);
        if (post.getUser() != null) {
            postDTO.setUser(post.getUser());
            postDTO.getUser().getPosts().add(post);
        }

        return postDTO;
    }

	    public static Post toEntity(PostDTO postDTO, User user) {
	        Post post = new Post();
	        post.setPostid(postDTO.getPostid());
	        post.setTitle(postDTO.getTitle());
	        post.setText(postDTO.getText());
	        post.setUser(user);
	
	        // CommentDTO 리스트를 Comment 엔티티 리스트로 변환
	        List<Comment> comments = postDTO.getCommentList().stream()
	            .map(commentDTO -> 
	            new Comment(commentDTO.getCommentid(), commentDTO.getText(), post, user))
	            .collect(Collectors.toList());
	        post.setCommentList(comments);
	        
	        user.getPosts().add(post);
	
	        return post;
	    }
	    public static List<PostDTO> toPostDTOList(List<Post> posts)
	    {
	    	List<PostDTO> poststemp = new ArrayList<>();
	    	posts.stream().map(
	    			po -> 
	    			
	    			poststemp.add(PostDTOConverter.toDto(po))
	    			
	    			
	    			).collect(Collectors.toList());
	    	return poststemp;
	    	 
	    }
	    
	    public static List<Post> toPostList(List<PostDTO> postdtos,User user)
	    {
	    	List<Post> poststemp = new ArrayList<>();
	    	postdtos.stream().map(
	    			po -> poststemp.add(PostDTOConverter.toEntity(po, user))
	    			).collect(Collectors.toList());
	    	return poststemp;
	    	
	    	
	    }
}
