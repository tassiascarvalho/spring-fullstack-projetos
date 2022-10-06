package br.com.treinaweb.twprojetos.excessoes;

public class FuncionarioPossuiProjetosException extends RuntimeException {
    public FuncionarioPossuiProjetosException(Long id){
        super(String.format(" Exception teste - Funcionario com ID %s ‚ Lider de projetos(s) relacionados ", id));
    }
}
