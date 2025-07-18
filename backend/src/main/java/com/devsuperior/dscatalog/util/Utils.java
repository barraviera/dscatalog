package com.devsuperior.dscatalog.util;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.projections.IdProjection;
import com.devsuperior.dscatalog.projections.ProductProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {


    // Metodo auxiliar que retornará uma lista de produtos
    // No 1º parametro ele receberá uma lista ordenada de ProductProjection que veio da paginação
    // no 2º recebemos a lista desordenada
    public static <ID> List<? extends IdProjection<ID>> replace(List<? extends IdProjection<ID>> ordered, List<? extends IdProjection<ID>> unordered) {

        // Vamos guardar os produtos em um map com a chave id Long e o objeto Product
        Map<ID, IdProjection<ID>> map = new HashMap<>();
        // Pra cada objeto Product que está na lista unordered(desordenada), vamos copiar para o map
        // pq quando precisar procurar um produto nesse map a busca será instantanea
        for(IdProjection<ID> obj : unordered) {
            map.put(obj.getId(), obj);
        }

        // Criar a lista de produtos ordenada
        List<IdProjection<ID>> result = new ArrayList<>();
        // Percorremos a lista de ProductProjection que está ordenada
        // e pra cada objeto desta lista ordenada pegamos o id dele, acessamos a entidade produto no map
        // e adicionamos na lista result
        for(IdProjection<ID> obj : ordered) {
            result.add(map.get(obj.getId()));
        }

        return result;
    }
}
