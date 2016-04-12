package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Article;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Article entity.
 */
public interface ArticleRepository extends JpaRepository<Article,Long> {

    Article findBybarCode(String barCode);

}
