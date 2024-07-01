package io.nology.eventscreatorbackend.event;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import io.nology.eventscreatorbackend.config.JwtService;
import io.nology.eventscreatorbackend.user.Role;
import io.nology.eventscreatorbackend.user.User;
import io.nology.eventscreatorbackend.user.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserService userService;

    @Test
    void shouldRejectCreatingEventWhenNotLoggedIn() throws Exception {

        String jsonEvent = "{"
            + "\\\"name\\\": \\\"New event\\\","
            + "\\\"startDate\\\": \\\"2024-07-01T10:00:00\\\","
            + "\\\"endDate\\\": \\\"2024-07-01T12:00:00\\\","
            + "\\\"labels\\\": []"
            + "}";

        this.mockMvc.perform(
            post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEvent)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnEventsWhenUserLoggedIn() throws Exception {
 
        Event existingEvent = Event.builder()
            .name("New event")
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(1))
            .build();

        User existingUser = User.builder()
            .firstName("John")
            .lastName("Smith")
            .password("pass")
            .email("john@mail.com")
            .role(Role.STANDARD_USER)
            .build();

        List<Event> existingEvents = new ArrayList<Event>();
        existingEvents.add(existingEvent);
        
        when(this.eventService.findAll()).thenReturn(existingEvents);
        when(this.jwtService.extractUserId(anyString())).thenReturn(1l);
        when(this.userService.getById(anyLong())).thenReturn(existingUser);
        when(this.jwtService.isTokenValid(anyString(), any(User.class))).thenReturn(true);

        this.mockMvc.perform(
            get("/events")
                .cookie(new MockCookie("jwt", "testJWT"))
        ).andExpect(status().isOk())
         .andExpect(jsonPath("$[0].name").value("New event"))
         .andExpect(jsonPath("$[0].startDate").exists());
    }

    @Test
    void shouldCreateEventWhenValidDataAndUserLoggedIn() throws Exception {

        User existingUser = User.builder()
            .firstName("John")
            .lastName("Smith")
            .password("pass")
            .email("john@mail.com")
            .role(Role.STANDARD_USER)
            .build();

        Event createdEvent = Event.builder()
            .name("Created event")
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(1))
            .build();

        String jsonEvent = "{"
            + "\"name\": \"Created event\","
            + "\"startDate\": \"2024-07-01T10:00:00\","
            + "\"endDate\": \"2024-07-01T12:00:00\","
            + "\"labels\": []"
            + "}";

        when(this.eventService.create(any(EventCreateDTO.class))).thenReturn(createdEvent);
        when(this.jwtService.extractUserId(anyString())).thenReturn(1l);
        when(this.userService.getById(anyLong())).thenReturn(existingUser);
        when(this.jwtService.isTokenValid(anyString(), any(User.class))).thenReturn(true);

        this.mockMvc.perform(
            post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEvent)
                .cookie(new MockCookie("jwt", "testJWT"))
        ).andExpect(status().isCreated())
        .andExpect(jsonPath("name").value("Created event"))
        .andExpect(jsonPath("startDate").exists())
        .andExpect(jsonPath("endDate").exists());
    }
}