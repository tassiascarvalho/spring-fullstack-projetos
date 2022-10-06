package br.com.treinaweb.twprojetos.controles;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import br.com.treinaweb.twprojetos.entidades.Cliente;
import br.com.treinaweb.twprojetos.excessoes.ClientePossuiProjetosException;
import br.com.treinaweb.twprojetos.servicos.ClienteServico;
import br.com.treinaweb.twprojetos.validadores.ClienteValidador;

@Controller
@RequestMapping("/clientes")
public class ClienteControle {

    @Autowired
    private ClienteServico clienteServico;

    @Autowired
    private ClienteValidador clienteValidador; // provido pelo beans ValdiadorConfig

    // inje��o para cliente repositorio ser utilizado no clientevalidador
    @InitBinder("cliente")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(clienteValidador);
    }

    @GetMapping
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("cliente/home");

        modelAndView.addObject("clientes", clienteServico.buscarTodos());

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView detalhes(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("cliente/detalhes");

        modelAndView.addObject("cliente", clienteServico.buscarPorID(id));

        return modelAndView;
    }

    @GetMapping("/cadastrar")
    public ModelAndView cadastrar() {
        ModelAndView modelAndView = new ModelAndView("cliente/formulario");

        modelAndView.addObject("cliente", new Cliente());
        return modelAndView;
    }

    @GetMapping("/{id}/editar")
    public ModelAndView editar(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("cliente/formulario");

        modelAndView.addObject("cliente", clienteServico.buscarPorID(id));

        return modelAndView;
    }

    @PostMapping({ "/cadastrar", "/{id}/editar" })
    public String salvar(@Valid Cliente cliente, BindingResult resultado, RedirectAttributes attr,
            @PathVariable(required = false) Long id) {
        // Verifica se teve erros na valida��o
        if (resultado.hasErrors()) {
            // deixei o exemplo para quando h� necessidade de carregar outros objetos
            // model.addAttribute("ufs", UF.values());//carregar os estados em caso de erros
            return "cliente/formulario";
        }

        // RedirectAttributes envia atributos para outras action/m�todos
        if (cliente.getId() == null) {
            clienteServico.cadastrar(cliente);
            attr.addFlashAttribute("alert", new AlertDTO("Cliente cadastrado com Sucesso", "alert-success"));
        } else {
            clienteServico.atualizar(cliente, id);
            attr.addFlashAttribute("alert", new AlertDTO("Cliente alterado com Sucesso", "alert-success"));
        }

        return "redirect:/clientes";
    }

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes attrs) {
        try {
            clienteServico.excluirPorid(id);
            attrs.addFlashAttribute("alert", new AlertDTO("Cliente excluido com Sucesso", "alert-success"));
        } catch (ClientePossuiProjetosException e) {
            attrs.addFlashAttribute("alert",
                    new AlertDTO("Cliente n�o pode ser exlu�do pois possui projetos associados a ele", "alert-danger"));
        }

        return "redirect:/clientes";
    }

}
