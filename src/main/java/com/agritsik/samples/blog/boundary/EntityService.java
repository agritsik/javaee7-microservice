package com.agritsik.samples.blog.boundary;

import java.util.List;

/**
 * Optional interface that I use only for naming convention for my services
 */
public interface EntityService<E> {

    void create(E e);

    E find(int id);

    E update(E e);

    void delete(int id);

    List<E> find();

}
