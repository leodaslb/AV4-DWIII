package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.VendaControle;
import com.autobots.automanager.dto.VendaResDto;

@Component
public class AdicionadorLinkVenda implements AdicionadorLink<VendaResDto> {

    @Override
    public void adicionarLink(List<VendaResDto> lista) {
        for (VendaResDto dto : lista) {
            long id = dto.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(VendaControle.class)
                            .obterVenda(id, null))
                    .withSelfRel();
            dto.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(VendaResDto objeto) {
       
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VendaControle.class)
                        .obterVenda(objeto.getId(), null))
                .withSelfRel();
        objeto.add(linkProprio);

        
        Link linkLista = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VendaControle.class)
                        .obterVendas())
                .withRel("vendas");
        objeto.add(linkLista);
    }
}