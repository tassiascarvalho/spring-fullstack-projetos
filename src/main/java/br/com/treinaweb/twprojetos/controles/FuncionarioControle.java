package br.com.treinaweb.twprojetos.controles;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.treinaweb.twprojetos.dto.AlertDTO;
import br.com.treinaweb.twprojetos.entidades.Funcionario;
import br.com.treinaweb.twprojetos.excessoes.FuncionarioPossuiProjetosException;
import br.com.treinaweb.twprojetos.servicos.CargoServico;
import br.com.treinaweb.twprojetos.servicos.FuncionarioServico;
import br.com.treinaweb.twprojetos.validadores.FuncionarioValidador;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioControle {


    @Autowired
    private CargoServico cargoServico;

    @Autowired
    private FuncionarioServico funcionarioServico;

    @Autowired
    private FuncionarioValidador funcionarioValidador;

    //Associar arquivo de validaá∆o
    @InitBinder("funcionario")
    private void initBinder(WebDataBinder binder){
        binder.addValidators(funcionarioValidador);        
    }

    @GetMapping
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("funcionario/home");

        modelAndView.addObject("funcionarios", funcionarioServico.buscarTodos());

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView detalhes(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("funcionario/detalhes");

        modelAndView.addObject("funcionario", funcionarioServico.buscarPorID(id));

        return modelAndView;
    }

    @GetMapping("/cadastrar")
    public ModelAndView cadastrar() {
        ModelAndView modelAndView = new ModelAndView("funcionario/formulario");

        modelAndView.addObject("funcionario", new Funcionario());
        modelAndView.addObject("cargos", cargoServico.buscarTodos());
        
        return modelAndView;
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editar(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("funcionario/formulario");

        modelAndView.addObject("funcionario", funcionarioServico.buscarPorID(id));
        modelAndView.addObject("cargos", cargoServico.buscarTodos());
        
        return modelAndView;
    }

    @PostMapping({"/cadastrar"})
    public String cadastrar(@Valid Funcionario funcionario, BindingResult resultado, ModelMap model, RedirectAttributes attrs){
        if(resultado.hasErrors()){
            model.addAttribute("cargos", cargoServico.buscarTodos());
            return "funcionario/formulario";
        }
        funcionarioServico.cadastrar(funcionario);
        attrs.addFlashAttribute("alert", new AlertDTO("Funcionario cadastrado com Sucesso", "alert-success"));
       
        return "redirect:/funcionarios";
    }

    @PostMapping({"/{id}/editar"})
    public String editar(@Valid Funcionario funcionario, BindingResult resultado, @PathVariable Long id, ModelMap model, RedirectAttributes attrs) {
       
        //Verifica se h† algum erro
        if(resultado.hasErrors()){
            model.addAttribute("cargos", cargoServico.buscarTodos());
            return "funcionario/formulario";
        }

        
        //recupera a senha atual do funcionario. - Foi para camada de controle
        // String senhaAtual = funcionarioRepositorio.getOne(id).getSenha();
        // funcionario.setSenha(senhaAtual);

        funcionarioServico.atualizar(funcionario, id);
        attrs.addFlashAttribute("alert", new AlertDTO("Funcionario alterado com Sucesso", "alert-success"));
        

        return "redirect:/funcionarios";
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes attrs) {
        try{        
            funcionarioServico.excluirPorid(id);
            attrs.addFlashAttribute("alert", new AlertDTO("Funcionario excluido com Sucesso", "alert-success"));
        }catch(FuncionarioPossuiProjetosException e){
            attrs.addFlashAttribute("alert", new AlertDTO("Funcionario n∆o pode ser excluido pois Ç lider de projeto Controle", "alert-danger"));
        }
        return "redirect:/funcionarios";
    }
    
}
