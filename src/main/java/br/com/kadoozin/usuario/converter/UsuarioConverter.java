package br.com.kadoozin.usuario.converter;

import br.com.kadoozin.usuario.dto.EnderecoDTO;
import br.com.kadoozin.usuario.dto.TelefoneDTO;
import br.com.kadoozin.usuario.dto.UsuarioDTO;
import br.com.kadoozin.usuario.entities.Endereco;
import br.com.kadoozin.usuario.entities.Telefone;
import br.com.kadoozin.usuario.entities.Usuario;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UsuarioConverter {

    public Usuario converterParaUsuario(UsuarioDTO dto){
        if (dto == null) return null;
        return Usuario.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .sobrenome(dto.getSobrenome())
                .dataNascimento(dto.getDataNascimento())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .enderecos(converterListaEnderecoEntity(dto.getEnderecos()))
                .telefones(converterListaTelefoneEntity(dto.getTelefones()))
                .build();
    }

    public UsuarioDTO converterParaUsuarioDTO(Usuario entity){
        if (entity == null) return null;
        return UsuarioDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .sobrenome(entity.getSobrenome())
                .dataNascimento(entity.getDataNascimento())
                .email(entity.getEmail())
                .senha(entity.getSenha())
                .enderecos(converterListaEnderecoDTO(entity.getEnderecos()))
                .telefones(converterListaTelefoneDTO(entity.getTelefones()))
                .build();
    }

    public List<Endereco> converterListaEnderecoEntity(List<EnderecoDTO> dtos){
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(this::converterParaEnderecoEntity).toList();
    }

    public List<EnderecoDTO> converterListaEnderecoDTO(List<Endereco> entities){
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::converterParaEnderecoDTO).toList();
    }

    public List<Telefone> converterListaTelefoneEntity(List<TelefoneDTO> dtos){
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(this::converterParaTelefoneEntity).toList();
    }

    public List<TelefoneDTO> converterListaTelefoneDTO(List<Telefone> entities){
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::converterParaTelefoneDTO).toList();
    }


    public Endereco converterParaEnderecoEntity(EnderecoDTO dto){
        if (dto == null) return null;
        return Endereco.builder()
                .id(dto.getId())
                .numero(dto.getNumero())
                .rua(dto.getRua())
                .complemento(dto.getComplemento())
                .cep(dto.getCep())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .build();
    }

    public EnderecoDTO converterParaEnderecoDTO(Endereco entity){
        if (entity == null) return null;
        return EnderecoDTO.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .rua(entity.getRua())
                .complemento(entity.getComplemento())
                .cep(entity.getCep())
                .cidade(entity.getCidade())
                .estado(entity.getEstado())
                .build();
    }


    public Telefone converterParaTelefoneEntity(TelefoneDTO dto){
        if (dto == null) return null;
        return Telefone.builder()
                .id(dto.getId())
                .numero(dto.getNumero())
                .ddd(dto.getDdd())
                .build();
    }

    public TelefoneDTO converterParaTelefoneDTO(Telefone entity){
        if (entity == null) return null;
        return TelefoneDTO.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .ddd(entity.getDdd())
                .build();
    }


    public Usuario mergeUsuarioComDTO(UsuarioDTO dto, Usuario entity){
        if (dto == null || entity == null) return entity;

        return Usuario.builder()
                .id(entity.getId())
                .nome(dto.getNome() != null ? dto.getNome() : entity.getNome())
                .sobrenome(dto.getSobrenome() != null ? dto.getSobrenome() : entity.getSobrenome())
                .dataNascimento(dto.getDataNascimento() != null ? dto.getDataNascimento() : entity.getDataNascimento())
                .email(dto.getEmail() != null ? dto.getEmail() : entity.getEmail())
                .senha(entity.getSenha())
                .enderecos(entity.getEnderecos())
                .telefones(entity.getTelefones())
                .build();
    }

    public Endereco mergeEnderecoComDTO(EnderecoDTO dto, Endereco entity){
        if (dto == null || entity == null) return entity;

        return Endereco.builder()
                .id(entity.getId())
                .numero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero())
                .rua(dto.getRua() != null ? dto.getRua() : entity.getRua())
                .complemento(dto.getComplemento() != null ? dto.getComplemento() : entity.getComplemento())
                .cep(dto.getCep() != null ? dto.getCep() : entity.getCep())
                .cidade(dto.getCidade() != null ? dto.getCidade() : entity.getCidade())
                .estado(dto.getEstado() != null ? dto.getEstado() : entity.getEstado())
                .usuario(entity.getUsuario())
                .build();
    }

    public Telefone mergeTelefoneComDTO(TelefoneDTO dto, Telefone entity){
        if (dto == null || entity == null) return entity;

        return Telefone.builder()
                .id(entity.getId())
                .ddd(dto.getDdd() != null ? dto.getDdd() : entity.getDdd())
                .numero(dto.getNumero() != null ? dto.getNumero() : entity.getNumero())
                .usuario(entity.getUsuario())
                .build();
    }
}
