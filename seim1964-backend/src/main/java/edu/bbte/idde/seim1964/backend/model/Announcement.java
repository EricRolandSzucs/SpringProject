package edu.bbte.idde.seim1964.backend.model;

import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Announcement extends BaseEntity {
    private String title;
    private Long userId;
    private LocalDate date;
    private Car car;
    private String description;

    public Announcement() {
        super();
    }

    public Announcement(String title, Long userId, LocalDate date, Car car, String description) {
        super();
        this.title = title;
        this.userId = userId;
        this.date = date;
        this.car = car;
        this.description = description;
    }
}
