package br.com.treinaweb.twprojetos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.treinaweb.twprojetos.repositorios.ClienteRepositorio;
import br.com.treinaweb.twprojetos.repositorios.FuncionarioRepositorio;
import br.com.treinaweb.twprojetos.validadores.ClienteValidador;
import br.com.treinaweb.twprojetos.validadores.FuncionarioValidador;
import br.com.treinaweb.twprojetos.validadores.PessoaValidador;

@Configuration
public class ValidadorConfig {
    //cria metodos para ensinar o spring a genrenciar classes n∆o gerenciadas por ele, tipo de bibliotecas externas, e validades

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;


    @Bean //Ç responsavel a ensinar o webconteiner com injeá∆o de dependencia, como instanciar classes
    public ClienteValidador clienteValidador(){
        return new ClienteValidador(clienteRepositorio);
    }

    @Bean
    public FuncionarioValidador funcionarioValidador(){
        return new FuncionarioValidador(funcionarioRepositorio);
    }

    @Bean
    public PessoaValidador pessoaValidador(){
        return new PessoaValidador();
    }
    
}
