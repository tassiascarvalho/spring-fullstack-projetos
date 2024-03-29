package br.com.treinaweb.twprojetos.controles;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.treinaweb.twprojetos.dao.AlterarSenhaDAO;
import br.com.treinaweb.twprojetos.dto.AlertDTO;
import br.com.treinaweb.twprojetos.entidades.Funcionario;
import br.com.treinaweb.twprojetos.repositorios.FuncionarioRepositorio;
import br.com.treinaweb.twprojetos.utils.SenhaUtils;

@Controller
public class UsuarioControle {
    
    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;

    @GetMapping("/login")
    public String login(){
        return "usuario/login";
    }

    @GetMapping("/perfil")
    public ModelAndView perfil(Principal principal){//principal representa o usuario autentica o spring security
        ModelAndView modelAndView = new ModelAndView("usuario/perfil");

        Funcionario usuario = funcionarioRepositorio.findByEmail(principal.getName()).get();

        modelAndView.addObject("usuario", usuario);
        modelAndView.addObject("alterarSenhaForm", new AlterarSenhaDAO());

        return modelAndView;
    }

    @PostMapping("/alterar-senha")
    public String alterarSenha(AlterarSenhaDAO form, Principal principal, RedirectAttributes attrs){
        Funcionario usuario = funcionarioRepositorio.findByEmail(principal.getName()).get();
        //Alterar a senha e j� criptografar com m�todo hash
        if(SenhaUtils.matches(form.getSenhaAtual(), usuario.getSenha())){
            usuario.setSenha(SenhaUtils.encode(form.getNovaSenha()));
            funcionarioRepositorio.save(usuario);     
            
            attrs.addFlashAttribute("alert", new AlertDTO("Senha alterada com Sucesso", "alert-success"));

        }else{
            attrs.addFlashAttribute("alert", new AlertDTO("Senha atual incorreta", "alert-danger"));
        }

        return ("redirect:/perfil");

    }




}
