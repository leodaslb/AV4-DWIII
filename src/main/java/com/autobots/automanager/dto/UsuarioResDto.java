package com.autobots.automanager.dto;

import java.util.HashSet;
import java.util.Set;

import com.autobots.automanager.modelos.Perfil;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class UsuarioResDto extends RepresentationModel<UsuarioResDto> {
    private Long id;
    private String nome;
    private String nomeSocial;
    private Set<Perfil> perfis = new HashSet<>();
    private EnderecoResDto endereco;
    private Set<TelefoneResDto> telefones = new HashSet<>();
    private Set<DocumentoResDto> documentos = new HashSet<>();
    private Set<EmailResDto> emails = new HashSet<>();
}
