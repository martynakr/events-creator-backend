package io.nology.eventscreatorbackend.label;

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
		return this.labelRepository.findAll();
	}
	
	public Optional<EventLabel> findByName(String name) {
		return this.labelRepository.findByName(name);
	}
	
	public EventLabel create(EventLabelCreateDTO data) {
		EventLabel newLabel = EventLabel.builder().name(data.getName()).build();
		return this.labelRepository.save(newLabel);
	}
}
