package br.com.treinaweb.twprojetos.controles;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.treinaweb.twprojetos.dto.AlertDTO;
import br.com.treinaweb.twprojetos.entidades.Cargo;
import br.com.treinaweb.twprojetos.excessoes.CargoPossuiFuncionariosException;
import br.com.treinaweb.twprojetos.servicos.CargoServico;

//As anota��es tamb�m definem o formato da inje��o de dependecia
//As anota��es tamb�m permitem instanciar as classes especificas
//As anota��es das classes beans spring definem como acontecer� a inje��o de dependencia
// @Controller (Beans Spring)
// @Service - Define as classes da chamada de servico (Beans Spring)
// @Repository - S�o utilizados em classes responsaveis pela parte de acesso
// @Component - � utilizado para definir beans spring que n�o fazerm parte das camadas anteriores, classes mais genericas, geralmente utilizada em classes utils

// @Bean - N�o � utilizado em classe, apenas anota m�todos
// server para ensinar o spring mvc gerenciar classes das quais ele n�o tem conhecimento
// exemplo classes de bibliotecas externas

@Controller
@RequestMapping("/cargos")
public class CargoControle {

    // @Autowired//Inje��o de Depend�ncia.
    // Existem varias formas de fazer inje��o de depend�ncia, ou colocando
    // o Autorired em uma private, ou chamando o set

    @Autowired // Inje�a� de dependencia por camada classe
    private CargoServico cargoServico; // Camada para implementar os metodos;

    // //Inje��o de dependencia por set
    // @Autowired
    // public void setCargoServico(CargoServico cargoServico) {
    // this.cargoServico = cargoServico;
    // }

    // Inje��o de dependencia por Construtor
    // � mais indicado em Testes Automatizados
    // public CargoControle(CargoServico cargoServico) {
    // this.cargoServico = cargoServico;
    // }

    // As tr�s formas de inje��o de dependencia funcionam
    // Escolha uma e adote padr�o.

    @GetMapping
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("cargo/home");

        modelAndView.addObject("cargos", cargoServico.buscarTodos());

        return modelAndView;
    }

    @GetMapping("/cadastrar")
    public ModelAndView cadastrar() {
        ModelAndView modelAndView = new ModelAndView("cargo/formulario");

        modelAndView.addObject("cargo", new Cargo());

        return modelAndView;
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editar(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("cargo/formulario");

        modelAndView.addObject("cargo", cargoServico.buscarPorID(id));

        return modelAndView;
    }

    @PostMapping({ "/cadastrar", "/{id}/editar" })
    public String salvar(@Valid Cargo cargo, BindingResult resultado, RedirectAttributes attrs,
            @PathVariable(required = false) Long id) {
        // anota��o @Valid � para validar de acordo com a classe, e o bindingresulte
        // retornar os erros, deve sempre vir logo apos o atributo de valida��o

        if (resultado.hasErrors()) {
            return "cargo/formulario";
        }

        if (cargo.getId() == null) {
            cargoServico.cadastrar(cargo);
            attrs.addFlashAttribute("alert", new AlertDTO("Cargo cadastrado com Sucesso", "alert-success"));
        } else {
            cargoServico.atualizar(cargo, id);
            attrs.addFlashAttribute("alert", new AlertDTO("Cargo alterado com Sucesso", "alert-success"));
        }

        return "redirect:/cargos";
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            cargoServico.excluirPorid(id);
            attrs.addFlashAttribute("alert", new AlertDTO("Cargo excluido com Sucesso", "alert-success"));
        } catch (CargoPossuiFuncionariosException e) {
            attrs.addFlashAttribute("alert",
                    new AlertDTO("Cargo n�o pode ser excluido, pois possui funcion�rios associados", "alert-danger"));
        }

        return "redirect:/cargos";
    }

}
