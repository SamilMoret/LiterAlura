package br.com.alura.literalura.service;

import br.com.alura.literalura.model.Autor;
import br.com.alura.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    @Autowired
    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public Set<Autor> buscarAutoresVivosNoAno(int ano) {
        return autorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(ano, ano);
    }
}
