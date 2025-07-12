package br.com.kadoozin.usuario.repository;

import br.com.kadoozin.usuario.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
