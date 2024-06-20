package io.nology.eventscreatorbackend.label;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class LabelCreateDTO {

	@NotBlank
	@Getter
	@Setter
	String name;
}
