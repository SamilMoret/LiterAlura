package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    Set<Autor> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(int birthYear, int deathYear);
    List<Autor> findByNameContainingIgnoreCase(String name);
}
