package br.com.kadoozin.usuario.controller;

import br.com.kadoozin.usuario.dto.EnderecoDTO;
import br.com.kadoozin.usuario.dto.TelefoneDTO;
import br.com.kadoozin.usuario.dto.UsuarioDTO;
import br.com.kadoozin.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/criar")
    public ResponseEntity<UsuarioDTO> criaUsuario(@RequestBody UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.criaUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.autentificarUsuario(usuarioDTO));
    }

    @PutMapping("/atualizar/")
    public ResponseEntity<UsuarioDTO> atualizaDadosUsuario(@RequestBody UsuarioDTO usuarioDTO,
                                                           @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.atualizarDadosUsuario(token, usuarioDTO));
    }

    @GetMapping("/buscar/")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorEmail(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorId(id));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarUsuarioPorId(@PathVariable Long id){
        usuarioService.deletarUsuarioPorId(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioDTO>> listarUsuario(){
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @PutMapping("/endereco/")
    public ResponseEntity<EnderecoDTO> atualizaDadosDoEnderecoPorId(@RequestBody EnderecoDTO dto,
                                                                    @RequestParam ("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizaDadosDoEnderecoPorId(id, dto));
    }

    @PutMapping("/telefone/")
    public ResponseEntity<TelefoneDTO> atualizaDadosDoTelefonePorId(@RequestBody TelefoneDTO dto,
                                                                    @RequestParam ("id") Long id){
        return ResponseEntity.ok(usuarioService.atualizaDadosDoTelefonePorId(id, dto));
    }
}
