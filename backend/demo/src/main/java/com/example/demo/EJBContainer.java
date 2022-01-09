package com.example.demo;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.*;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.demo.secure.KeyGenerator;
import com.example.demo.secure.PasswordHash;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONArray;


@Stateless
public class EJBContainer {
    AppUser createdAppUser;
    AppPoint createdAppPoint;

    EntityManagerFactory entityManagerFactory;
    EntityManager entityManager;
    EntityTransaction transaction;

    List<AppUser> tempUsers;
    List<AppPoint> tempPoints;
    PasswordHash hashAlgorithm;

    @Context
    private UriInfo uriInfo;

    @Inject
    public KeyGenerator keyGenerator;

    private void connection() {
        entityManagerFactory = Persistence.createEntityManagerFactory("PostgresDS");
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
        hashAlgorithm = new PasswordHash();
    }

    public EJBContainer (){
        tempUsers = new ArrayList<>();
        tempPoints = new ArrayList<>();
        connection();
        loadEntries();
    }

    private void loadEntries() {
        try {
            transaction.begin();
            Query query = entityManager.createQuery("SELECT e FROM AppUser e");
            tempUsers = query.getResultList();
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    private void loadPoints(Long id) {
        try {
            transaction.begin();
            Query query = entityManager.createQuery("SELECT new com.example.demo.AppPoint(e.x, e.y, e.radius, e.result) FROM AppPoint e where e.owner.id = :id ");
            query.setParameter("id", id);
            tempPoints = query.getResultList();
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }

    }

    public AppUser findUserById(Long id){
        transaction.begin();
        AppUser appUser = entityManager.find(AppUser.class, id);
        transaction.commit();
        return appUser;
    }

    public AppUser findUserByName(String username){
        transaction.begin();
        TypedQuery<AppUser> query = entityManager.createNamedQuery(AppUser.FIND_BY_USERNAME, AppUser.class);
        query.setParameter("username", username);
        AppUser user = query.getSingleResult();
        transaction.commit();
        return user;
    }

    public AppUser createUser(String username, String password ){
        try {
            createdAppUser = new AppUser();
            createdAppUser.setUsername(username);
            createdAppUser.setPassword(hashAlgorithm.makeHash(password));

            if (!userExist(username)) {
                transaction.begin();
                entityManager.persist(createdAppUser);
                transaction.commit();
                tempUsers.add(createdAppUser);
                return createdAppUser;
            }
            return null;
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
                return null;
            }
            throw exception;
        }
    }

    public AppPoint checkPoint(int x, int y, int r, String name) throws Exception {
        if (!validateXY(x) || !validateXY(y) || !validateRadius(r)) throw new Exception("Can not pass validation test");
        createdAppPoint = new AppPoint(x, y, r);
        createdAppPoint.setResult((y >= 0 && y <= r && x >= -r && x <= 0) || ((x * x) + (y * y) <= (r * r) && x <= 0 && y <= 0) || (x - r <= y && x >= 0 && y <= 0));
        createdAppPoint.setOwner(findUserByName(name));
        transaction.begin();
        entityManager.persist(createdAppPoint);
        transaction.commit();
        loadPoints(createdAppPoint.getOwner().getId());
        return createdAppPoint;
    }

    private boolean validateRadius(int r) {
        return r > 0 && r <= 5;
    }

    private boolean validateXY(int xy){
        return xy >= -3 && xy <= 5;
    }

    public List<AppPoint> getPointsOnInit( String name) {
        loadPoints(findUserByName(name).getId());
        return tempPoints;
    }


    public List<AppPoint> getTempPoints() {
        return tempPoints;
    }

    public boolean authenticate(String username, String password) {
        try{
            transaction.begin();
            TypedQuery<AppUser> query = entityManager.createNamedQuery(AppUser.FIND_BY_USERNAME_PASSWORD, AppUser.class);
            query.setParameter("username", username);
            query.setParameter("password", hashAlgorithm.makeHash(password));
            AppUser user = query.getSingleResult();
            transaction.commit();
            return true;
        }catch (NoResultException e){
            return false;
        }
    }

    public String issueToken(String login) {
        Key key = keyGenerator.generateKey();
        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date())
                //.setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
                .setExpiration(Date.from(Instant.ofEpochSecond(4622470422L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    public boolean userExist(String username) {
        boolean is_exist = false;
        for(AppUser u: tempUsers) {
            if (!username.equals(u.getUsername())) {
                continue;
            }
            is_exist = true;
        }
        return is_exist;
    }
}
