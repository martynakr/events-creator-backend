package io.nology.eventscreatorbackend.label;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.nology.eventscreatorbackend.event.Event;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "labels")
public class EventLabel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	Long id;
	
	@Column(nullable = false)
	@Getter
	@Setter
	private String name;

	// relationship with users
	
	@Getter
	@Setter
	@JsonIgnore
	@ManyToMany(mappedBy = "labels")
	private List<Event> events;
}
