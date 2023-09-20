package io.nology.eventscreatorbackend.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.nology.eventscreatorbackend.auth.AuthService;
import io.nology.eventscreatorbackend.exceptions.NotFoundException;
import io.nology.eventscreatorbackend.label.EventLabel;
import io.nology.eventscreatorbackend.label.EventLabelService;
import io.nology.eventscreatorbackend.user.User;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EventService {
	
	@Autowired
	private EventRepository repository;
	
	@Autowired
	private EventLabelService labelService;
	
	@Autowired
	private AuthService authService;

	@Autowired
	private ModelMapper modelMapper;
	
	public Event create(EventCreateDTO data) {
		
		// get logged in user and assign to event
		User loggedInUser = this.authService.getCurrentUser();
		
		Event newEvent = Event.builder()
				.name(data.getName())
				.startDate(data.getStartDate())
				.endDate(data.getEndDate())
				.user(loggedInUser)
				.labels(new ArrayList<EventLabel>())
				.build();
		
		if(data.getLabels() != null ) {
			data.getLabels().forEach(l -> {
				Optional<EventLabel> maybeLabel = this.labelService.findByName(l.getName());
				if(maybeLabel.isPresent()) {
					newEvent.getLabels().add(maybeLabel.get());
				} else {
					EventLabel newLabel = this.labelService.create(l);
					newEvent.getLabels().add(newLabel);
				}
				
			}
		  );
			
		}
		return this.repository.save(newEvent);
	}
	
	public List<Event> findAll() {
		
		User loggedInUser = this.authService.getCurrentUser();
	
		return this.repository.findByUserId(loggedInUser.getId());
	}
	
	public Event update(Long id, EventUpdateDTO data) {
		Optional<Event> foundEvent = this.repository.findById(id);
		
		if(foundEvent.isEmpty()) {
			throw new NotFoundException("Event with id: " + id + " not found");
		}

		Event exisitngEvent = foundEvent.get();
		modelMapper.map(data, exisitngEvent);

		return this.repository.save(exisitngEvent);
		
	}

}
