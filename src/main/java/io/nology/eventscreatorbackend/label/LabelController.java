package io.nology.eventscreatorbackend.label;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/labels")
public class LabelController {
	
	@Autowired
	private LabelService labelService; 
	
	@GetMapping
	public ResponseEntity<List<Label>> findAll() {
		List<Label> allLabels = this.labelService.findAll();
		return new ResponseEntity<List<Label>>(allLabels, HttpStatus.OK);
	} 

	@PostMapping
	public ResponseEntity<Label> postMethodName(@Valid @RequestBody LabelCreateDTO data) {
		Label newLabel = this.labelService.create(data);
		return new ResponseEntity<Label>(newLabel, HttpStatus.CREATED);
	}
	
}
