package io.nology.eventscreatorbackend.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginDTO {

	@NotBlank
	private String email;
	
	@NotBlank
	private String password;
	
}
