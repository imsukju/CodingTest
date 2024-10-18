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

public class UserDTOConverter {
	
	public static UserDTO toDTO(User user)
	{
		List<PostDTO> postdtos = user.getPosts().stream()
				.map(pd ->
				{
					List<CommentDTO> commentdtos = pd.getCommentList().stream()
							.map(comment -> CommentDTOconverter.toDto(comment)).collect(Collectors.toList());
					return new PostDTO(pd.getPostid(),pd.getTitle(),pd.getText(),commentdtos,user);
				})
				.collect(Collectors.toList());
		
		
		List<CommentDTO> commentdtos = user.getComments().stream().
				map(p -> new CommentDTO(p.getCommentid(), p.getText(), p.getPost().getPostid())).collect(Collectors.toList());
		
		return new UserDTO(user.getUserid(),user.getUserName(),postdtos,commentdtos);
		

	}
	
//	public static User toEntity(UserDTO user)
//	{
//		List<Comment> comments = CommentDTOconverter.toEntity(null, null, null)
//		
//	
//	}
	


}
