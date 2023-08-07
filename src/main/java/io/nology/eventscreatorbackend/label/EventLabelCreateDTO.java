package io.nology.eventscreatorbackend.label;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class EventLabelCreateDTO {

	@NotBlank
	@Getter
	@Setter
	String name;
}
