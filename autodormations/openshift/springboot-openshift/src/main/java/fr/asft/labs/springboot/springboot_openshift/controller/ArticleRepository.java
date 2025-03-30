package fr.asft.labs.springboot.springboot_openshift.controller;



import org.springframework.web.bind.annotation.*;

import fr.asft.labs.springboot.springboot_openshift.entities.Article;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleRepository {

    private final ArticleRepository repository;

    public ArticleRepository(ArticleRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Article> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Article create(@RequestBody Article article) {
        return repository.save(article);
    }

    @GetMapping("/{id}")
    public Article getById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }
}
