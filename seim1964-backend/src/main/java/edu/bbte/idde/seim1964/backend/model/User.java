package edu.bbte.idde.seim1964.backend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends BaseEntity {
    private String username;
    private String name;
    private String email;
    private String password;
    private String phone;

    public User() {
        super();
    }

    public User(String username, String name, String email, String password, String phone) {
        super();
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}
