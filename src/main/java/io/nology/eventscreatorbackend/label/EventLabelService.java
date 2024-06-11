package io.nology.eventscreatorbackend.label;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.nology.eventscreatorbackend.auth.AuthService;
import io.nology.eventscreatorbackend.user.User;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class EventLabelService {

	@Autowired
	private EventLabelRepository labelRepository;

	@Autowired
	private AuthService authService;
	
	public List<EventLabel> findAll() {

		// return all for the currently logged in user
		User loggedInUser = this.authService.getCurrentUser();
		return this.labelRepository.findByCreatedBy(loggedInUser);
	}
	
	public Optional<EventLabel> findByName(String name) {
		User loggedInUser = this.authService.getCurrentUser();
		// same here only for current user
		return this.labelRepository.findByNameAndCreatedBy(name, loggedInUser);
		// return this.labelRepository.findByName(name);
	}
	
	public EventLabel create(EventLabelCreateDTO data) {
		// check if that label exists for the current user, only if not, create it


		EventLabel newLabel = EventLabel.builder().name(data.getName()).build();
		return this.labelRepository.save(newLabel);
	}
}
