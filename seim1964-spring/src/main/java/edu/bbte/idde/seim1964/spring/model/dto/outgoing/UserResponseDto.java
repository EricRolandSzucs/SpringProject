package edu.bbte.idde.seim1964.spring.model.dto.outgoing;

import edu.bbte.idde.seim1964.spring.model.Announcement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Collection;

@Data
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
    private String password;
    private String phone;
    private String username;
    private Instant requestDate;

    private Collection<Announcement> announcements;
}
