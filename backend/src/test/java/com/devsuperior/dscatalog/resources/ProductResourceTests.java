package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Testes na camada web, ou seja, controller, ou resources como podemos chamar tambem
@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    // Atributos

    @Autowired
    private MockMvc mockMvc;

    // Usar o @MockBean ao inves do @Mock, quando a classe de teste carrega o contexto da aplicação
    // e precisa mockar bean do sistema. Ex. quando usar o @WebMvcTest, usaremos o @MockBean
    @MockBean
    private ProductService service;

    // Usar o PageImpl pra instanciar um objeto concreto
    private PageImpl<ProductDTO> page;

    private ProductDTO productDTO;

    // Simuladores, que executa antes de cada metodo de teste
    @BeforeEach
    void setUp() throws Exception {

        // Usamos a classe Factory que criamos para nos auxiliar
        productDTO = Factory.createProductDTO();
        // Criamos uma page passando uma lista de productDTO
        page = new PageImpl<>(List.of(productDTO));

        // Quando chamarmos o metodo findAllPaged do ProductService passando qualquer argumento any()
        // ele terá que retornar um objeto page do tipo PageImpl<ProductDTO>
        when(service.findAllPaged(any())).thenReturn(page);
    }

    // Metodos

    @Test
    // o findAllPaged do ProductResource deveria retornar um page
    public void findAllShouldReturnPage() throws Exception {

        // o perform faz uma requisição e usando o andExpect para esperar que o status da resposta seja um isOk (200)
        mockMvc.perform(get("/products")).andExpect(status().isOk());
    }

}
