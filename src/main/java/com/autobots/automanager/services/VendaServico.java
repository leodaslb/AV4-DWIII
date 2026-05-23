package com.autobots.automanager.services;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.VendaReqDto;
import com.autobots.automanager.dto.VendaResDto;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import com.autobots.automanager.repositorios.ServicoRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;

@Service
public class VendaServico {

    @Autowired
    private VendaRepositorio vendaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private VeiculoRepositorio veiculoRepositorio;

    @Autowired
    private MercadoriaRepositorio mercadoriaRepositorio;

    @Autowired
    private ServicoRepositorio servicoRepositorio;

    @Autowired
    private VeiculoServico veiculoServico;

    @Autowired
    private MercadoriaServico mercadoriaServico;

    @Autowired
    private ServicoServico servicoServico;

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    private Venda converterVenda(VendaReqDto dto) {
        Venda venda = new Venda();
        venda.setIdentificacao(dto.getIdentificacao());
        venda.setCadastro(Calendar.getInstance().getTime());

        // busca cliente
        if (dto.getClienteId() != null) {
            Usuario cliente = usuarioRepositorio.findById(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            venda.setCliente(cliente);
        }

        
        if (dto.getFuncionarioId() != null) {
            Usuario funcionario = usuarioRepositorio.findById(dto.getFuncionarioId())
                    .orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));
            venda.setFuncionario(funcionario);
        }

        
        if (dto.getVeiculoId() != null) {
            Veiculo veiculo = veiculoRepositorio.findById(dto.getVeiculoId())
                    .orElseThrow(() -> new RuntimeException("Veiculo não encontrado"));
            venda.setVeiculo(veiculo);
        }

        // busca mercadorias pelos ids
        if (dto.getMercadoriaIds() != null && !dto.getMercadoriaIds().isEmpty()) {
            Set<Mercadoria> mercadorias = dto.getMercadoriaIds().stream()
                    .map(id -> mercadoriaRepositorio.findById(id)
                            .orElseThrow(() -> new RuntimeException("Mercadoria não encontrada: " + id)))
                    .collect(Collectors.toSet());
            venda.setMercadorias(mercadorias);
        }

        // busca servicos pelos ids
        if (dto.getServicoIds() != null && !dto.getServicoIds().isEmpty()) {
            Set<Servico> servicos = dto.getServicoIds().stream()
                    .map(id -> servicoRepositorio.findById(id)
                            .orElseThrow(() -> new RuntimeException("Servico não encontrado: " + id)))
                    .collect(Collectors.toSet());
            venda.setServicos(servicos);
        }

        return venda;
    }

    public Venda criarVenda(VendaReqDto dto) {
        
        return vendaRepositorio.save(converterVenda(dto));
    }

    public List<Venda> selecionarTodos() {
        return vendaRepositorio.findAll();
    }

    public Venda selecionarPorId(Long id) {
        return vendaRepositorio.findById(id).orElse(null);
    }

    public Venda atualizar(Long id, VendaReqDto atualizacao) {
        Venda venda = vendaRepositorio.findById(id).orElse(null);
        if (venda != null) {
            if (!verificador.verificar(atualizacao.getIdentificacao()))
                venda.setIdentificacao(atualizacao.getIdentificacao());

            if (atualizacao.getClienteId() != null) {
                Usuario cliente = usuarioRepositorio.findById(atualizacao.getClienteId())
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
                venda.setCliente(cliente);
            }
            if (atualizacao.getFuncionarioId() != null) {
                Usuario funcionario = usuarioRepositorio.findById(atualizacao.getFuncionarioId())
                        .orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));
                venda.setFuncionario(funcionario);
            }
            if (atualizacao.getVeiculoId() != null) {
                Veiculo veiculo = veiculoRepositorio.findById(atualizacao.getVeiculoId())
                        .orElseThrow(() -> new RuntimeException("Veiculo não encontrado"));
                venda.setVeiculo(veiculo);
            }
            if (atualizacao.getMercadoriaIds() != null && !atualizacao.getMercadoriaIds().isEmpty()) {
                Set<Mercadoria> mercadorias = atualizacao.getMercadoriaIds().stream()
                        .map(mid -> mercadoriaRepositorio.findById(mid)
                                .orElseThrow(() -> new RuntimeException("Mercadoria não encontrada")))
                        .collect(Collectors.toSet());
                venda.setMercadorias(mercadorias);
            }
            if (atualizacao.getServicoIds() != null && !atualizacao.getServicoIds().isEmpty()) {
                Set<Servico> servicos = atualizacao.getServicoIds().stream()
                        .map(sid -> servicoRepositorio.findById(sid)
                                .orElseThrow(() -> new RuntimeException("Servico não encontrado")))
                        .collect(Collectors.toSet());
                venda.setServicos(servicos);
            }
            return vendaRepositorio.save(venda);
        }
        return null;
    }

    public void excluir(Long id) {
        Venda venda = vendaRepositorio.findById(id).orElse(null);
        if (venda != null)
            vendaRepositorio.delete(venda);
    }

    public VendaResDto toResDto(Venda venda) {
        if (venda == null) return null;
        VendaResDto dto = new VendaResDto();
        dto.setId(venda.getId());
        dto.setCadastro(venda.getCadastro());
        dto.setIdentificacao(venda.getIdentificacao());
        if (venda.getCliente() != null)
            dto.setClienteId(venda.getCliente().getId());
        if (venda.getFuncionario() != null)
            dto.setFuncionarioId(venda.getFuncionario().getId());
        if (venda.getVeiculo() != null)
            dto.setVeiculo(veiculoServico.toResDto(venda.getVeiculo()));
        if (venda.getMercadorias() != null)
            dto.setMercadorias(venda.getMercadorias().stream()
                    .map(mercadoriaServico::toResDto).collect(Collectors.toSet()));
        if (venda.getServicos() != null)
            dto.setServicos(venda.getServicos().stream()
                    .map(servicoServico::toResDto).collect(Collectors.toSet()));
        return dto;
    }

    public List<VendaResDto> toResDtoList(List<Venda> vendas) {
        return vendas.stream().map(this::toResDto).collect(Collectors.toList());
    }
}
