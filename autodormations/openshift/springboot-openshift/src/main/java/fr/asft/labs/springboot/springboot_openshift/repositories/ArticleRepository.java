package fr.asft.labs.springboot.springboot_openshift.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.asft.labs.springboot.springboot_openshift.entities.*;;
public interface ArticleRepository extends JpaRepository<Article, Long> {}
