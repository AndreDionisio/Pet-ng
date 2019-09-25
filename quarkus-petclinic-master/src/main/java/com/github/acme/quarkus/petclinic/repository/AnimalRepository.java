package com.github.acme.quarkus.petclinic.repository;

import com.github.acme.quarkus.petclinic.model.Pet;
import com.github.acme.quarkus.petclinic.model.PetType;
import io.agroal.api.AgroalDataSource;
import io.quarkus.hibernate.orm.panache.PanacheRepository;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/*
Design a Java Web Application that uses the spring framework (the use of Springboot is mandatory) with
        the following requirements:
        -Objective
        + Manage Animals (Dogs and Cats) for adoption with the following rules:
        - The options for adoption are:
        + You can select if you want a Dog or Cat;
        + Or you can select Any (without specifying if it is a Dog or a Cat)
        + The oldest animals must always be chosen before
        - Expected Operation
        + Add
        + Search
        + Update
*/
@ApplicationScoped
public class AnimalRepository implements PanacheRepository<Pet> {

    public List<Pet> findByType(Pet type) {
        if (type == null)
            return Pet.<Pet>streamAll()
                    .sorted(Comparator.comparing(Pet::getBirthDate, Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());

        return Pet.<Pet>streamAll()
                .filter(a -> {
                    if (a.getClass().isInstance(type))
                        return true;
                    else return false;
                })
                .sorted(Comparator.comparing(Pet::getBirthDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }
    public Pet findByType(PetType type) {
        return list("type", type).stream().sorted(Comparator.comparing(Pet::getBirthDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .findFirst().get();
    }
}
