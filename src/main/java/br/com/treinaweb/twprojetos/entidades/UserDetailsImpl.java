package br.com.treinaweb.twprojetos.entidades;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.treinaweb.twprojetos.enums.Perfil;

public class UserDetailsImpl implements UserDetails{
    //Classe para Gerenciar o Usuario

    private Funcionario funcionario;

    public UserDetailsImpl(Funcionario funcionario){
        this.funcionario = funcionario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Saber as partes de autorizaá∆o
        // Defini se o Usuario e o seu tipo de acordo com seu cargo
        // L¢gica para definir o perfil de acordo com o cargo
        Perfil perfil = 
            funcionario.getCargo().getNome().equals("Gerente") 
            ? Perfil.ADMIN : Perfil.USER;

        return AuthorityUtils.createAuthorityList(perfil.toString());
    }

    @Override
    public String getPassword() {        
        return funcionario.getSenha();
    }

    @Override
    public String getUsername() {        
        return funcionario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        //Caso usuario tenha data de demiss∆o n∆o loga
        return funcionario.getDataDemissao() == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
