package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.dto.VeiculoResDto;

@Component
public class AdicionadorLinkVeiculo implements AdicionadorLink<VeiculoResDto> {

    @Override
    public void adicionarLink(List<VeiculoResDto> lista) {
        for (VeiculoResDto dto : lista) {
            long id = dto.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(VeiculoControle.class)
                            .obterVeiculo(id))
                    .withSelfRel();
            dto.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(VeiculoResDto objeto) {
        // Link para o próprio recurso (Self)
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VeiculoControle.class)
                        .obterVeiculo(objeto.getId()))
                .withSelfRel();
        objeto.add(linkProprio);

        // Link para a coleção completa (Lista de veículos)
        Link linkLista = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VeiculoControle.class)
                        .obterVeiculos())
                .withRel("veiculos");
        objeto.add(linkLista);

        // Link para o proprietário do veículo (Bônus HATEOAS)
        if (objeto.getProprietarioId() != null) {
            Link linkProprietario = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(UsuarioControle.class)
                            .obterUsuario(objeto.getProprietarioId(), null))
                    .withRel("proprietario");
            objeto.add(linkProprietario);
        }
    }
}