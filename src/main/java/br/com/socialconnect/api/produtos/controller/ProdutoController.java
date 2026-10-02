package br.com.socialconnect.api.produtos.controller;

import br.com.socialconnect.api.exception.ProblemDetail;
import br.com.socialconnect.api.produtos.dto.*;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/produtos")
@Tag(name = "Produtos", description = "Cadastro e consulta de produtos do estoque")
public class ProdutoController {
    private final ProdutoService service;
    public ProdutoController(ProdutoService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Lista produtos com filtros e paginação")
    @ApiResponse(responseCode = "200", description = "Produtos listados")
    @ApiResponse(responseCode = "400", description = "Filtro ou ordenação inválidos", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public Page<ProdutoResponseDTO> listar(@Parameter(example = "arroz") @RequestParam(required = false) String nome,
            @Parameter(example = "ALIMENTO") @RequestParam(required = false) CategoriaProduto categoria,
            @ParameterObject @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return service.listar(nome, categoria, pageable);
    }

    @GetMapping("/{id_produto}")
    @Operation(summary = "Busca produto por ID")
    @ApiResponse(responseCode = "200", description = "Produto encontrado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ProdutoResponseDTO buscarPorId(@Parameter(example = "1") @PathVariable("id_produto") Long id) { return service.buscarPorId(id); }

    @PostMapping
    @Operation(summary = "Cadastra produto")
    @ApiResponse(responseCode = "201", description = "Produto cadastrado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Nome duplicado", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "422", description = "Estoque negativo", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO criado = service.criar(dto);
        return ResponseEntity.created(URI.create("/api/v1/produtos/" + criado.idProduto())).body(criado);
    }

    @PutMapping("/{id_produto}")
    @Operation(summary = "Substitui produto")
    @ApiResponse(responseCode = "200", description = "Produto atualizado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Nome duplicado", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "422", description = "Estoque negativo", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ProdutoResponseDTO atualizar(@Parameter(example = "1") @PathVariable("id_produto") Long id, @Valid @RequestBody ProdutoRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id_produto}")
    @Operation(summary = "Remove produto")
    @ApiResponse(responseCode = "204", description = "Produto removido")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<Void> deletar(@Parameter(example = "1") @PathVariable("id_produto") Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
