## 시험내용

게시판 기능 구현  
Entity Class : User, Post, Comment(모두 각각 연관 관계가 있음)  
JPA API 적용(하이버네이트 API 사용 X)   
DTO Class 구현  
Logic : 게시글 작성자 표시, 작성자 게시글 검색  
Page 표시   

### Comment 기능은 구현을 늦게해서 댓글 구현전, 구현후의 포스트 내부 모양이 다릅니다.
> ## **반드시 H2 DB를 실행시켜야지 작동합니다**


---
   
     
   
## Entity Class : User, Post, Comment(모두 각각 연관 관계가 있음)  
### base
```java
package com.test1017.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {
	@CreationTimestamp
	protected LocalDateTime creationDate;
	
	protected String lastModifiedBy;
	
	@UpdateTimestamp
	protected LocalDateTime lastModifiedDate;
}

```
### Post   
```java
package com.test1017.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "POST")
public class Post extends BaseEntity{
	
	@Id
	@Column(name = "POST_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long postid;
	
    @Size(min = 1, max = 120)
    private String title;
    

    
    @Lob
    private String text;
	
	
    @JsonManagedReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Comment> commentList = new ArrayList<>();
	
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

}

```  

### User
```java
package com.test1017.entity;




import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class User {

	
	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long userid;
	
	private String userName;
	
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
	private List<Post> posts = new ArrayList<>();
    
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
	private List<Comment> comments = new ArrayList<>();
}

```  

### Coment   
```java
package com.test1017.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMMENT")
public class Comment extends BaseEntity{
	
	@Id
	@Column(name = "COMMENT_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long commentid;
	
    @Lob
    private String text;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;
	
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

}

```  

## DTO Class 구현   

### PostDTO   
```java   
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
```   
Converter 를 위해서 생성자를 하나 더 만들었습니다.

### UserDto   
```java
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

```   
Converter 를 위해서 생성자를 하나 더 만들었습니다. 

### CommentDTO   
```java
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
 
```   
Converter 를 위해서 생성자를 하나 더 만들었습니다.


## DTO Converter

### Post  
```java
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

```
### User     
```java
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

```    
user 의 toentity부분은 PostConverter, 와 commentConverter 에서 변환할때마다 user도 같이 세팅되도록 코드를 짰습니다.

### Comment
```java
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

```    

---
## 게시판 기능 구현



### 유저 등록
![게시판 화면](./resource/게시판%20메인화면.png)  
현재 화면에서 Create User 버튼을 클릭하여 유저를 등록하는 화면으로 넘어가도록 Get 요청을 보냅니다. 


![유저1](./resource/유저등록/유저등록1.png)   
유저 등록입니다.  
![유저2](./resource/유저등록/user1등록.png)   
![유저3](./resource/유저등록/user2등록.png)   
![유저4](./resource/유저등록/user3등록.png)   

![유저5](./resource/유저등록/DB에%20유저등록된사진.png)    
유저 등록후 DB에도 정상적으로 등록이 되어있습니다


### 게시글 등록
![게시판 화면](./resource/게시판%20메인화면.png)  
현재 화면에서 Creat New Post 버튼을 클릭하여 포스트 등록하는 화면으로 넘어가도록 Get 요청을 보냅니다. 

![포스트1](./resource/포스트등록/1DB에%20있는%20유저선탣.png)  
전에 등록하였던 유저를 선택할 수 있습니다.  
![포스트2](./resource/포스트등록/2포스트등록.png)  
![포스트3](./resource/포스트등록/3포스트등록후%20게시판모습.png)    
포스트 작성자의 이름을 볼 수 있습니다.  

![포스트4](./resource/포스트등록/4여러개의포스트%20등록과%20페이지활성화.png)  
![포스트5](./resource/포스트등록/5다음페이지를%20눌러서%20페이지전환.png)  
페이지를 전환하여 페이지 전환을 보여줍니다  


### 작성글 검색 by UserName
![포스트6](./resource/포스트등록/6유저가%20작성한글검색.png)  
검색창에 검색한 유저의 이름으로 검색을 하여 작성한 포스트을 보여줍니다  

 
 ### 포스트 수정
![포스트4](./resource/포스트등록/4여러개의포스트%20등록과%20페이지활성화.png)  
Action 쪽의 View 텍스트를 클릭하여 자세히 보기 요청을 보냅니다.

![포스트9](./resource/포스트등록/9삭제할글.png)  
자세히 보기 화면입니다  
>** TestSeaching 이라는 제목을 글을 수정하는것이 아닌  그 밑을 Something 글을 수정하는겁니다. **

![포스트7](./resource/포스트등록/7글수정.png)  
포스트 수정 화면에 들어가서 포스트 수정을 시도합니다.  
![포스트8](./resource/포스트등록/8수정후%20게시판.png)  
포스트 수정후 게시판 모습입니다  

### 포스트 삭제


![포스트9](./resource/포스트등록/9삭제할글.png)   
포스트중 하나를 삭제하려고 합니다 .   
![포스트10](./resource/포스트등록/10삭제확인버튼.png)  
삭제버튼을 누르니 삭제 알림이 나옵니다  
![포스트11](./resource/포스트등록/11삭제후게시판.png)    
수정와 삭제가 완료된 최종 게시판의 모습입니다  
![포스트12](./resource/포스트등록/12최종포스트DB.png)
수정와 삭제가 완료된 최종 POST DB의 모습입니다   


### 댓글 기능 추가
> **lastModifiedBy** 는 임의로 만들어놨는데 실수로 지우지 않아서 추가된 컬럼입니다.

![댓글1](./resource/댓글/1댓글추가%20게시판디테일.png)   
댓글 기능 추가후 포스트에서 댓글을 볼 수 있습니다  

![댓글2](./resource/댓글/2유저선택.png)  
포스트 처럼 댓글을 작성할 유저를 선택할 수 있습니다. 

![댓글3](./resource/댓글/3댓글추가전.png)  
댓글를 추가할려고 합니다.  

![댓글4](./resource/댓글/4%20댓글%20달린거확인.png)  
정상적으로 댓글이 추가되었습니다.  

![댓글5](./resource/댓글/5여러개의%20댓글을%20담.png)  
임의로 여러개의 유저로 여러개의 댓글을 달았습니다.  

![댓글6](./resource/댓글/6댓글작성후%20DB.png)   
데이터 베이스에 정상적으로 업데이트가 되었습니다.  


![댓글7](./resource/댓글/7댓글%20수정전.png)   
edit 버튼을 눌러서 댓글을 수정하는 페이지로 넘어왔습니다.   

![댓글8](./resource/댓글/8댓글%20수정후%20커밋전.png)    
댓글을 이런식으로 수정하려고 합니다.   

![댓글9](./resource/댓글/9댓글%20수정후%20게시판.png)    
댓글 수정후 포스트 내부 모습입니다 .  

![댓글10](./resource/댓글/10%20수정된%20댓글이%20db에%20반영됨.png)     
수정된 댓글이 db에 정상적으로 잘 반영되었습니다.  

![댓글11](./resource/댓글/11Another라는%20댓글%20삭제전.png)     
Anthor 라는 내용을 가진 댓글을 삭제하려고 삭제버튼을 눌렀습니다   

![댓글12](./resource/댓글/12삭제완료.png)     
삭제후 포스트 내부 모습입니다.   

![댓글13](./resource/댓글/13삭제후%20DB.png)     
삭제한것도 DB에 정상적으로 반영이 되었습니다.    