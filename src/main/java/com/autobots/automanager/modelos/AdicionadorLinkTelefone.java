package com.autobots.automanager.modelos;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;


import com.autobots.automanager.controles.TelefoneControle;
import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.dto.TelefoneResDto;

@Component
public class AdicionadorLinkTelefone implements AdicionadorLink<TelefoneResDto> {

    @Override
    public void adicionarLink(List<TelefoneResDto> lista) {
       
    }

    
    public void adicionarLink(TelefoneResDto objeto, Long clienteId) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(TelefoneControle.class)
                        .selecionarTelefone(clienteId, objeto.getId()))
                .withSelfRel();
        objeto.add(linkProprio);
		Link listaClientes = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
            .methodOn(UsuarioControle.class).obterUsuario(clienteId, null))
			.withRel("todos-clientes");
    		objeto.add(listaClientes);
    }

    @Override
    public void adicionarLink(TelefoneResDto objeto) {
       
    }
}