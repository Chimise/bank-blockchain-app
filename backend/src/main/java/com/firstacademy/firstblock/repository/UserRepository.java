package com.firstacademy.firstblock.repository;

import org.springframework.data.repository.CrudRepository;

import com.firstacademy.firstblock.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
