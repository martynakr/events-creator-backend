package io.nology.eventscreatorbackend.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/events")
public class EventController {

	@Autowired
	private EventService service;
	
	@GetMapping
	public ResponseEntity<List<Event>> getAll() {
		List<Event> allEvents = this.service.findAll();
		return new ResponseEntity<List<Event>>(allEvents, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Event> create(@Valid @RequestBody EventCreateDTO data) {
		Event created = this.service.create(data);
		return new ResponseEntity<Event>(created, HttpStatus.CREATED);
		
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Event> update(@Valid @RequestBody EventUpdateDTO data, @PathVariable Long id){

		try {
		Event updated = this.service.update(id, data);
		return new ResponseEntity<Event>(updated, HttpStatus.OK);
		} catch (Exception ex) {
			throw ex;
		}
		
	}
}
