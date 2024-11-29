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
