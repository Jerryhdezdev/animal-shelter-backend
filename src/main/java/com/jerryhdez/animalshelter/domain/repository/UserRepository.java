package com.jerryhdez.animalshelter.domain.repository;

import com.jerryhdez.animalshelter.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

