package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.entity.Post;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by andrey on 6/6/15.
 */

@Stateless
public class PostService {

    @PersistenceContext
    EntityManager entityManager;

    public void create(Post post){
        entityManager.persist(post);
    }

    public Post find(long id){
        return entityManager.find(Post.class, id);
    }

    public Post update(Post post){
        return entityManager.merge(post);
    }

    public void remove(long id){
        Post post = entityManager.find(Post.class, id);
        entityManager.remove(post);
    }

    public List<Post> find(int first, int maxResult){
        return entityManager.createNamedQuery("Post.findAll")
                .setFirstResult(first).setMaxResults(maxResult).getResultList();
    }

}
