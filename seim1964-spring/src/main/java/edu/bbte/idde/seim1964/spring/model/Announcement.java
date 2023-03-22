package edu.bbte.idde.seim1964.spring.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "announcements")
public class Announcement extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate date;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Car car;

    @Column(nullable = false)
    private String description;
}
