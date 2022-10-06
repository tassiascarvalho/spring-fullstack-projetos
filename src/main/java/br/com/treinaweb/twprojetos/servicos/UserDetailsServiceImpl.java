package br.com.treinaweb.twprojetos.servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.treinaweb.twprojetos.entidades.Funcionario;
import br.com.treinaweb.twprojetos.entidades.UserDetailsImpl;
import br.com.treinaweb.twprojetos.repositorios.FuncionarioRepositorio;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Ç o metodo que chama para encontrar o usuario na aplicaá∆o
        //Autenticaá∆o do Usuario
        Funcionario funcionario = funcionarioRepositorio.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario n∆o encontrado"));

        return new UserDetailsImpl(funcionario);
    }
    
}
