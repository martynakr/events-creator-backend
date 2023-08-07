package io.nology.eventscreatorbackend.label;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLabelRepository extends JpaRepository<EventLabel, Long>{

	Optional<EventLabel> findByName(String name);
}
