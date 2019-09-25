package com.github.acme.quarkus.petclinic.service;




import com.github.acme.quarkus.petclinic.model.Pet;
import com.github.acme.quarkus.petclinic.repository.AnimalRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class GreetingService {

    @Inject
    AnimalRepository animalRepository;
    @Transactional
    public void add(Pet animal) throws Exception {
        if (animal==null)
            throw new Exception("Animal was invalidly set.");
        if (animal.id != null)
            throw new Exception("Id was invalidly set.");
        animal.persistAndFlush();
    }
    @Transactional
    public Pet update(Pet animal) throws Exception {
        if (animal==null)
            throw new Exception("Animal was invalidly set.");
        if (animal.id == null)
            throw new Exception("Id was invalidly unset.");

        Pet entity = Pet.findById(animal.id);

        if (entity == null)
            throw new Exception("Animal with id of " + animal.id + " does not exist.");
        entity.setVisits(animal.getVisits());
        entity.setOwner(animal.getOwner());
        entity.setName(animal.getName());
        entity.setAge(animal.getAge());
        entity.setBirthDate(animal.getBirthDate());
        entity.setType(animal.getType());
        animalRepository.persistAndFlush(entity);
        return entity;
    }
    public List<Pet> search(Pet animal){
        if(animal==null)
            return animalRepository.findByType(animal);

        Map<String, Object> params = new HashMap<>();
        StringBuffer query = new StringBuffer();
        if(animal.id!=null&&animal.id>0) {
            params.put("id", animal.getId());
            query.append("id = :id");
        }
        if(animal.getAge()!=null&&animal.getAge()>0) {
            params.put("age", animal.getAge());
            if(query.length()>0)
                query.append(" and ");
            query.append("age = :age");
        }
        if(animal.getName()!=null&&!animal.getName().isEmpty()) {
            params.put("name", animal.getName());
            if(query.length()>0)
                query.append(" and ");
            query.append("name = :name");
        }
        if(animal.getType()!=null) {
            params.put("type", animal.getType());
            if(query.length()>0)
                query.append(" and ");
            query.append("type = :type");
        }
        if(animal.getBirthDate()!=null) {
            params.put("birthDate", animal.getBirthDate());
            if(query.length()>0)
                query.append(" and ");
            query.append("birthDate = :birthDate");
        }

        return animalRepository.find(query.toString(), params).list().stream().sorted(Comparator.comparing(Pet::getBirthDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    private String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");

    public String greeting(String name) {
        return "hello " + name + " from " + hostname;
    }

}
