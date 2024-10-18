package com.test1017.dto;

import java.util.List;

import com.test1017.entity.Comment;
import com.test1017.entity.User;

import java.util.ArrayList;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

	private Long userid;
	
	private String userName;
	

	private List<PostDTO> posts = new ArrayList<>();
    
	private List<CommentDTO> comments = new ArrayList<>();
}
