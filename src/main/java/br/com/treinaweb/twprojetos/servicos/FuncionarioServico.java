package br.com.treinaweb.twprojetos.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.treinaweb.twprojetos.entidades.Funcionario;
import br.com.treinaweb.twprojetos.entidades.Projeto;
import br.com.treinaweb.twprojetos.excessoes.FuncionarioNaoEncontradoException;
import br.com.treinaweb.twprojetos.excessoes.FuncionarioPossuiProjetosException;
import br.com.treinaweb.twprojetos.repositorios.FuncionarioRepositorio;
import br.com.treinaweb.twprojetos.repositorios.ProjetoRepositorio;
import br.com.treinaweb.twprojetos.utils.SenhaUtils;

@Service
public class FuncionarioServico {
    

    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;

    @Autowired
    private ProjetoRepositorio projetoRepositorio;
    

    public List<Funcionario> buscarTodos(){
        return funcionarioRepositorio.findAll();
    }

    public List<Funcionario> buscarLideres(){
        return funcionarioRepositorio.findByCargoNome("Gerente");
    }

    public List<Funcionario> buscarEquipe(){
        return funcionarioRepositorio.findByCargoNomeNot("Gerente");
    }

    public Funcionario buscarPorID(Long id){
        return funcionarioRepositorio.findById(id)
            .orElseThrow(() -> new FuncionarioNaoEncontradoException(id));
    }

    public Funcionario cadastrar(Funcionario funcionario){
         //aplicar um hash na senha
         String senhaEncriptada = SenhaUtils.encode(funcionario.getSenha());
         funcionario.setSenha(senhaEncriptada);
         
        return funcionarioRepositorio.save(funcionario);
    }

    public Funcionario atualizar(Funcionario funcionario, Long id){
        Funcionario funcionarioEncontrado = buscarPorID(id);

        funcionario.setSenha(funcionarioEncontrado.getSenha());

        return funcionarioRepositorio.save(funcionario);
    }
    
    public void excluirPorid(Long id){
        Funcionario funcionarioEncontrado =  buscarPorID(id);
        //Verifica se o funcionario ‚ lider do projeto
        if(projetoRepositorio.findByLider(funcionarioEncontrado).isEmpty()){
            //Verifica se est  associado ao projeto como funcionario e se sim exclui o funcionario do projeto
            if(!funcionarioEncontrado.getProjetos().isEmpty()){//isEmpty Verifica se tem valores
                for(Projeto projeto : funcionarioEncontrado.getProjetos()){
                    projeto.getEquipe().remove(funcionarioEncontrado);
                    projetoRepositorio.save(projeto);
                }
            }
            funcionarioRepositorio.delete(funcionarioEncontrado);
        }else{
            throw new FuncionarioPossuiProjetosException(id);
        }
    }
}
