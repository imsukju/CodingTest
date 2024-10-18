package com.test1017.converter;

import com.test1017.dto.CommentDTO;
import com.test1017.dto.PostDTO;
import com.test1017.entity.Comment;
import com.test1017.entity.Post;
import com.test1017.entity.User;

public class CommentDTOconverter {
	public static CommentDTO toDto(Comment comment)
	{
		Long postid = (comment.getPost().getPostid() != null)?comment.getPost().getPostid():null;
		Long userid = (comment.getUser().getUserid() != null)?comment.getUser().getUserid():null;
		
		CommentDTO cto = new CommentDTO(comment.getCommentid(),comment.getText(),comment.getPost().getPostid());
		cto.getUser().getComments().add(cto);
		return cto;
	}
	
	public static Comment toEntity(CommentDTO cto,PostDTO postdto, User user)
	{
		Post post = PostDTOConverter.toEntity(postdto, user);
		Comment comment = new Comment(cto.getCommentid(),cto.getText(),post,user);
		user.getComments().add(comment);
		return comment;
	}

}
