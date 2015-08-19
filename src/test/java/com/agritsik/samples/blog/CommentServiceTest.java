package com.agritsik.samples.blog;

import com.agritsik.samples.blog.boundary.CommentService;
import com.agritsik.samples.blog.boundary.PostService;
import com.agritsik.samples.blog.entity.Comment;
import com.agritsik.samples.blog.entity.Post;
import junit.framework.TestCase;
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
import java.util.List;

/**
 * Created by andrey on 6/10/15.
 */
@RunWith(Arquillian.class)
public class CommentServiceTest extends TestCase {

    public static final String MESSAGE = "my first comment";
    public static final String EMAIL = "commentor1@agritsik.com";

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "com.agritsik.samples")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    CommentService commentService;

    @EJB
    PostService postService;

    @Test
    @InSequence(1)
    public void testCreate() throws Exception {

        Post post = new Post();
        post.setTitle("Post with comments");

        postService.create(post);

        Comment comment = new Comment();
        comment.setEmail(EMAIL);
        comment.setMessage(MESSAGE);

        commentService.create(comment, post.getId());

        assertNotNull(comment.getId());


        TestContext.createdId = post.getId();
    }

    @Test
    @InSequence(2)
    public void testFindAllByPost() throws Exception {

        List<Comment> allByPost = commentService.findAllByPost(TestContext.createdId);

        assertEquals(1, allByPost.size());
        assertEquals(MESSAGE, allByPost.get(0).getMessage());

    }
}