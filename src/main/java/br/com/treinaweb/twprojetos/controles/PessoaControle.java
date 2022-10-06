package br.com.treinaweb.twprojetos.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import br.com.treinaweb.twprojetos.enums.UF;
import br.com.treinaweb.twprojetos.validadores.PessoaValidador;

//Classe controleadvice para refatorar o c¢digo e centralizar 
//Quando queremos compartilhar um dado entre diferentes controllers da aplica‡Æo adicionar anota‡Æo ControllerAdvice
@ControllerAdvice(assignableTypes = {FuncionarioControle.class, ClienteControle.class})
 //Todas as classe passsam a ter acesso a esse Controle, mas pode especificar
public class PessoaControle {

    @Autowired
    private PessoaValidador pessoaValidador;

    //InitBinder - valida‡Æo com anota‡äes
    //no value sÆo os objetos
    @InitBinder(value ={"funcionario", "cliente"})
    public void initBinder(WebDataBinder binder){
        binder.addValidators(pessoaValidador);
    }

    @ModelAttribute("ufs") 
    //m‚todo para criar a disponibiliza‡Æo dos estados j  que deve ser feito v rias vezes no sistema
    //fun‡Æo para retornar os estados tanto em cliente quanto em funcionario
    public UF[] getUfs(){
        return UF.values();
    }
}
