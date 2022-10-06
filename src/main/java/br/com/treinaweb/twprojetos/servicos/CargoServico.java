package br.com.treinaweb.twprojetos.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.treinaweb.twprojetos.entidades.Cargo;
import br.com.treinaweb.twprojetos.excessoes.CargoNaoEncontradoException;
import br.com.treinaweb.twprojetos.excessoes.CargoPossuiFuncionariosException;
import br.com.treinaweb.twprojetos.repositorios.CargoRepositorio;
import br.com.treinaweb.twprojetos.repositorios.FuncionarioRepositorio;

@Service
public class CargoServico {

    @Autowired
    private CargoRepositorio cargoRepositorio;

    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;

    public List<Cargo> buscarTodos(){
        return cargoRepositorio.findAll();
    }

    public Cargo buscarPorID(Long id){
        Cargo cargoEncontrado = cargoRepositorio.findById(id).orElseThrow(() -> new CargoNaoEncontradoException(id));

        return cargoEncontrado;
    }

    public Cargo cadastrar(Cargo cargo){
        return cargoRepositorio.save(cargo);
    }

    public Cargo atualizar(Cargo cargo, Long id){
        buscarPorID(id);
        return cargoRepositorio.save(cargo);
    }
    
    public void excluirPorid(Long id){
        Cargo cargoEncontrado =  buscarPorID(id);

        if(funcionarioRepositorio.findByCargo(cargoEncontrado).isEmpty()){
            cargoRepositorio.delete(cargoEncontrado);
        }else{
            throw new CargoPossuiFuncionariosException(id);
        }
    }


}
