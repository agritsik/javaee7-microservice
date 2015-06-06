package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.entity.Post;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

}
