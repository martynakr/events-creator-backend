package io.nology.eventscreatorbackend.seeder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.nology.eventscreatorbackend.event.Event;
import io.nology.eventscreatorbackend.event.EventRepository;
import io.nology.eventscreatorbackend.label.EventLabel;
import io.nology.eventscreatorbackend.label.EventLabelRepository;
import io.nology.eventscreatorbackend.user.User;
import io.nology.eventscreatorbackend.user.UserRepository;

@Component
public class DataBaseSeeder implements CommandLineRunner {
	
	@Autowired
	private EventLabelRepository labelRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		  if (args.length > 0 && args[0].equals("seed")) {
	            seedData();
	        }
	}
	
	private void seedData() {
		
		User user1 = new User("John", "Doe", "john@email.com", passwordEncoder.encode("pass"));
		User user2 = new User("Jane", "Smith", "jane@email.com", passwordEncoder.encode("pass"));
		
		ArrayList<User> allUsers = new ArrayList<>(Arrays.asList(user1, user2));
		allUsers.forEach(u -> this.userRepository.save(u));

		EventLabel label = new EventLabel();
		label.setName("work");
		label.setCreatedBy(user2);
		
		EventLabel label2 = new EventLabel();
		label2.setName("sport");
		label2.setCreatedBy(user2);
		
		EventLabel label3 = new EventLabel();
		label3.setName("family time");
		label3.setCreatedBy(user1);

		ArrayList<EventLabel> allLabels = new ArrayList<>(Arrays.asList(label, label2, label3));
		allLabels.forEach(l -> this.labelRepository.save(l));
		
		
		Event event1 = Event.builder()
				.name("Yoga in the park")
				.startDate(LocalDateTime.of(2023, 10, 05, 10, 30))
				.endDate(LocalDateTime.of(2023, 10, 05, 11, 30))
				.labels(allLabels)
				.user(user2)
				.build();
		
		Event event2 = Event.builder()
				.name("10km run")
				.startDate(LocalDateTime.of(2023, 10, 07, 10, 30))
				.endDate(LocalDateTime.of(2023, 10, 07, 12, 30))
				.labels(allLabels)
				.user(user1)
				.build();
		
		ArrayList<Event> allEvents = new ArrayList<>(Arrays.asList(event1, event2));
		
		allEvents.forEach(e -> this.eventRepository.save(e));
		
		System.out.println("seeded successfully");
	}

	// command to run seeder: mvn spring-boot:run -Dspring-boot.run.arguments=seed

}
