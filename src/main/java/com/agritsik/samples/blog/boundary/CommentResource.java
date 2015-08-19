package com.agritsik.samples.blog.boundary;

import com.agritsik.samples.blog.entity.Comment;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by andrey on 6/10/15.
 */

@Path("/posts/{postId}/comments")
public class CommentResource {

    @Context
    UriInfo uriInfo;

    @EJB
    PostService postService;

    @EJB
    CommentService commentService;


    @POST
    public Response create(@PathParam("postId") long postId, Comment comment){
        commentService.create(comment, postId);

        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(comment.getId())).build();
        return Response.created(uri).build();
    }

    @GET
    public List<Comment> findAllByPost(@PathParam("postId") long postId){
        return commentService.findAllByPost(postId);
    }

}
