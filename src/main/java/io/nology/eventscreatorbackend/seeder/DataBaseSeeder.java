package io.nology.eventscreatorbackend.seeder;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.nology.eventscreatorbackend.label.EventLabel;
import io.nology.eventscreatorbackend.label.EventLabelRepository;

@Component
public class DataBaseSeeder implements CommandLineRunner {
	
	@Autowired
	private EventLabelRepository labelRepository;

	@Override
	public void run(String... args) throws Exception {
		  if (args.length > 0 && args[0].equals("seed")) {
	            seedData();
	        }
	}
	
	private void seedData() {
		
		EventLabel label = new EventLabel();
		label.setName("work");
		
		EventLabel label2 = new EventLabel();
		label2.setName("sport");
		
		EventLabel label3 = new EventLabel();
		label3.setName("family time");

		ArrayList<EventLabel> allLabels = new ArrayList<>(Arrays.asList(label, label2, label3));
		allLabels.forEach(l -> this.labelRepository.save(l));
		System.out.println("seeded successfully");
	}

	// command to run seeder: mvn spring-boot:run -Dspring-boot.run.arguments=seed

}
