package edu.bbte.idde.seim1964.backend.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class BaseEntity implements Serializable {
    protected Long id;
}