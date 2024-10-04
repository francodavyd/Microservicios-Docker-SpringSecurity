package com.francodavyd.repository;

import com.francodavyd.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByPreferenceId(String preferenceId);
}
