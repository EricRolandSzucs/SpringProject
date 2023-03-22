package edu.bbte.idde.seim1964.spring.model.dto.incoming;

import edu.bbte.idde.seim1964.spring.model.Car;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class AnnouncementCreationDto {

    @NotNull
    @Size(min = 6)
    private String title;

    @NotNull
    @Past
    private LocalDate date;

    @NotNull
    private Car car;

    @NotNull
    @Size(min = 6)
    private String description;
}
