package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// Estendemos a interface JpaRepository do spring, com isso ja temos vários metodos para acessar os dados no banco
// <User, Long> precisamos informar a Entidade e o tipo do id dela
// Ao colocar a anotação @Repository indicamos que ele será um componente injetavel do spring
// e em UserService temos que colocar ele como @Autowired
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar no banco de dados um usuario pelo email
    // Veja como é simples usando o Jpa
    User findByEmail(String email);

    // Consulta personalizada pra buscar por email
    @Query(nativeQuery = true, value = """
			SELECT tb_user.email AS username, tb_user.password, tb_role.id AS roleId, tb_role.authority
			FROM tb_user
			INNER JOIN tb_user_role ON tb_user.id = tb_user_role.user_id
			INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id
			WHERE tb_user.email = :email
		""")
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);


}
