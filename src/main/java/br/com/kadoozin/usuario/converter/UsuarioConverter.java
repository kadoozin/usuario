package br.com.kadoozin.usuario.converter;

import br.com.kadoozin.usuario.dto.EnderecoDTO;
import br.com.kadoozin.usuario.dto.TelefoneDTO;
import br.com.kadoozin.usuario.dto.UsuarioDTO;
import br.com.kadoozin.usuario.entities.Endereco;
import br.com.kadoozin.usuario.entities.Telefone;
import br.com.kadoozin.usuario.entities.Usuario;
import br.com.kadoozin.usuario.repository.TelefoneRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {

    private final TelefoneRepository telefoneRepository;

    public UsuarioConverter(TelefoneRepository telefoneRepository) {
        this.telefoneRepository = telefoneRepository;
    }

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
        return enderecoDTOS.stream().map(this::converterParaEnderecoDTO).toList();
    }

    public Endereco converterParaEnderecoDTO(EnderecoDTO enderecoDTO){
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
        return enderecos.stream()
                .map(this::converterParaEnderecoDTO)
                .toList();
    }

    public EnderecoDTO converterParaEnderecoDTO(Endereco endereco){
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

    public Usuario mergeUsuarioComDTO(UsuarioDTO usuarioDTO, Usuario entity){
        return Usuario.builder()
                .id(entity.getId())
                .nome(usuarioDTO.getNome() != null ? usuarioDTO.getNome() : entity.getNome())
                .sobrenome(usuarioDTO.getSobrenome() != null ? usuarioDTO.getSobrenome() : entity.getSobrenome())
                .dataNascimento(usuarioDTO.getDataNascimento() != null ? usuarioDTO.getDataNascimento() : entity.getDataNascimento())
                .email(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : entity.getEmail())
                .senha(entity.getSenha())
                .enderecos(entity.getEnderecos())
                .telefones(entity.getTelefones())
                .build();
    }

    public Endereco mergeEnderecoComDTO(EnderecoDTO enderecoDTO, Endereco enderecoEntity){
        return Endereco.builder()
                .id(enderecoEntity.getId())
                .numero(enderecoDTO.getNumero() != null ? enderecoDTO.getNumero() : enderecoEntity.getNumero())
                .rua(enderecoDTO.getRua() != null ? enderecoDTO.getRua() : enderecoEntity.getRua())
                .complemento(enderecoDTO.getComplemento() != null ? enderecoDTO.getComplemento() : enderecoEntity.getComplemento())
                .cep(enderecoDTO.getCep() != null ? enderecoDTO.getCep() : enderecoEntity.getCep())
                .cidade(enderecoDTO.getCidade() != null ? enderecoDTO.getCidade() : enderecoEntity.getCidade())
                .estado(enderecoDTO.getEstado() != null ? enderecoDTO.getEstado() : enderecoEntity.getEstado())
                .build();
    }

    public Telefone mergeTelefoneComDTO(TelefoneDTO telefoneDTO, Telefone telefoneEntity){
        return Telefone.builder()
                .id(telefoneEntity.getId())
                .ddd(telefoneDTO.getDdd() != null ? telefoneDTO.getDdd() : telefoneEntity.getDdd())
                .numero(telefoneDTO.getNumero() != null ? telefoneDTO.getNumero() : telefoneEntity.getNumero())
                .build();
    }
}
