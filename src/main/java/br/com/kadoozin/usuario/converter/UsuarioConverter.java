package br.com.kadoozin.usuario.converter;

import br.com.kadoozin.usuario.dto.EnderecoDTO;
import br.com.kadoozin.usuario.dto.TelefoneDTO;
import br.com.kadoozin.usuario.dto.UsuarioDTO;
import br.com.kadoozin.usuario.entities.Endereco;
import br.com.kadoozin.usuario.entities.Telefone;
import br.com.kadoozin.usuario.entities.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {
    public Usuario converterParaUsuario(UsuarioDTO usuarioDTO){
        return Usuario.builder()
                .id(usuarioDTO.getId())
                .nome(usuarioDTO.getNome())
                .sobrenome(usuarioDTO.getSobrenome())
                .dataNascimento(usuarioDTO.getDataNascimento())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(converterListaEnderecos(usuarioDTO.getEnderecos()))
                .telefones(converterListaTelefones(usuarioDTO.getTelefones()))
                .build();
    }

    public List<Endereco> converterListaEnderecos (List<EnderecoDTO> enderecoDTOS){
        return enderecoDTOS.stream().map(this::converterParaEndereco).toList();
    }

    public Endereco converterParaEndereco(EnderecoDTO enderecoDTO){
        return Endereco.builder()
                .id(enderecoDTO.getId())
                .numero(enderecoDTO.getNumero())
                .rua(enderecoDTO.getRua())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .cidade(enderecoDTO.getCidade())
                .estado(enderecoDTO.getEstado())
                .build();
    }

    public List<Telefone> converterListaTelefones(List<TelefoneDTO> telefoneDTOS){
        return telefoneDTOS.stream().map(this::converterParaTelefone).toList();
    }

    public Telefone converterParaTelefone(TelefoneDTO telefoneDTO){
        return Telefone.builder()
                .id(telefoneDTO.getId())
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

    public UsuarioDTO converterParaUsuarioDTO(Usuario usuario){
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .sobrenome(usuario.getSobrenome())
                .dataNascimento(usuario.getDataNascimento())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .enderecos(converterListaEnderecosDTO(usuario.getEnderecos()))
                .telefones(converterListaTelefoneDTO(usuario.getTelefones()))
                .build();
    }

    public List<EnderecoDTO> converterListaEnderecosDTO(List<Endereco> enderecos){
        return enderecos.stream().map(this::converterParaEndereco).toList();
    }

    public EnderecoDTO converterParaEndereco(Endereco endereco){
        return EnderecoDTO.builder()
                .id(endereco.getId())
                .numero(endereco.getNumero())
                .rua(endereco.getRua())
                .complemento(endereco.getComplemento())
                .cep(endereco.getCep())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .build();
    }

    public List<TelefoneDTO> converterListaTelefoneDTO(List<Telefone> telefones){
        return telefones.stream().map(this::converterParaTelefoneDTO).toList();
    }

    public TelefoneDTO converterParaTelefoneDTO (Telefone telefone){
        return TelefoneDTO.builder()
                .id(telefone.getId())
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }
}
