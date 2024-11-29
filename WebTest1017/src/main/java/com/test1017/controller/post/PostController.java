package com.test1017.controller.post;

import java.util.ArrayList;
import java.util.List;import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.test1017.converter.CommentDTOconverter;
import com.test1017.converter.PostDTOConverter;
import com.test1017.dao.impl.UserDao;
import com.test1017.dto.CommentDTO;
import com.test1017.dto.PostDTO;
import com.test1017.entity.Comment;
import com.test1017.entity.Post;
import com.test1017.entity.User;
import com.test1017.service.CommentService;
import com.test1017.service.EntityCallback;
import com.test1017.service.PostService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class PostController 
{
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

	
    @GetMapping("/posts")
    public String search(@RequestParam(value = "page", defaultValue = "1") int page,
    		 @RequestParam(value = "search", required = false) String search,
    		 Model model) {

    	
    	 if (search != null && !search.isEmpty()) {
    		 if(postService.searchPostsByName(search, page, PAGE_SIZE) != null)
    		 {
    	    		List <PostDTO> postdto = PostDTOConverter.toPostDTOList(postService.searchPostsByName(search, page, PAGE_SIZE));
    	    		long totalPosts = postService.countPostsByName(search);
    	    		int totalPages = (int) Math.ceil((double) totalPosts / PAGE_SIZE);
    	            model.addAttribute("posts", postdto);
    	            model.addAttribute("currentPage", page);
    	            model.addAttribute("totalPages", totalPages);
    	            model.addAttribute("search", search); // 검색어 전달
    		 }

    	 }
 		else {
            // 기존 게시글 목록 조회
            long totalPosts = postService.countPosts();
            int totalPages = (int) Math.ceil((double) totalPosts / PAGE_SIZE);
            System.out.println("토탈포스트"+totalPosts + "토탈페이지" + totalPages);
            if (postService.findPostsByPage(page, PAGE_SIZE) != null)
            {
            	
            	List<Post> posts = postService.findPostsByPage(page, PAGE_SIZE);
                System.out.println("Test posts Size: "  + posts.size());
                List<PostDTO> postdto = PostDTOConverter.toPostDTOList(postService.findPostsByPage(page, PAGE_SIZE));
                System.out.println("Test DTO Size: "  + postdto.size());
                model.addAttribute("posts", postdto);
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", totalPages);
           
            }
   
        }

        // search.jsp로 반환
        return "mainpageWithPost"; // search.jsp 파일을 가리킴
    }
    
    @GetMapping("/posts/new")
    public String showNewPostForm(Model model) {
        List<User> users = userDao.getAlluser();
        model.addAttribute("post", new PostDTO());
        model.addAttribute("userList",users);
        return "post/new";
    }
    
    @PostMapping("/posts")
    public String createPost(@Valid @ModelAttribute("post") PostDTO postdto,
    		BindingResult result) {
        if (result.hasErrors()) {
            return "post/new";
        }
        
        Post post = PostDTOConverter.toEntity(postdto, postdto.getUser());
        postService.post(post, postCallbackImpl);
        return "redirect:/";	
    }
    
    @PostMapping("/users")
    public String creatUser(@ModelAttribute User user)
    {
    	userDao.saveUser(user);
    	return "redirect:/";
    }
    
    @GetMapping("/users")
    public String getMethodName() {
        return "creatUser";
    }
    
    @GetMapping("/posts/search")
    public String getSearch(Model model, @RequestParam(value = "search", required = false) String search) {
        List<PostDTO> postdto = PostDTOConverter.toPostDTOList(postService.searchPostsByName(search, 1, PAGE_SIZE));
        System.out.println("검색갯수 :" + postdto.size());
        model.addAttribute("posts", postdto);
        return "post/searchPost";
    }
    
//    @GetMapping("/searchPost")
//    public String getSearchpost(Model model) {
//    	
//        return "redirect:/posts/search";
//    }
//    
    @GetMapping("/searchPost")
    public String getSearchpost(Model model, @RequestParam(value = "search", required = false) String search) {
        List<PostDTO> postdto = PostDTOConverter.toPostDTOList(postService.searchPostsByName(search, 1, PAGE_SIZE));
        System.out.println("검색갯수 :" + postdto.size());
        model.addAttribute("posts", postdto);
        return "post/searchPost";
    }
    
    
    @GetMapping("/posts/{id}")
    public String getMethodName(@PathVariable("id") Long id,Model model) {
    	Post post = postService.findById(id, postCallbackImpl);
    	if(commentService.findByPostid(post.getPostid()) != null)
    	{
    		List<Comment> coms = commentService.findByPostid(post.getPostid());
    		List<CommentDTO> comments = CommentDTOconverter.toDtoList(coms);
        	model.addAttribute("comments",comments);
    	}
     
        List<User> users = userDao.getAlluser();
        
        
        model.addAttribute("userList",users);
     	model.addAttribute("comment", new CommentDTO());
    	model.addAttribute("post",post);

        return "post/detail";
    }
    
    @GetMapping("/posts/{id}/edit")
    public String getEdit(@PathVariable("id") Long id, Model model)
    {
    	Post post = postService.findById(id, postCallbackImpl);
    	model.addAttribute("post",post);
    	return "post/edit";
    }
    
    
    
    @PostMapping("/posts/{id}/edit")
    public String getEditpst(@PathVariable("id") Long id, Model model,@RequestParam("title")String title, @RequestParam("text")String text)
    {
    	Post post = postService.findById(id, postCallbackImpl);
    	postService.update(post, postCallbackImpl,title,text);
    	model.addAttribute("post",post);
    	return "redirect:/";
    }
    
   
    @PostMapping("/posts/{id}/delete")
    public String getdeletepst(@PathVariable("id") Long id, Model model)
    {
    	Post post = postService.findById(id, postCallbackImpl);
    	postService.delete(post, postCallbackImpl);
    	model.addAttribute("post",post);
    	return "redirect:/";
    }   
    @PostMapping("/posts/{id}/comments")
    public String addComment(@Valid @ModelAttribute("comment") CommentDTO comment,
    		BindingResult result, @PathVariable("id") Long id, @RequestParam("user.userid")Long userid)
    {
        if (result.hasErrors()) {
            return "post/new";
        }
        PostDTO podto = PostDTOConverter.toDto(postService.findById(id, postCallbackImpl));
        		
        User user = userDao.findUserbyid(userid);
        Comment comment1 = CommentDTOconverter.toEntity(comment, podto,user);
        comment1.setPost(postService.findById(id, postCallbackImpl));
        comment1.setUser(user);
        commentService.post(comment1, commentServiceCallbcak);

        return "redirect: /posts/{id}";
    }   
    

    
    
    
    
}


