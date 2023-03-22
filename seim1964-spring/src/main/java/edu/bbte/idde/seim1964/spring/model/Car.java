package edu.bbte.idde.seim1964.spring.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "cars")
public class Car extends BaseEntity {

    @Column(nullable = false)
    protected String brand;

    @Column(nullable = false)
    protected String model;

    @Column(nullable = false)
    protected Integer price;

    @Column(nullable = false)
    protected String type;

    @Column(nullable = false)
    protected String color;
}
