package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Estendemos a interface JpaRepository do spring, com isso ja temos vários metodos para acessar os dados no banco
// <Category, Long> precisamos informar a Entidade e o tipo do id dela
// Ao colocar a anotação @Repository indicamos que ele será um componente injetavel do spring
// e em CategoryService temos que colocar ele como @Autowired
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {



}
