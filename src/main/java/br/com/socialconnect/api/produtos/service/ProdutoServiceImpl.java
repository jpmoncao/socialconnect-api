package br.com.socialconnect.api.produtos.service;

import br.com.socialconnect.api.exception.RecursoNaoEncontradoException;
import br.com.socialconnect.api.produtos.dto.*;
import br.com.socialconnect.api.produtos.exception.*;
import br.com.socialconnect.api.produtos.model.*;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoServiceImpl implements ProdutoService {
    private final ProdutoRepository repository;

    public ProdutoServiceImpl(ProdutoRepository repository) { this.repository = repository; }

    @Override @Transactional(readOnly = true)
    public Page<ProdutoResponseDTO> listar(String nome, CategoriaProduto categoria, Pageable pageable) {
        Specification<Produto> filtro = (root, query, cb) -> cb.conjunction();
        if (nome != null && !nome.isBlank()) {
            String busca = "%" + nome.trim().toLowerCase().replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%";
            filtro = filtro.and((root, query, cb) -> cb.like(cb.lower(root.get("nome")), busca, '\\'));
        }
        if (categoria != null) filtro = filtro.and((root, query, cb) -> cb.equal(root.get("categoria"), categoria));
        return repository.findAll(filtro, pageable).map(this::mapear);
    }

    @Override @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) { return mapear(encontrar(id)); }

    @Override @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        validarEstoque(dto);
        if (repository.existsByNomeIgnoreCase(dto.nome().trim())) throw new NomeProdutoDuplicadoException(dto.nome());
        Produto produto = new Produto();
        preencher(produto, dto);
        return mapear(repository.saveAndFlush(produto));
    }

    @Override @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = encontrar(id);
        validarEstoque(dto);
        if (repository.existsByNomeIgnoreCaseAndIdProdutoNot(dto.nome().trim(), id)) throw new NomeProdutoDuplicadoException(dto.nome());
        preencher(produto, dto);
        return mapear(repository.saveAndFlush(produto));
    }

    @Override @Transactional
    public void deletar(Long id) { repository.delete(encontrar(id)); }

    private Produto encontrar(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id));
    }

    private void validarEstoque(ProdutoRequestDTO dto) {
        if (dto.estoqueAtual() != null && dto.estoqueAtual() < 0) throw new EstoqueNegativoException();
    }

    private void preencher(Produto produto, ProdutoRequestDTO dto) {
        produto.setNome(dto.nome().trim());
        produto.setCategoria(dto.categoria());
        produto.setEstoqueAtual(dto.estoqueAtual());
        produto.setEstoqueMinimo(dto.estoqueMinimo());
        produto.setUnidadeMedida(dto.unidadeMedida().trim());
    }

    private ProdutoResponseDTO mapear(Produto produto) {
        return new ProdutoResponseDTO(produto.getIdProduto(), produto.getNome(), produto.getCategoria(),
                produto.getEstoqueAtual(), produto.getEstoqueMinimo(), produto.getUnidadeMedida(),
                produto.getDataCadastro(), produto.getEstoqueAtual() < produto.getEstoqueMinimo());
    }
}
