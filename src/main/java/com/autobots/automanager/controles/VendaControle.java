package com.autobots.automanager.controles;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.dto.VendaReqDto;
import com.autobots.automanager.dto.VendaResDto;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.AdicionadorLinkVenda;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.services.VendaServico;

@RestController
@RequestMapping("/venda")
public class VendaControle {

    @Autowired
    private VendaServico vendaServico;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private AdicionadorLinkVenda adicionadorLink;

    @GetMapping("/vendas")
@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
public ResponseEntity<List<VendaResDto>> obterVendas() {
    List<Venda> vendas = vendaServico.selecionarTodos();
    if (vendas.isEmpty()) {
        return ResponseEntity.notFound().build();
    }
    List<VendaResDto> dto = vendaServico.toResDtoList(vendas);
    adicionadorLink.adicionarLink(dto);
    return ResponseEntity.ok(dto);
}

@GetMapping("/{id}")
public ResponseEntity<VendaResDto> obterVenda(@PathVariable Long id, Principal principal) {
    Venda venda = vendaServico.selecionarPorId(id);
    if (venda == null) {
        return ResponseEntity.notFound().build();
    }

    Usuario usuarioLogado = usuarioRepositorio
        .findByCredencialNomeUsuario(principal.getName());

    boolean isVendedor = usuarioLogado.getPerfis().contains(Perfil.ROLE_VENDEDOR);
    boolean isCliente = usuarioLogado.getPerfis().contains(Perfil.ROLE_CLIENTE);

    if (isVendedor && !venda.getFuncionario().getId().equals(usuarioLogado.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    if (isCliente && !venda.getCliente().getId().equals(usuarioLogado.getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    VendaResDto dto = vendaServico.toResDto(venda);
    adicionadorLink.adicionarLink(dto);
    return ResponseEntity.ok(dto);
}

    @PostMapping("/cadastrar")
    public ResponseEntity<VendaResDto> criarVenda(@RequestBody VendaReqDto dtoReq) {
        Venda venda = vendaServico.criarVenda(dtoReq);
        VendaResDto dtoRes = vendaServico.toResDto(venda);
        adicionadorLink.adicionarLink(dtoRes);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoRes);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<VendaResDto> atualizarVenda(@PathVariable Long id, @RequestBody VendaReqDto dtoReq) {
        Venda venda = vendaServico.atualizar(id, dtoReq);
        if (venda == null) {
            return ResponseEntity.notFound().build();
        }
        VendaResDto dtoRes = vendaServico.toResDto(venda);
        adicionadorLink.adicionarLink(dtoRes);
        return ResponseEntity.ok(dtoRes);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> excluirVenda(@PathVariable Long id) {
        vendaServico.excluir(id);
        return ResponseEntity.ok().build();
    }
}