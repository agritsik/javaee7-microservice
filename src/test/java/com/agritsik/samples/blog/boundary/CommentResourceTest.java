package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.entity.Comment;
import com.agritsik.samples.blog.entity.Post;
import junit.framework.TestCase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by andrey on 6/10/15.
 */

@RunWith(Arquillian.class)
public class CommentResourceTest extends TestCase {
    public static final Logger LOGGER = Logger.getLogger(CommentResourceTest.class.getName());
    public static final String MESSAGE = "My comment";


    @ArquillianResource
    URL url;

    private Client client;
    private WebTarget postsTarget;

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "com.agritsik")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("jbossas-ds.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() throws Exception {
        this.client = ClientBuilder.newClient();
        this.postsTarget = client.target(new URL(url, "resources/posts").toExternalForm());
    }

    @Test
    @InSequence(1)
    public void testCreate() throws Exception {

        // create post
        Post post = new Post();
        post.setTitle("My post with comments");

        Response response = this.postsTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(post));
        TestContext.createdURL = response.getLocation();

        // create comment for post
        Comment comment = new Comment();
        comment.setMessage(MESSAGE);
        comment.setEmail("commentor2@agritsik.com");

        Response response1 = this.client.target(response.getLocation()).path("comments")
                .request(MediaType.APPLICATION_JSON).post(Entity.json(comment));

        // check result
        assertEquals(Response.Status.CREATED.getStatusCode(), response1.getStatus());
        assertNotNull(response1.getLocation());

    }

    @Test
    @InSequence(2)
    public void testFindAllByPost() throws Exception {

        List<Comment> comments = this.client.target(TestContext.createdURL).path("comments")
                .request(MediaType.APPLICATION_JSON).get(new GenericType<List<Comment>>() {
                });

        assertEquals(1, comments.size());
        assertEquals(MESSAGE, comments.get(0).getMessage());

    }
}