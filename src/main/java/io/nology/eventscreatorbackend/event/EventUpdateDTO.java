package io.nology.eventscreatorbackend.event;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

public class EventUpdateDTO {

	@Getter
	@Setter
	String name;
	
	@Getter
	@Setter
	LocalDateTime startDate;
	
	@Getter
	@Setter
	LocalDateTime endDate;
    
}
