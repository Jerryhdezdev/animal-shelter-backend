package com.jerryhdez.animalshelter.domain.repository;

import com.jerryhdez.animalshelter.domain.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

}
