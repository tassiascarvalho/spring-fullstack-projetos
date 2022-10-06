package br.com.treinaweb.twprojetos.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.treinaweb.twprojetos.entidades.Cliente;
import br.com.treinaweb.twprojetos.excessoes.ClienteNaoEncontradoException;
import br.com.treinaweb.twprojetos.excessoes.ClientePossuiProjetosException;
import br.com.treinaweb.twprojetos.repositorios.ClienteRepositorio;
import br.com.treinaweb.twprojetos.repositorios.ProjetoRepositorio;

@Service
public class ClienteServico {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ProjetoRepositorio projetoRepositorio;
    

    public List<Cliente> buscarTodos(){
        return clienteRepositorio.findAll();
    }

    public Cliente buscarPorID(Long id){
        return clienteRepositorio.findById(id)
            .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    public Cliente cadastrar(Cliente cliente){
        return clienteRepositorio.save(cliente);
    }

    public Cliente atualizar(Cliente cliente, Long id){
        buscarPorID(id);
        return clienteRepositorio.save(cliente);
    }
    
    public void excluirPorid(Long id){
        Cliente clienteEncontrado =  buscarPorID(id);

        if(projetoRepositorio.findByCliente(clienteEncontrado).isEmpty()){
            clienteRepositorio.delete(clienteEncontrado);
        }else{
            throw new ClientePossuiProjetosException(id);
        }
    }
}
