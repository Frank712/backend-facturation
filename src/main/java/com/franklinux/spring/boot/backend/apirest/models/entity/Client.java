package com.franklinux.spring.boot.backend.apirest.models.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "clients")
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "must not be empty")
    @Size(min = 4, max = 20, message = "size must be between 4 and 20 characters")
    @Column(nullable = false)
    private String name;

    @NotEmpty(message = "must not be empty")
    private String lastname;

    @NotEmpty(message = "must not be empty")
    @Email(message = "must be a well-formed email address")
    @Column(nullable = false, unique = false)
    private String email;

    @NotNull(message = "must not be null")
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    private String img;

    /*@PrePersist
    public void prePersist(){
        createdAt = new Date();
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    private static final long serialVersionUID = 1L;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
