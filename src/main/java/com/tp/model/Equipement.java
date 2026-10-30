package com.tp.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité JPA représentant un Équipement disponible dans une ou plusieurs salles.
 * Association ManyToMany avec l'entité Salle.
 */
@Entity
@Table(name = "equipements")
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 30)
    private String code;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "etat", length = 30)
    private String etat;

    @ManyToMany(mappedBy = "equipements", fetch = FetchType.LAZY)
    private Set<Salle> salles = new HashSet<>();

    // Constructeur sans argument (requis par JPA)
    public Equipement() {
    }

    // Constructeur avec paramètres sans ID
    public Equipement(String code, String nom, String type, String etat) {
        this.code = code;
        this.nom = nom;
        this.type = type;
        this.etat = etat;
    }

    // Constructeur complet
    public Equipement(Long id, String code, String nom, String type, String etat) {
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.type = type;
        this.etat = etat;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Set<Salle> getSalles() {
        return salles;
    }

    public void setSalles(Set<Salle> salles) {
        this.salles = salles;
    }

    @Override
    public String toString() {
        return String.format("Equipement[ID=%d, Code='%s', Nom='%s', Type='%s', État='%s']",
                id, code, nom, type, etat);
    }
}
