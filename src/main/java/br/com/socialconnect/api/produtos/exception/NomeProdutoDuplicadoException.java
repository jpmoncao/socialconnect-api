package br.com.socialconnect.api.produtos.exception;

public class NomeProdutoDuplicadoException extends RuntimeException {
    public NomeProdutoDuplicadoException(String nome) { super("Produto com nome já cadastrado: " + nome); }
}
