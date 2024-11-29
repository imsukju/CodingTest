package com.test1017.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.test1017.dto.CommentDTO;
import com.test1017.dto.PostDTO;
import com.test1017.entity.Comment;
import com.test1017.entity.Post;
import com.test1017.entity.User;

public class CommentDTOconverter {
	public static CommentDTO toDto(Comment comment)
	{
		Long postid = (comment.getPost().getPostid() != null)?comment.getPost().getPostid():null;
		
		CommentDTO cto = new CommentDTO();
		cto.setCommentid(comment.getCommentid());
		cto.setText(comment.getText());
		cto.setPostid(postid);
		cto.setUser(comment.getUser());
		if(comment !=null && cto.getUser() != null)
		{
			cto.getUser().getComments().add(comment);
		}
		
		
		return cto;
	}
	
	public static Comment toEntity(CommentDTO cto,PostDTO postdto, User user)
	{
		Post post = PostDTOConverter.toEntity(postdto, user);
		Comment comment = new Comment();
		comment.setText(cto.getText());
		post.setUser(user);
		user.getComments().add(comment);
		return comment;
	}
	
	public static List<CommentDTO> toDtoList(List<Comment> comments)
	{
		List<CommentDTO> coDto = new ArrayList<>();
		
		coDto = comments.stream().map(
				p -> toDto(p)
				).collect(Collectors.toList());
		
		return coDto;
	}
	
	public static List<Comment> toDtoList(List<CommentDTO> comments,PostDTO postdto, User user)
	
	{
		List<Comment> coms = new ArrayList<>();
		
		coms = comments.stream().map(
				p -> toEntity(p, postdto, user)
				).collect(Collectors.toList());
		
		return coms;
	}

}
