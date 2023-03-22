package edu.bbte.idde.seim1964.spring.model.dto.outgoing;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarResponseDto {
    private Long id;
    private String brand;
    private String color;
    private String model;
    private Float price;
    private String type;
}