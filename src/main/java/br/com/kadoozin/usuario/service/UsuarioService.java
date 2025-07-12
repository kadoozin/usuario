package br.com.kadoozin.usuario.service;

import br.com.kadoozin.usuario.converter.UsuarioConverter;
import br.com.kadoozin.usuario.dto.UsuarioDTO;
import br.com.kadoozin.usuario.entities.Usuario;
import br.com.kadoozin.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO criaUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.converterParaUsuario(usuarioDTO);
        return usuarioConverter.converterParaUsuarioDTO(usuarioRepository.save(usuario));
    }
}
