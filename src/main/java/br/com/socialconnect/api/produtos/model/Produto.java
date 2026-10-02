package br.com.socialconnect.api.produtos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "produtos")
public class Produto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id_produto")
    private Long idProduto;
    @Column(nullable = false, unique = true, length = 150)
    private String nome;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private CategoriaProduto categoria;
    @Column(name = "estoque_atual", nullable = false)
    private Integer estoqueAtual;
    @Column(name = "estoque_minimo", nullable = false)
    private Integer estoqueMinimo;
    @Column(name = "unidade_medida", nullable = false, length = 20)
    private String unidadeMedida;
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDate dataCadastro;

    @PrePersist void preencherDataCadastro() { if (dataCadastro == null) dataCadastro = LocalDate.now(); }
    public Long getIdProduto() { return idProduto; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public CategoriaProduto getCategoria() { return categoria; }
    public void setCategoria(CategoriaProduto categoria) { this.categoria = categoria; }
    public Integer getEstoqueAtual() { return estoqueAtual; }
    public void setEstoqueAtual(Integer estoqueAtual) { this.estoqueAtual = estoqueAtual; }
    public Integer getEstoqueMinimo() { return estoqueMinimo; }
    public void setEstoqueMinimo(Integer estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }
    public String getUnidadeMedida() { return unidadeMedida; }
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }
    public LocalDate getDataCadastro() { return dataCadastro; }
}
