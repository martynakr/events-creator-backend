package io.nology.eventscreatorbackend.label;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import io.nology.eventscreatorbackend.user.User;


public interface EventLabelRepository extends JpaRepository<EventLabel, Long>{

	Optional<EventLabel> findByName(String name);
	List<EventLabel> findByCreatedBy(User createdBy);
	Optional<EventLabel> findByNameAndCreatedBy(String name, User createdBy);
}
