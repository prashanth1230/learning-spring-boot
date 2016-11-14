package com.pras.learningspringboot;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by a487037 on 11/11/2016.
 */
public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {
    public Image findByName(String name);
}
