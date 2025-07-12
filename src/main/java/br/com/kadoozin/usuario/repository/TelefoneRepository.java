package br.com.kadoozin.usuario.repository;

import br.com.kadoozin.usuario.entities.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}
