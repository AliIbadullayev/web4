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
    private Integer x;
    private Integer y;
    private Integer radius;
    private Boolean result;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="owner_id")
    private AppUser owner;

    public AppPoint() {
    }

    public AppPoint(Integer x, Integer y, Integer radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public AppPoint(Integer x, Integer y, Integer radius, Boolean result) {
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

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
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
