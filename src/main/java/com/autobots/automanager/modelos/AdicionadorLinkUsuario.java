package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.dto.UsuarioResDto;

@Component
public class AdicionadorLinkUsuario implements AdicionadorLink<UsuarioResDto> {

    @Override
    public void adicionarLink(List<UsuarioResDto> lista) {
        for (UsuarioResDto dto : lista) {
            long id = dto.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(UsuarioControle.class)
                            .obterUsuario(id, null))
                    .withSelfRel();
            dto.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(UsuarioResDto objeto) {
   
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioControle.class)
                        .obterUsuario(objeto.getId(), null))
                .withSelfRel();
        objeto.add(linkProprio);

        Link linkLista = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioControle.class)
                        .obterUsuarios())
                .withRel("usuarios");
        objeto.add(linkLista);
    }
}