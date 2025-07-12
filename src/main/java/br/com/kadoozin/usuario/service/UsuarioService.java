package br.com.kadoozin.usuario.service;

import br.com.kadoozin.usuario.converter.UsuarioConverter;
import br.com.kadoozin.usuario.dto.UsuarioDTO;
import br.com.kadoozin.usuario.entities.Usuario;
import br.com.kadoozin.usuario.exceptions.ConflictException;
import br.com.kadoozin.usuario.exceptions.ResourceNotFoundException;
import br.com.kadoozin.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO criaUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.converterParaUsuario(usuarioDTO);
        return usuarioConverter.converterParaUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email){
        if (verificaEmailExistente(email)){
            throw new ConflictException("Email já cadastrado: " +  email);
        }
    }

    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorId(Long id) {
        return usuarioConverter.converterParaUsuarioDTO(
                usuarioRepository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Usuario de ID: " + id + " nao foi encontrado!")
                        )
        );
    }

    public void deletarUsuarioPorId(Long id){
        usuarioRepository.deleteById(id);
    }

    public List<UsuarioDTO> listarUsuarios(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuarioConverter :: converterParaUsuarioDTO)
                .collect(Collectors.toList());
    }


}
