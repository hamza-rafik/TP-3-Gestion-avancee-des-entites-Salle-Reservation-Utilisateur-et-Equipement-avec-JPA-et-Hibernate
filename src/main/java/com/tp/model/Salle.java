package com.tp.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entité JPA représentant une Salle.
 * Associations :
 * - ManyToMany avec Equipement (via table de jointure 'salle_equipement')
 * - OneToMany avec Reservation (une salle peut faire l'objet de plusieurs réservations)
 */
@Entity
@Table(name = "salles")
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 30)
    private String code;

    @Column(name = "libelle", nullable = false, length = 100)
    private String libelle;

    @Column(name = "capacite", nullable = false)
    private int capacite;

    @Column(name = "etage", length = 30)
    private String etage;

    // Association Many-to-Many avec Équipement
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "salle_equipement",
            joinColumns = @JoinColumn(name = "salle_id"),
            inverseJoinColumns = @JoinColumn(name = "equipement_id")
    )
    private Set<Equipement> equipements = new HashSet<>();

    // Association One-to-Many avec Réservation
    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    // Constructeur sans argument (requis par JPA)
    public Salle() {
    }

    // Constructeur avec paramètres
    public Salle(String code, String libelle, int capacite, String etage) {
        this.code = code;
        this.libelle = libelle;
        this.capacite = capacite;
        this.etage = etage;
    }

    // Constructeur complet
    public Salle(Long id, String code, String libelle, int capacite, String etage) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.capacite = capacite;
        this.etage = etage;
    }

    // Méthodes de synchronisation de la relation bidirectionnelle Salle <-> Equipement
    public void addEquipement(Equipement equipement) {
        if (equipement != null) {
            this.equipements.add(equipement);
            equipement.getSalles().add(this);
        }
    }

    public void removeEquipement(Equipement equipement) {
        if (equipement != null) {
            this.equipements.remove(equipement);
            equipement.getSalles().remove(this);
        }
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getEtage() {
        return etage;
    }

    public void setEtage(String etage) {
        this.etage = etage;
    }

    public Set<Equipement> getEquipements() {
        return equipements;
    }

    public void setEquipements(Set<Equipement> equipements) {
        this.equipements = equipements;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return String.format("Salle[ID=%d, Code='%s', Libelle='%s', Capacité=%d, Étage='%s']",
                id, code, libelle, capacite, etage);
    }
}
