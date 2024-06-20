package io.nology.eventscreatorbackend.event;

import java.time.LocalDateTime;
import java.util.List;

import io.nology.eventscreatorbackend.label.LabelCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class EventCreateDTO {

	@NotBlank
	@Getter
	@Setter
	String name;
	
	@NotNull
	@Getter
	@Setter
	LocalDateTime startDate;
	
	@NotNull
	@Getter
	@Setter
	LocalDateTime endDate;
	
	@Getter
	@Setter
	List<@Valid LabelCreateDTO> labels = null;
}
