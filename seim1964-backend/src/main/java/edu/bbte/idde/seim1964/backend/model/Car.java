package edu.bbte.idde.seim1964.backend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Car extends BaseEntity {
    protected String brand;
    protected String model;
    protected Integer price;
    protected String type;
    protected String color;

    public Car() {
        super();
    }

    public Car(String brand, String model, Integer price, String type, String color) {
        super();
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.type = type;
        this.color = color;
    }
}
