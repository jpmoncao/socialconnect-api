package br.com.socialconnect.api.produtos.service;

import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.exception.*;
import br.com.socialconnect.api.produtos.model.*;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {
    @Mock ProdutoRepository repository;
    @InjectMocks ProdutoServiceImpl service;

    private ProdutoRequestDTO dto(int estoque) {
        return new ProdutoRequestDTO("Arroz 5kg", CategoriaProduto.ALIMENTO, estoque, 10, "unidade");
    }

    @Test void deveCriarProdutoQuandoDadosValidos() {
        // Arrange
        when(repository.saveAndFlush(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // Act
        var resposta = service.criar(dto(3));
        // Assert
        assertEquals("Arroz 5kg", resposta.nome());
        assertTrue(resposta.estoqueBaixo());
        verify(repository).saveAndFlush(any(Produto.class));
    }

    @Test void deveLancarExcecaoQuandoEstoqueNegativo() {
        // Arrange
        ProdutoRequestDTO entrada = dto(-1);
        // Act / Assert
        assertThrows(EstoqueNegativoException.class, () -> service.criar(entrada));
        verifyNoInteractions(repository);
    }

    @Test void deveLancarExcecaoQuandoNomeDuplicado() {
        // Arrange
        when(repository.existsByNomeIgnoreCase("Arroz 5kg")).thenReturn(true);
        // Act / Assert
        assertThrows(NomeProdutoDuplicadoException.class, () -> service.criar(dto(3)));
        verify(repository, never()).saveAndFlush(any());
    }
}
