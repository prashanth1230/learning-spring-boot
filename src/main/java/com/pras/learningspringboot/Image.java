package com.pras.learningspringboot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by a487037 on 11/11/2016.
 */
@Entity
public class Image {
    @Id @GeneratedValue
    private Long id;
    private String name;
    //Default constructor is needed by JPA. We don't use it
    private Image() {}
    public Image(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
