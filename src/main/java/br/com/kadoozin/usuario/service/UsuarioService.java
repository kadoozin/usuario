package br.com.kadoozin.usuario.service;

import br.com.kadoozin.usuario.converter.UsuarioConverter;
import br.com.kadoozin.usuario.dto.EnderecoDTO;
import br.com.kadoozin.usuario.dto.TelefoneDTO;
import br.com.kadoozin.usuario.dto.UsuarioDTO;
import br.com.kadoozin.usuario.entities.Endereco;
import br.com.kadoozin.usuario.entities.Telefone;
import br.com.kadoozin.usuario.entities.Usuario;
import br.com.kadoozin.usuario.exceptions.ConflictException;
import br.com.kadoozin.usuario.exceptions.ResourceNotFoundException;
import br.com.kadoozin.usuario.exceptions.UnauthorizedException;
import br.com.kadoozin.usuario.repository.EnderecoRepository;
import br.com.kadoozin.usuario.repository.TelefoneRepository;
import br.com.kadoozin.usuario.repository.UsuarioRepository;
import br.com.kadoozin.usuario.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO criaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.converterParaUsuario(usuarioDTO);
        return usuarioConverter.converterParaUsuarioDTO(usuarioRepository.save(usuario));
    }

    public String autentificarUsuario(UsuarioDTO usuarioDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            usuarioDTO.getEmail(),
                            usuarioDTO.getSenha()
                    )
            );
            return "Bearer " + jwtUtil.generateToken(authentication.getName());
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new UnauthorizedException("Usuario ou senha inválidos", e);
        }
    }

    public void emailExiste(String email) {
        if (verificaEmailExistente(email)) {
            throw new ConflictException("Email já cadastrado: " + email);
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        return usuarioConverter.converterParaUsuarioDTO(
                usuarioRepository.findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("Este email é inválido ou inexistente"))
        );
    }

    public UsuarioDTO buscarUsuarioPorId(Long id) {
        return usuarioConverter.converterParaUsuarioDTO(
                usuarioRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Usuario de ID: " + id + " nao foi encontrado!"))
        );
    }

    public void deletarUsuarioPorId(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<UsuarioDTO> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuarioConverter::converterParaUsuarioDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO atualizarDadosUsuario(String token, UsuarioDTO dto) {

        //Busca o email do usuario através do token (tira a obrigatoriedade do email)
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        //Busca os dados do usuario no bando de dados!
        Usuario usuarioEntity = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado"));

        //Mesclou os dados que recebemos na requisição DTO com os dados do bando de dados!
        Usuario usuario = usuarioConverter.mergeUsuarioComDTO(dto, usuarioEntity);

        //Criptografia de senha
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        //Salva no banco e retorna como DTO
        return usuarioConverter.converterParaUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaDadosDoEnderecoPorId(Long id, EnderecoDTO enderecoDTO) {
        Endereco entity = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado " + id));
        Endereco endereco = usuarioConverter.mergeEnderecoComDTO(enderecoDTO, entity);
        return usuarioConverter.converterParaEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaDadosDoTelefonePorId(Long id, TelefoneDTO dto) {
        Telefone entity = telefoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado " + id));
        Telefone telefone = usuarioConverter.mergeTelefoneComDTO(dto, entity);
        return usuarioConverter.converterParaTelefoneDTO(telefoneRepository.save(telefone));
    }


}
