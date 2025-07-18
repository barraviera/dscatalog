package com.devsuperior.dscatalog.projections;

public interface IdProjection<E> {

    // Tipo E é generico podendo ser string, long, etc
    E getId();

}
