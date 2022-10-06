package br.com.treinaweb.twprojetos.excessoes;

public class CargoPossuiFuncionariosException extends RuntimeException{

    public CargoPossuiFuncionariosException(Long id){
        super(String.format("Cargo com ID %s possui funcionario(s) relcionados ", id));
    }
    
}
