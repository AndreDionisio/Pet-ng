package com.github.acme.quarkus.petclinic.web.resource;

import com.github.acme.quarkus.petclinic.model.Pet;
import com.github.acme.quarkus.petclinic.service.GreetingService;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;

//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import org.jboss.resteasy.annotations.jaxrs.*;
//import org.jboss.resteasy.annotations.jaxrs.PathParam;
//import javax.ws.rs.*;

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

@Path("/hello")

@ApplicationScoped
@Default
public class GreetingResource {

    @Inject
    GreetingService service;
    @POST
    @Path("/Add")
    @Produces("application/json")
    @Consumes("application/json")
    public Response add(Pet animal) throws Exception {
        if (animal.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        service.add(animal);
        return Response.ok(animal).header("Access-Control-Allow-Origin", "*").status(201).build();
    }
    @POST
    @Path("/Search")
    @Produces("application/json")
    @Consumes("application/json")
    public Response search(Pet animal) {
        return Response.ok(service.search(animal)).header("Access-Control-Allow-Origin", "*").status(200).build();
    }

    @PUT
    @Path("/Update")
    @Produces("application/json")
    @Consumes("application/json")
    public Response update(Pet animal) throws Exception {
        if (animal.getName() == null) {
            throw new WebApplicationException("Animal name was not set on request.", 422);
        }
        return Response.ok(service.update(animal)).header("Access-Control-Allow-Origin", "*").status(204).build();
    }
    @GET
    @Path("/All")
    @Produces("application/json")
    public Response getAll() {
        return Response.ok(Pet.listAll(Sort.by("name"))).header("Access-Control-Allow-Origin", "*").status(200).build();
    }

    @GET
    @Path("/pet/{id}")
    @Produces("application/json")
    public Response getSingle(@PathParam("id") Integer id) {
        Pet entity = Pet.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Animal with id of " + id + " does not exist.", 404);
        }
        return Response.ok(entity).header("Access-Control-Allow-Origin", "*").status(200).build();
    }


    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") Long id) {
        Pet entity = Pet.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Animal with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(202).header("Access-Control-Allow-Origin", "*").build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Override
        public Response toResponse(Exception exception) {
            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }
            if (exception instanceof Exception) {
                code = 404;
            }
            return Response.status(code)
                    .entity(Json.createObjectBuilder().add("error", exception.getMessage()).add("code", code).build())
                    .build();
        }

    }
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/greeting/{name}")
    public String greeting(@PathParam("name") String name) {
        return service.greeting(name);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }
}
