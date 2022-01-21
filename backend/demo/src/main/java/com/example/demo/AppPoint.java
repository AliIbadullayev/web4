package com.example.demo;

import javax.persistence.*;

@Entity
@Table(name = "app_point")
public class AppPoint {

    @Id
    @SequenceGenerator(name = "point_id", sequenceName = "point_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "point_id")
    @Column(name = "point_id")
    private Long id;
    private Float x;
    private Float y;
    private Float radius;
    private Boolean result;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="owner_id")
    private AppUser owner;

    public AppPoint() {
    }

    public AppPoint(Float x, Float y, Float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public AppPoint(Float x, Float y, Float radius, Boolean result) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser owner) {
        this.owner = owner;
    }
}
