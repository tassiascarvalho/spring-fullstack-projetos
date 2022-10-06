package br.com.treinaweb.twprojetos.excessoes;

public class ClientePossuiProjetosException extends RuntimeException{
    public ClientePossuiProjetosException(Long id){
        super(String.format("Cliente com ID %s possui projetos(s) relcionados ", id));
    }
}
