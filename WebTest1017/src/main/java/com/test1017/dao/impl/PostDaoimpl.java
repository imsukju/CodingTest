package com.test1017.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test1017.Main;
import com.test1017.dao.Postdao;
import com.test1017.entity.Post;
import com.test1017.entity.QPost;
import com.test1017.entity.QUser;
import com.test1017.entity.User;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;

@Repository
@Qualifier("postDao")
public class PostDaoimpl implements Postdao{

	@PersistenceContext
	EntityManager em;
	
	private JPAQueryFactory queryFactory;
	
    @PostConstruct
    public void init() {
        this.queryFactory = new JPAQueryFactory(em);
    }
	
	@Override
	public void save(Post post) {
		em.persist(post);

		
	}

	@Override
	public Post findById(Long id) {
		QPost post = QPost.post;
		
		return queryFactory.selectFrom(post).where(post.postid.eq(id)).fetchOne();
	}

	@Override
	public List<Post> findAll() {
		// TODO Auto-generated method stub
		QPost post = QPost.post;
		
		return queryFactory.selectFrom(post).fetch();
	}

	@Transactional
	@Override
	public void delete(Post entity) {
	       Post existingPost = em.find(Post.class, entity.getPostid());
	       
	       
	       em.createNativeQuery("DELETE FROM post WHERE post_id = :id")
	       .setParameter("id", existingPost.getPostid())
	       .executeUpdate();
	}

	@Override
	public void update(Post entity,String title, String text) {
       Post existingPost = em.find(Post.class, entity.getPostid());

        if (existingPost != null) {
            existingPost.setTitle(title); 
            existingPost.setText(text);   
            em.merge(existingPost);  
        }
		
	}
	
    @SuppressWarnings("deprecation")
	public long countPostsByName(String search) {

    	QUser quser = QUser.user;

        return queryFactory
                .selectFrom(quser)
                .where(quser.userName.containsIgnoreCase(search)) // 검색어가 포함된 name
                .fetchCount(); // 개수 반환
    }
    
    @Override
    public List<Post> searchPostsByName(String search, int page, int pageSize) {
    	QUser user = QUser.user;
    	QPost post = QPost.post;


    	return queryFactory
    	         .selectFrom(post)
    	         .leftJoin(post.user, user) // Post와 User의 관계에 맞는 조인 설정
    	         .where(user.userName.containsIgnoreCase(search)) // 검색 조건
    	         .fetch();

    }

	@Override
	public List<Post> findPostsByPage(int page, int pageSize) {
		
		// TODO Auto-generated method stub
	   	if (page < 1) {
    		page = 1;
    	}
    	
        QPost post = QPost.post;
        return queryFactory
                .selectFrom(post)
                .orderBy(post.creationDate.desc()) // 원하는 정렬 방식 적용
                .offset((page - 1) * pageSize) // 페이지 오프셋 계산
                .limit(pageSize) // 한 페이지당 몇 개의 포스트를 가져올지 제한
                .fetch();

	}
	


}
