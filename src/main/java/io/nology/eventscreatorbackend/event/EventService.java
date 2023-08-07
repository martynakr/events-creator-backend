package io.nology.eventscreatorbackend.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.nology.eventscreatorbackend.label.EventLabel;
import io.nology.eventscreatorbackend.label.EventLabelService;
import io.nology.eventscreatorbackend.user.User;
import io.nology.eventscreatorbackend.user.UserRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EventService {
	
	@Autowired
	private EventRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EventLabelService labelService;
	
	public Event create(EventCreateDTO data) {
		
		// get logged in user and assign to event
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(auth.getName() + " auth credentials");
		
		String email = auth.getName();
		
		// do this better
		Optional<User> loggedInUser = Optional.ofNullable(
				this.userRepository.findByEmail(email).orElse(null));
		
		Event newEvent = Event.builder()
				.name(data.getName())
				.startDate(data.getStartDate())
				.endDate(data.getEndDate())
				.user(loggedInUser.get())
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
		// check the user, return all for that user
		return this.repository.findAll();
	}

}
