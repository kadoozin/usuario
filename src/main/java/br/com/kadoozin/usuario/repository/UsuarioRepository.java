package br.com.kadoozin.usuario.repository;

import br.com.kadoozin.usuario.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);

    @Transactional
    void deleteByEmail(String email);

    Optional<Usuario> findByEmail(String email);
}
