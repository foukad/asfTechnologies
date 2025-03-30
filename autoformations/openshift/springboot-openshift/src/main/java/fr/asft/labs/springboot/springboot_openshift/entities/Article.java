package fr.asft.labs.springboot.springboot_openshift.entities;

import javax.persistence.*;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    // Getters and Setters
}
