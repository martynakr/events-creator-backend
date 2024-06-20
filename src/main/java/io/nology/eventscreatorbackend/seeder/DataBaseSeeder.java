package io.nology.eventscreatorbackend.seeder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.nology.eventscreatorbackend.event.Event;
import io.nology.eventscreatorbackend.event.EventRepository;
import io.nology.eventscreatorbackend.label.Label;
import io.nology.eventscreatorbackend.label.LabelRepository;
import io.nology.eventscreatorbackend.user.User;
import io.nology.eventscreatorbackend.user.UserRepository;

@Component
public class DataBaseSeeder implements CommandLineRunner {
	
	@Autowired
	private LabelRepository labelRepository;
	
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

	private String getRandomColor() {
        Random random = new Random();

        int hue = random.nextInt(360);

        int saturation = random.nextInt(30) + 70;

        int lightness = random.nextInt(20) + 70;

        String color = String.format("hsl(%d, %d%%, %d%%)", hue, saturation, lightness);

        return color;
    }
	
	private void seedData() {
		
		User user1 = new User("John", "Doe", "john@email.com", passwordEncoder.encode("pass"));
		User user2 = new User("Jane", "Smith", "jane@email.com", passwordEncoder.encode("pass"));
		
		ArrayList<User> allUsers = new ArrayList<>(Arrays.asList(user1, user2));
		allUsers.forEach(u -> this.userRepository.save(u));

		Label label = new Label();
		label.setName("work");
		label.setColour(getRandomColor());
		label.setCreatedBy(user2);
		
		Label label2 = new Label();
		label2.setName("sport");
		label2.setColour(getRandomColor());
		label2.setCreatedBy(user2);
		
		Label label3 = new Label();
		label3.setName("family time");
		label3.setColour(getRandomColor());
		label3.setCreatedBy(user1);

		ArrayList<Label> allLabels = new ArrayList<>(Arrays.asList(label, label2, label3));
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
				.startDate(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 30)))
				.endDate(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(12, 30)))
				.labels(allLabels)
				.user(user1)
				.build();
		
		ArrayList<Event> allEvents = new ArrayList<>(Arrays.asList(event1, event2));
		
		allEvents.forEach(e -> this.eventRepository.save(e));
		
		System.out.println("seeded successfully");
	}

	// command to run seeder: mvn spring-boot:run -Dspring-boot.run.arguments=seed

}
