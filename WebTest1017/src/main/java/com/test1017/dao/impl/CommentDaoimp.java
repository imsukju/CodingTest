package com.test1017.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test1017.dao.CommentDao;
import com.test1017.entity.Comment;
import com.test1017.entity.Post;
import com.test1017.entity.QComment;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Qualifier("commentDao")
public class CommentDaoimp implements CommentDao{
	@PersistenceContext
	EntityManager em;
	
	private JPAQueryFactory queryFactory;
	
    @PostConstruct
    public void init() {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Transactional
	@Override
	public void save(Comment comment) {
		// TODO Auto-generated method stub
		em.persist(comment);
		
	}

	@Override
	public Comment findById(Long id) {
		QComment qcomment = QComment.comment;
		return queryFactory.selectFrom(qcomment).where(qcomment.commentid.eq(id)).fetchOne();
	}

	@Override
	public List<Comment> findAll() {
		// TODO Auto-generated method stub
		QComment qcomment = QComment.comment;
		return queryFactory.selectFrom(qcomment).fetch();
	}

	@Transactional
	@Override
	public void delete(Comment comment) {
		// TODO Auto-generated method stub
		Comment exitingcomment = em.find(Comment.class, comment.getCommentid());
	       
	       
	       em.createNativeQuery("DELETE FROM comment WHERE comment_id = :id")
	       .setParameter("id", exitingcomment.getCommentid())
	       .executeUpdate();
		
	}

	@Transactional
	@Override
	public void update(Comment comment, String text) {
		Comment exitingcomment = em.find(Comment.class, comment.getCommentid());
		
		if(exitingcomment != null)
		{
			exitingcomment.setText(text);
			em.merge(exitingcomment);
		}

	}
	@Override
	public List<Comment> findByPostid(Long postid) {
		QComment qcomment = QComment.comment;
		
		return queryFactory.selectFrom(qcomment).where(qcomment.post.postid.eq(postid)).fetch();
	}

}
