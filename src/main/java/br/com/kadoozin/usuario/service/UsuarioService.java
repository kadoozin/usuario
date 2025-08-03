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
        usuario.getEnderecos().forEach(end -> end.setUsuario(usuario));
        usuario.getTelefones().forEach(tel -> tel.setUsuario(usuario));
        return usuarioConverter.converterParaUsuarioDTO(usuarioRepository.save(usuario));
    }

    public String autentificarUsuario(UsuarioDTO usuarioDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha())
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
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email inválido ou inexistente!"));
        return usuarioConverter.converterParaUsuarioDTO(usuario);
    }

    public UsuarioDTO buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario de ID: " + id + " não foi encontrado!"));
        return usuarioConverter.converterParaUsuarioDTO(usuario);
    }

    public void deletarUsuarioPorId(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuarioConverter::converterParaUsuarioDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO atualizarDadosUsuario(String token, UsuarioDTO dto) {
        String email = jwtUtil.extrairEmailDoToken(token.substring(7));
        Usuario usuarioEntity = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado!"));

        Usuario usuario = usuarioConverter.mergeUsuarioComDTO(dto, usuarioEntity);

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        usuario.getEnderecos().forEach(end -> end.setUsuario(usuario));
        usuario.getTelefones().forEach(tel -> tel.setUsuario(usuario));

        return usuarioConverter.converterParaUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaDadosDoEnderecoPorId(Long id, EnderecoDTO enderecoDTO) {
        Endereco entity = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado!"));
        Endereco endereco = usuarioConverter.mergeEnderecoComDTO(enderecoDTO, entity);
        endereco.setUsuario(entity.getUsuario()); // garante usuário
        return usuarioConverter.converterParaEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaDadosDoTelefonePorId(Long id, TelefoneDTO dto) {
        Telefone entity = telefoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id não encontrado!"));
        Telefone telefone = usuarioConverter.mergeTelefoneComDTO(dto, entity);
        telefone.setUsuario(entity.getUsuario()); // garante usuário
        return usuarioConverter.converterParaTelefoneDTO(telefoneRepository.save(telefone));
    }

    public EnderecoDTO adicionaNovoEndereco(String token, EnderecoDTO dto) {
        String email = jwtUtil.extrairEmailDoToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado!"));
        Endereco endereco = usuarioConverter.converterParaEnderecoEntity(dto);
        endereco.setUsuario(usuario);
        return usuarioConverter.converterParaEnderecoDTO(enderecoRepository.save(endereco));
    }

    public void deletarEnderecoPorId(String token, Long enderecoId) {
        String email = jwtUtil.extrairEmailDoToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));
        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado!"));
        if (!endereco.getUsuario().getId().equals(usuario.getId())) {
            throw new UnauthorizedException("Endereço não pertence ao usuário logado!");
        }
        enderecoRepository.delete(endereco);
    }


    public TelefoneDTO adicionaNovoTelefone(String token, TelefoneDTO dto) {
        String email = jwtUtil.extrairEmailDoToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado!"));
        Telefone telefone = usuarioConverter.converterParaTelefoneEntity(dto);
        telefone.setUsuario(usuario);
        return usuarioConverter.converterParaTelefoneDTO(telefoneRepository.save(telefone));
    }

    public void deletarTelefonePorId(String token, Long telefoneId){
        String email = jwtUtil.extrairEmailDoToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));
        Telefone telefone = telefoneRepository.findById(telefoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Telefone não encontrado!"));
        if (!telefone.getUsuario().getId().equals(usuario.getId())){
            throw new UnauthorizedException("Telefone não pertence ao usuário logado!");
        }
        telefoneRepository.delete(telefone);
    }
}
