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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.net.URL;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by andrey on 6/7/15.
 */
@RunWith(Arquillian.class)
public class PostResourceTest extends TestCase{

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
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @InSequence(1)
    public void testCreate() throws Exception {

        Client client = ClientBuilder.newClient();
        client.register(new LoggingFilter(LOGGER, true));
        WebTarget target = client.target(new URL(url, "resources/posts").toExternalForm());

        Post post = new Post();
        post.setTitle(TITLE);

        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(post));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getLocation());
        assertNotNull(response.toString());

        TestContext.createdURL = response.getLocation();

    }

    @Test
    @InSequence(2)
    public void testFind() throws Exception {

        Client client = ClientBuilder.newClient();
        client.register(new LoggingFilter(LOGGER, true));

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

        // get entity by url
        Post post = client.target(TestContext.createdURL)
                .request(MediaType.APPLICATION_JSON).get(Post.class);
        post.setTitle(TITLE_EDITED);

        // try to update entity
        WebTarget target = client.target(new URL(url, "resources/posts").toExternalForm());
        Response response = target.path(String.valueOf(post.getId()))
                .request(MediaType.APPLICATION_JSON).put(Entity.json(post));

        // check update result
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

        Response response = client.target(TestContext.createdURL)
                .request(MediaType.APPLICATION_JSON).delete();

        // check delete result
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        Post deletedPost = client.target(TestContext.createdURL)
                .request(MediaType.APPLICATION_JSON).get(Post.class);

        assertNull(deletedPost);


    }
}