package io.nology.eventscreatorbackend.label;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import io.nology.eventscreatorbackend.user.User;


public interface LabelRepository extends JpaRepository<Label, Long>{

	Optional<Label> findByName(String name);
	List<Label> findByCreatedBy(User createdBy);
	Optional<Label> findByNameAndCreatedBy(String name, User createdBy);
}
