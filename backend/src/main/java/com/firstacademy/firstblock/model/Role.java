package com.firstacademy.firstblock.model;

import jakarta.persistence.*;

import java.util.Collection;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "role_id")
    private Long id;

    @ManyToMany(mappedBy = "roles", cascade = CascadeType.REMOVE)
    private Collection<User> users;

    @Enumerated(EnumType.STRING)
    private UserRoles role;

}
