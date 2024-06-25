package br.com.alura.literalura.livraria;

import br.com.alura.literalura.service.ConsumoApi;

public class Livraria {
    public void consumo(){
        ConsumoApi consumoApi = new ConsumoApi ();
        var json = consumoApi.obtenerDatos("https://gutendex.com/books/?search=%20Romeo%20and%20Juliet");
        System.out.println(json);
    }

}
