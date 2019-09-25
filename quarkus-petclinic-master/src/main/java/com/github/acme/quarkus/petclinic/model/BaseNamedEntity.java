package com.github.acme.quarkus.petclinic.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class BaseNamedEntity extends PanacheEntityBase {



        @Column(name = "name")
        public String name;

        public String getName() {
            return this.name;
        }
}
