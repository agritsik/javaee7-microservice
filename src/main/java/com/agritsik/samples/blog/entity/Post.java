package com.agritsik.samples.blog.entity;

import javax.persistence.*;

/**
 * Created by andrey on 6/6/15.
 */

@Entity
@Table(name = "posts")
@NamedQueries({
        @NamedQuery(name = Post.FIND_ALL, query = "SELECT p FROM Post p")
})
public class Post {

    public static final String FIND_ALL = "Post.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
