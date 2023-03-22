package edu.bbte.idde.seim1964.spring.model.dto.incoming;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserCreationDto {

    @NotNull
    @Size(min = 6)
    private String email;

    @NotNull
    @Size(min = 6)
    private String name;

    @NotNull
    @Size(min = 10)
    private String password;

    @NotNull
    private String phone;

    @NotNull
    @Size(min = 6)
    private String username;
}
