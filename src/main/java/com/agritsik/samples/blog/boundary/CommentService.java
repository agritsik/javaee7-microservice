package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.entity.Comment;
import com.agritsik.samples.blog.entity.Post;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by andrey on 6/10/15.
 */
@Stateless
public class CommentService {

    @PersistenceContext
    EntityManager entityManager;

    @EJB
    PostService postService;

    public void create(Comment comment, long postId) {
        Post post = postService.find(postId);
        comment.setPost(post);

        entityManager.persist(comment);
    }

    public List<Comment> findAllByPost(long postId) {
        return entityManager.createNamedQuery("Comment.findAllByPost")
                .setParameter("postId", postId)
                .getResultList();
    }


}
