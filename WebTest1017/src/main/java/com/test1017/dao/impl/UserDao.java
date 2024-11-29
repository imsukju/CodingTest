package com.test1017.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test1017.dto.PostDTO;
import com.test1017.entity.Post;
import com.test1017.entity.QUser;
import com.test1017.entity.User;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Qualifier("userDao")
public class UserDao {
	@PersistenceContext
	EntityManager em;
	
	private JPAQueryFactory queryFactory;
	
    @PostConstruct
    public void init() {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Transactional
    public void saveUser(User user)
    {
    	em.persist(user);
    }
    
    @Transactional
    public List<User> getAlluser()
    {
    	QUser qUser = QUser.user; 
    	return queryFactory.selectFrom(qUser).fetch();
    
    }
    
    public List<Post> getAllpost(User user)
    {
    	return null;
    }
    
    
    public User findUserbyid(Long id)
    {
    	return em.find(User.class, id);
    }
}
