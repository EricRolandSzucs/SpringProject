package edu.bbte.idde.seim1964.spring.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Collection<Announcement> announcements;

    public void setAnnouncements(Announcement announcement) {
        announcements.add(announcement);
    }

    public void deleteAnnouncements(Long idAnnouncement) {
        announcements.removeIf(announcement -> Objects.equals(announcement.getId(), idAnnouncement));
    }
}

