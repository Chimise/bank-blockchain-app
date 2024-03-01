
package com.firstacademy.firstblock.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.firstacademy.firstblock.model.Role;
import com.firstacademy.firstblock.model.UserRoles;

/**
 * RoleRepository
 */
public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRole(UserRoles role);
}