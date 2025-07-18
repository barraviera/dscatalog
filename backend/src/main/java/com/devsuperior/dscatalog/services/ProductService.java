package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.projections.ProductProjection;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// Essa anotação registrará esta classe como um componente que vai participar da injeção de dependência do spring
@Service
public class ProductService {

    // Para fazer as buscas precisaremos da instacia do ProductRepository
    @Autowired
    private ProductRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;


    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(
            String name,
            String categoryId,
            Pageable pageable
    ) {
        List<Long> categoryIds = Arrays.asList();

        if(!"0".equals(categoryId)) {
            categoryIds = Arrays.asList(categoryId.split(",")).stream().map(
                    Long::parseLong
            ).toList();
        }

        Page<ProductProjection> page = repository.searchProducts(categoryIds, name, pageable);
        List<Long> productIds = page.map(x -> x.getId()).toList();

        // Fazemos a consulta dos produtos passando os ids da pagina que buscamos acima
        // este resultado estará desordenado
        List<Product> entities = repository.searchProductsWithCategories(productIds);
        // Aqui geramos uma nova lista de entidades ordenada baseada na ordenadação da pagina
        entities = (List<Product>) Utils.replace(page.getContent(), entities);

        List<ProductDTO> dtos = entities.stream().map(p -> new ProductDTO(p, p.getCategories())).toList();

        Page<ProductDTO> pageDto = new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
        return pageDto;
    }

    // Busca categoria por id
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {

        // O objeto Optional apareceu no java 8 pra evitar trabalhar com valor nulo
        // e quem retorna este tipo Optional é o metodo do Jpa findById, que nunca retornará um objeto nulo, podendo ter ou não uma categoria dentro
        Optional<Product> obj = repository.findById(id);
        // Obter o objeto que está dentro do Optional
        // Usando o orElseThrow, caso nao tenha um objeto Product ele permite informarmos uma excessao
        // Criamos a nossa excessao EntityNotFoundException pra quando buscar um id que nao existe
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada"));
        // Retornamos o resultado como um ProductDTO
        // obs. usando o entity.getCategories() nós trazemos tambem as categorias relacionadas ao Product
        // que declaramos como relacionamento em Product.java e usamos o construtor ProductDTO(entity, entity.getCategories());
        // que recebe um objeto Product e uma lista de categorias
        return new ProductDTO(entity, entity.getCategories());
    }

    // Metodo para inserir categoria no banco de dados
    @Transactional
    public ProductDTO insert(ProductDTO dto) {

        // Criamos um objeto
        Product entity = new Product();

        // entity.setName(dto.getName());
        // Para nao ter que ficar fazendo setName, setPrice, etc aqui no insert e no update
        // vamos criar um metodo auxiliar copyDtoToEntity que faça isso
        copyDtoToEntity(entity, dto);

        // Agora vamos salvar o objeto no banco
        entity = repository.save(entity);

        // agora temos que retornar a entidade como forma de ProductDTO
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {

        try {

            // Iremos buscar a categoria pelo id usando o getReferenceById do proprio spring
            // Importante: quando vamos atualizar, usamos o getReferenceById para buscar, pois ele nao toca no banco, ele instancia um objeto provisório
            // e só quando mandamos salvar é que ele efetivamento toca no banco usando recurso. Diferente de usar o findById, que consumirá recurso para buscar
            // a categoria e depois novamente para salvar a categoria atualizada
            Product entity = repository.getReferenceById(id);

            // entity.setName(dto.getName());
            copyDtoToEntity(entity, dto);

            // Salvando a categoria atualizada no banco
            entity = repository.save(entity);

            // Retornamos a entidade convertida pra ProductDTO
            return new ProductDTO(entity);

        } catch (EntityNotFoundException e) { // A excessao EntityNotFoundException se da quando tentamos salvar o objeto retornado do getReferenceById mas que o id nao existia
            // Agora vamos lançar a nossa excessao personalizada
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    // Metodo para deletar categoria pelo id
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {

        // Se nao existir o id recebido por parametro...
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        // Mas se passou pela verificação acima, iremos no try tentar deletar a categoria pelo id recebido por parametro
        try {
            repository.deleteById(id);

        } catch (DataIntegrityViolationException e) {
            // Caso tentemos deletar um id que nao existe o erro será capturado pelo DataIntegrityViolationException
            // e lançaremos uma excessao personalizada DatabaseException
            // Explicando o que é Integridade Redefencial. Se temos vários produtos que são vinculados a uma categoria, e apagamos esta categoria
            // os produtos terão o id da categoria, mas ela nao vai existir, causando um problema de referencia no banco de dados
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    // Este metodo auxiliar receberá como parametro um objeto Product
    // e um Product DTO, ele vai copiar os dados do ProductDTO para o Product
    // Esse metodo será private e outras classes nao poderão acessa-lo
    private void copyDtoToEntity(Product entity, ProductDTO dto) {

        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());

        // Primeiro vamos limpar a lista de categorias
        entity.getCategories().clear();

        // vamos percorrer a lista de categorias que chegou via paramatro ProductDTO dto
        // vamos acessar essa lista usando o dto.getCategories() e percorrer a lista
        // chamando cada elemento de catDTO
        // usamos a classe categoryRepository pra usar o metodo getReferenceById do spring
        // este getReferenceById instancia um objeto provisorio sem tocar no banco
        for (CategoryDTO catDTO : dto.getCategories()) {

            // Categoria instanciada
            Category category = categoryRepository.getReferenceById(catDTO.getId());
            // Agora vamos associar as cetegorias localizadas, dentro da entitdade de Product
            entity.getCategories().add(category);
            // no final, chamando este metodo copyDtoToEntity, teremos um objeto Product pronto
            // para ser salva ou atualizar quando precisar
        }

    }
}
