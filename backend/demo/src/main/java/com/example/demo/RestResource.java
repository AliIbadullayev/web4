package com.example.demo;

import com.example.demo.filter.JWTTokenNeeded;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/rest")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class RestResource {
    @Inject
    private EJBContainer service;

    @Context
    HttpServletRequest httpServletRequest;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register")
    public Response doRegister(String json) throws JsonProcessingException {
        ObjectMapper obj = new ObjectMapper();
        AppUser appUser = obj.readValue(json, AppUser.class);
        AppUser result = service.createUser(appUser.getUsername(), appUser.getPassword());
        return Response.status(200).entity(result).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(String json) {

        try {
            ObjectMapper obj = new ObjectMapper();
            AppUser appUser = obj.readValue(json, AppUser.class);
            // Authenticate the user using the credentials provided
            if (service.authenticate(appUser.getUsername(), appUser.getPassword())){
                // Issue a token for the user
                String token = service.issueToken(appUser.getUsername());

                // Return the token on the response
                return Response.ok()
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                        .header("Access-Control-Allow-Credentials", "true")
                        .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                        .header("Access-Control-Max-Age", "1209600")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build();
            } else throw new Exception("Invalid username/password");

        } catch (Exception e) {
            return Response.status(500).build();
        }
    }

    @JWTTokenNeeded
    @GET
    @Path("/hello")
    public String getMessage() {

        return "hi "+ httpServletRequest.getAttribute("name");
    }

    @JWTTokenNeeded
    @POST
    @Path("/addpoint")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addPoint(String json) throws JsonProcessingException {
        try{
            ObjectMapper obj = new ObjectMapper();
            AppPoint appPoint = obj.readValue(json, AppPoint.class);
            AppPoint result = service.checkPoint(
                    appPoint.getX(),
                    appPoint.getY(),
                    appPoint.getRadius(),
                    httpServletRequest.getAttribute("name").toString()
            );
            return Response.status(200).entity(new JSONArray(service.getTempPoints())).build();
        }catch (Exception e){
            System.err.println(e.getMessage());
            return Response.status(500).build();
        }
    }

    @JWTTokenNeeded
    @GET
    @Path("/getpoints")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getPoints(String json) {
        return Response.status(200).entity(
                new JSONArray(service.getPointsOnInit(httpServletRequest.getAttribute("name").toString()))
        ).build();
    }

}