package com.test1017.dto;

import org.springframework.stereotype.Repository;

import com.test1017.entity.User;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostDTO {
    
	public PostDTO (Long postid, String title, String text, List<CommentDTO> commentList)
	{
		this.postid = postid;
		this.title = title;
		this.text = text;
		this.commentList = commentList;
	}
    private Long postid;
    
    private String title;
      
    private String text;
    
    private List<CommentDTO> commentList = new ArrayList<>();  // Comment도 DTO로 변환해 사용
    
    private User user; 
    
   
}