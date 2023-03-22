package edu.bbte.idde.seim1964.spring.model.dto.outgoing;

import edu.bbte.idde.seim1964.spring.model.Car;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AnnouncementResponseDto {
    private Long id;
    private String title;
    private LocalDate date;
    private Car car;
    private String description;
}
