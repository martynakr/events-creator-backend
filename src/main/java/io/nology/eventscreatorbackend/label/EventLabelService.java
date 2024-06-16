package io.nology.eventscreatorbackend.label;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.nology.eventscreatorbackend.auth.AuthService;
import io.nology.eventscreatorbackend.user.User;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class EventLabelService {

	@Autowired
	private EventLabelRepository labelRepository;

	@Autowired
	private AuthService authService;
	
	public List<EventLabel> findAll() {

		User loggedInUser = this.authService.getCurrentUser();
		return this.labelRepository.findByCreatedBy(loggedInUser);
	}
	
	public Optional<EventLabel> findByName(String name) {
		User loggedInUser = this.authService.getCurrentUser();
		return this.labelRepository.findByNameAndCreatedBy(name, loggedInUser);
	}
	
	public EventLabel create(EventLabelCreateDTO data) {

		User loggedInUser = this.authService.getCurrentUser();

		Optional<EventLabel> foundLabel = this.labelRepository.findByNameAndCreatedBy(data.getName(), loggedInUser);

		if(foundLabel.isPresent()) {
			return foundLabel.get();
		}

		EventLabel newLabel = EventLabel.builder().name(data.getName()).colour(this.getRandomColor()).build();
		return this.labelRepository.save(newLabel);
	}

	private String getRandomColor() {
        Random random = new Random();

        int hue = random.nextInt(360);

        int saturation = random.nextInt(30) + 70;

        int lightness = random.nextInt(20) + 70;

        String color = String.format("hsl(%d, %d%%, %d%%)", hue, saturation, lightness);

        return color;
    }
}
