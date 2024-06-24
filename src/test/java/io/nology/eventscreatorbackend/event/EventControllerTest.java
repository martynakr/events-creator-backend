package io.nology.eventscreatorbackend.event;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Dotenv dotenv;

    @MockBean private EventService eventService;

      private Key getSingInKey() {
        String secret = dotenv.get("SECRET_KEY");
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken() {
           return Jwts
                .builder()
                .setClaims(null)
                .setSubject("1")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 3600))
                .signWith(this.getSingInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

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
        
        List<Event> existingEvents = new ArrayList();
        existingEvents.add(existingEvent);
        
        when(this.eventService.findAll()).thenReturn(existingEvents);

        this.mockMvc.perform(
            get("/events")
                .cookie(new MockCookie("jwt", this.generateToken()))
        ).andExpect(status().isOk())
         .andExpect(jsonPath("$[0].name").value("New event"))
         .andExpect(jsonPath("$[0].startDate").exists());
    }
}
