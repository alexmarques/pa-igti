package br.com.pa.repository;

import br.com.pa.model.Recibo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecibosRepository extends JpaRepository<Recibo, Long> {
}
