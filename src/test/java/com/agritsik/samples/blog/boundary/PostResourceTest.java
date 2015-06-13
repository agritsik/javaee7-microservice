package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.entity.Post;
import junit.framework.TestCase;
import org.glassfish.jersey.filter.LoggingFilter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
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
 * Created by andrey on 6/7/15.
 */
@RunWith(Arquillian.class)
public class PostResourceTest extends TestCase {

    private static final Logger LOGGER = Logger.getLogger(PostResourceTest.class.getName());
    public static final String TITLE = "My new post via REST!";
    public static final String TITLE_EDITED = "My edited post via REST!";

    @ArquillianResource
    URL url;

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "com.agritsik")
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("jbossas-ds.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @InSequence(1)
    public void testCreate() throws Exception {

        Client client = ClientBuilder.newClient();
        client.register(new LoggingFilter(LOGGER, true));
        WebTarget target = client.target(new URL(url, "resources/posts").toExternalForm());

        // create post
        Post post = new Post();
        post.setTitle(TITLE);

        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(post));

        // check result
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getLocation());

        TestContext.createdURL = response.getLocation();
    }

    @Test
    @InSequence(2)
    public void testFind() throws Exception {

        Client client = ClientBuilder.newClient();
        client.register(new LoggingFilter(LOGGER, true));

        // try to find post by location
        Post post = client.target(TestContext.createdURL)
                .request(MediaType.APPLICATION_JSON).get(Post.class);

        System.out.println(post);
        assertNotNull(post);

    }

    @Test
    @InSequence(3)
    public void testUpdate() throws Exception {
        Client client = ClientBuilder.newClient();
        client.register(new LoggingFilter(LOGGER, true));

        // find post by location
        Post post = client.target(TestContext.createdURL)
                .request(MediaType.APPLICATION_JSON).get(Post.class);
        post.setTitle(TITLE_EDITED);

        // try to update post
        WebTarget target = client.target(new URL(url, "resources/posts").toExternalForm());
        Response response = target.path(String.valueOf(post.getId()))
                .request(MediaType.APPLICATION_JSON).put(Entity.json(post));

        // check result
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        Post updatedPost = client.target(TestContext.createdURL)
                .request(MediaType.APPLICATION_JSON).get(Post.class);
        assertEquals(TITLE_EDITED, updatedPost.getTitle());

    }

    @Test
    @InSequence(4)
    public void testDelete() throws Exception {

        Client client = ClientBuilder.newClient();
        client.register(new LoggingFilter(LOGGER, true));

        // try to delete post
        Response response = client.target(TestContext.createdURL)
                .request(MediaType.APPLICATION_JSON).delete();

        // check result
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        Post deletedPost = client.target(TestContext.createdURL)
                .request(MediaType.APPLICATION_JSON).get(Post.class);

        assertNull(deletedPost);


    }

    @Test
    @InSequence(5)
    public void testFind1() throws Exception {

        Client client = ClientBuilder.newClient();
        client.register(new LoggingFilter(LOGGER, true));
        WebTarget target = client.target(new URL(url, "resources/posts").toExternalForm());

        // create posts
        for (int i = 0; i < 85; i++) {
            Post post = new Post();
            post.setTitle("Another post #" + i);
            target.request(MediaType.APPLICATION_JSON).post(Entity.json(post));
        }

        // try to find posts
        List<Post> posts = target.queryParam("start", 0).queryParam("maxResult", 10)
                .request(MediaType.APPLICATION_JSON).get(new GenericType<List<Post>>() {});

        // check result
        assertEquals(10, posts.size());
        assertEquals("Another post #0", posts.get(0).getTitle());

    }
}