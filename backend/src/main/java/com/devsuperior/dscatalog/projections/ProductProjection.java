package com.devsuperior.dscatalog.projections;

public interface ProductProjection extends IdProjection<Long> {

    // Esses sao os dois dados que queremos retornar na consulta
    // do metodo searchProducts da classe ProductRepository
    // Long getId();
    // O getId vamos herdar da interface que criamos IdProjection
    // pois ele será um tipo generico

    String getName();

}
