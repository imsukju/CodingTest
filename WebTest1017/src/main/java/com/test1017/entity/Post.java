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
