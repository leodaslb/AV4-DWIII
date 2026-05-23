package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.LoginReqDto;
import com.autobots.automanager.dto.UsuarioReqDto;
import com.autobots.automanager.dto.UsuarioResDto;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.AdicionadorLinkUsuario;
import com.autobots.automanager.services.UsuarioServico;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

    @Autowired
    private UsuarioServico usuarioServico;

    @Autowired
    private AdicionadorLinkUsuario adicionadorLink;

    

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResDto>> obterUsuarios() {
        List<Usuario> usuarios = usuarioServico.selecionarTodos();
        if (usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<UsuarioResDto> dto = usuarioServico.toResDtoList(usuarios);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResDto> obterUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioServico.selecionarPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        UsuarioResDto dto = usuarioServico.toResDto(usuario);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResDto> criarUsuario(@RequestBody UsuarioReqDto dtoReq) {
        Usuario usuario = usuarioServico.criarUsuario(dtoReq);
        UsuarioResDto dtoRes = usuarioServico.toResDto(usuario);
        adicionadorLink.adicionarLink(dtoRes);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoRes);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<UsuarioResDto> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioReqDto dtoReq) {
        Usuario usuario = usuarioServico.atualizar(id, dtoReq);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        UsuarioResDto dtoRes = usuarioServico.toResDto(usuario);
        adicionadorLink.adicionarLink(dtoRes);
        return ResponseEntity.ok(dtoRes);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id) {
        usuarioServico.excluir(id);
        return ResponseEntity.ok().build();
    }
}