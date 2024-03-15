package com.example.slshopping.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.slshopping.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
