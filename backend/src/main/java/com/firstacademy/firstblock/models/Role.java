package com.firstacademy.firstblock.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @Enumerated(EnumType.STRING)
    private UserRoles role;

    public Role() {

    }

    public Role(Long id, UserRoles role) {
        this.id = id;
        this.role = role;
        this.users = new ArrayList<User>();
    }

    public Long getId() {
        return id;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public UserRoles getUserRoles() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Role)) {
            return false;
        }

        Role role = (Role) o;
        return Objects.equals(this.id, role.id) && Objects.equals(this.role, role.role);

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.role);
    }

    @Override
    public String toString() {
        return "Role{ " + "id=" + this.id + " role='" + this.role + "'}";
    }

}
