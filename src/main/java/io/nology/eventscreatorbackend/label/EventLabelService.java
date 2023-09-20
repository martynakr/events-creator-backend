package io.nology.eventscreatorbackend.label;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class EventLabelService {

	@Autowired
	private EventLabelRepository labelRepository;
	
	public List<EventLabel> findAll() {

		// return all for the currently logged in user
		return this.labelRepository.findAll();
	}
	
	public Optional<EventLabel> findByName(String name) {

		// same here only for current user
		return this.labelRepository.findByName(name);
	}
	
	public EventLabel create(EventLabelCreateDTO data) {
		// check if that label exists for the current user, only if not, create it
		EventLabel newLabel = EventLabel.builder().name(data.getName()).build();
		return this.labelRepository.save(newLabel);
	}
}
