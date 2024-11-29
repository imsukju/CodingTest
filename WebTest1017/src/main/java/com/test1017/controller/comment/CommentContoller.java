package com.test1017.controller.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.test1017.converter.CommentDTOconverter;
import com.test1017.dao.impl.UserDao;
import com.test1017.dto.CommentDTO;
import com.test1017.entity.Comment;
import com.test1017.entity.Post;
import com.test1017.service.CommentService;
import com.test1017.service.EntityCallback;
import com.test1017.service.PostService;

@Controller
public class CommentContoller {
	private static final int PAGE_SIZE = 5; 
	@Autowired
	PostService postService;
	
	@Autowired
	@Qualifier("userDao")
	UserDao userDao;
	
    @Autowired
    @Qualifier("postCallbackImpl")
    private EntityCallback<Post> postCallbackImpl;
    
	@Autowired
	@Qualifier("commentService")
	CommentService commentService;
	
	@Autowired
	@Qualifier("commentServiceCallbcak")
	private EntityCallback<Comment> commentServiceCallbcak;

	
    @GetMapping("/posts/{postid}/comments/{commentid}/edit")
    public String getMethodName(@PathVariable("postid") Long postid,@PathVariable("commentid") Long commentid
    		,Model model) {
    	Comment comment = commentService.findById(commentid, commentServiceCallbcak);
    	Post post = postService.findById(commentid, postCallbackImpl);
    	Comment c = commentService.findById(commentid, commentServiceCallbcak);
    	CommentDTO cdto = CommentDTOconverter.toDto(c);
    	model.addAttribute("comment",cdto);
    	model.addAttribute("post",post);
    	
    	
    

        return "comment/edit_comment";
    }
    
    
    @PostMapping("/posts/{postid}/comments/{commentid}/edit")
    public String postEditComment(@PathVariable("postid") Long postid,@PathVariable("commentid")Long commentid,
    		@RequestParam("text") String text, Model model)
    {
    	Comment c = commentService.findById(commentid, commentServiceCallbcak);
    	commentService.update(c, commentServiceCallbcak, text, text);

    	return "redirect:/posts/{postid}";
    }
    
    @PostMapping("/posts/{postid}/comments/{commentid}/delete")
    public String deleteComment(@PathVariable("postid") Long postid,@PathVariable("commentid")Long commentid)
    {
    	Comment c = commentService.findById(commentid, commentServiceCallbcak);
    	commentService.delete(c, commentServiceCallbcak);

    	return "redirect:/posts/{postid}";
    }
    
}
