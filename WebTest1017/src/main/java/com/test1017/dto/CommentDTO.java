package com.test1017.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.test1017.entity.Post;
import com.test1017.entity.User;


import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentDTO {

	public CommentDTO(Long commentid, String text, Long postid)
	{
		this.commentid = commentid;
		this.text = text;
		this.postid = postid;
	}
	private Long commentid;
	

    private String text;
    

    private Long postid;
	
    private UserDTO user;

}
