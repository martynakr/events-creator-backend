package io.nology.eventscreatorbackend.event;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Id;
import io.nology.eventscreatorbackend.label.Label;
import io.nology.eventscreatorbackend.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "events")
public class Event {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	private Long id;
	
	@Getter
	@Setter
	@Column(nullable = false)
	private String name;
	
	@Getter
	@Setter
	@Column(nullable = false)
	private LocalDateTime startDate;
	
	@Getter
	@Setter
	@Column(nullable = false)
	private LocalDateTime endDate;
	
	@ManyToMany
	@JoinTable(name = "event_labels",
		joinColumns = @JoinColumn(name = "event_id"),
		inverseJoinColumns = @JoinColumn(name = "label_id"))
	List<Label> labels;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore()
	User user;
	
}
