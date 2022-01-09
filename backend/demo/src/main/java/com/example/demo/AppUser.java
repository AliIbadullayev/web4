package com.example.demo;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "app_user")
@NamedQueries({
        @NamedQuery(name = AppUser.FIND_BY_USERNAME_PASSWORD, query = "SELECT u FROM AppUser u WHERE u.username = :username and u.password = :password"),
        @NamedQuery(name = AppUser.FIND_BY_USERNAME, query = "SELECT u FROM AppUser u WHERE u.username = :username ")
})
public class AppUser {
    public static final String FIND_BY_USERNAME_PASSWORD = "AppUser.findByUserAndPassword";
    public static final String FIND_BY_USERNAME = "AppUser.findByUser";

    @Id
    @SequenceGenerator(name = "user_id", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id")
    @Column(name = "user_id")
    private Long id;
    private String username;
    private String password;

    public AppUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(id, appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


