package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.entity.Post;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.*;

/**
 * Created by andrey on 6/6/15.
 */

@RunWith(Arquillian.class)
public class PostServiceTest                {

    public static final String TITLE = "My first post!";
    public static final String TITLE_EDITED = "My edited first post!";

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "com.agritsik")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }


    @EJB
    PostService postService;

    @Test
    @InSequence(1)
    public void testCreate() throws Exception {

        Post post = new Post();
        post.setTitle(TITLE);
        postService.create(post);

        assertNotNull(post.getId());
        TestContext.createdId = post.getId();

    }

    @Test
    @InSequence(2)
    public void testFind() throws Exception {

        Post post = postService.find(TestContext.createdId);
        assertNotNull(post);
        assertEquals(TITLE, post.getTitle());

    }

    @Test
    @InSequence(3)
    public void testUpdate() throws Exception {

        Post post = postService.find(TestContext.createdId);
        post.setTitle(TITLE_EDITED);
        postService.update(post);

        Post updatedPost = postService.find(TestContext.createdId);

        assertEquals(TITLE_EDITED, updatedPost.getTitle());

    }

    @Test
    @InSequence(4)
    public void testRemove() throws Exception {
        postService.remove(TestContext.createdId);

        Post post = postService.find(TestContext.createdId);
        assertNull(post);
    }
}