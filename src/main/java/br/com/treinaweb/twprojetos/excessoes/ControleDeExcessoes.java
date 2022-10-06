package br.com.treinaweb.twprojetos.excessoes;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice //Indicar que outras classes podem usar
//Como n∆o especifiquei todas as classes acessam
public class ControleDeExcessoes implements ErrorViewResolver {

    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        // Determina o que deve ser retornado quando o erro acontece.
        ModelAndView modelAndView = new ModelAndView("problema");

        modelAndView.addObject("status", status.value());
        switch(status.value()){
            case 404: 
                modelAndView.addObject("titulo", "P†gina N∆o Encontrada.");
                modelAndView.addObject("mensagem", "A p†gina que vocà procura n∆o existe");
                modelAndView.addObject("causa", "A url para p†gina "+ model.get("path") + " n∆o existe.");
                modelAndView.addObject("cssClass", "text-warning");
                break;
            case 500:
                modelAndView.addObject("titulo", "Erro Interno no Servidor.");
                modelAndView.addObject("mensagem", "Algo deu errado, procure o suporte");
                modelAndView.addObject("causa", "Ocorreu um erro inesperado, tente mais tarde ");
                modelAndView.addObject("cssClass", "text-danger");
                break;
        }

        return modelAndView;
    }
    
}
