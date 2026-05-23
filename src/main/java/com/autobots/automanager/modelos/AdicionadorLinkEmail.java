
package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmailControle;
import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.dto.EmailResDto;

@Component
public class AdicionadorLinkEmail implements AdicionadorLink<EmailResDto> {

    @Override
    public void adicionarLink(List<EmailResDto> lista) {
        
    }

    @Override
    public void adicionarLink(EmailResDto objeto) {
       
    }

    
    public void adicionarLink(List<EmailResDto> lista, Long usuarioId) {
        if (lista != null) {
            for (EmailResDto dto : lista) {
                this.adicionarLink(dto, usuarioId);
            }
        }
    }

   
    public void adicionarLink(EmailResDto objeto, Long usuarioId) {
        
        
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmailControle.class)
                        .obterEmail(usuarioId, objeto.getId()))
                .withSelfRel();
        objeto.add(linkProprio);

       
        Link linkUsuario = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioControle.class)
                        .obterUsuario(usuarioId, null))
                .withRel("usuario-dono");
        objeto.add(linkUsuario);
    }
}