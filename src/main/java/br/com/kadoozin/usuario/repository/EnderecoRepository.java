package br.com.kadoozin.usuario.repository;

import br.com.kadoozin.usuario.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
