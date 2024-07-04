package io.nology.eventscreatorbackend.event;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

	List<Event> findByUserId(Long id);
	Optional<Event> findByIdAndUserId(Long id, Long userId);
}
