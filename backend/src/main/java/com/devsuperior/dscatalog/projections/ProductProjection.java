package com.devsuperior.dscatalog.projections;

public interface ProductProjection {

    // Esses sao os dois dados que queremos retornar na consulta
    // do metodo searchProducts da classe ProductRepository
    Long getId();
    String getName();

}
