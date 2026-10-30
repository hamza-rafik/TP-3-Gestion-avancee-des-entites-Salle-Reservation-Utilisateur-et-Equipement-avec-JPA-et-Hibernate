package com.tp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entité JPA représentant une Réservation de Salle par un Utilisateur.
 * Associations :
 * - ManyToOne avec Utilisateur (l'utilisateur qui réserve)
 * - ManyToOne avec Salle (la salle réservée)
 */
@Entity
@Table(name = "reservations", indexes = {
        @Index(name = "idx_salle_creneaux", columnList = "salle_id, date_debut, date_fin")
})
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDateTime dateFin;

    @Column(name = "motif", nullable = false, length = 150)
    private String motif;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 30)
    private StatutReservation statut = StatutReservation.CONFIRMEE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;

    // Constructeur sans argument (requis par JPA)
    public Reservation() {
    }

    // Constructeur avec paramètres
    public Reservation(LocalDateTime dateDebut, LocalDateTime dateFin, String motif,
                       StatutReservation statut, Utilisateur utilisateur, Salle salle) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.statut = statut;
        this.utilisateur = utilisateur;
        this.salle = salle;
    }

    // Constructeur complet
    public Reservation(Long id, LocalDateTime dateDebut, LocalDateTime dateFin, String motif,
                       StatutReservation statut, Utilisateur utilisateur, Salle salle) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.statut = statut;
        this.utilisateur = utilisateur;
        this.salle = salle;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String userName = (utilisateur != null) ? (utilisateur.getPrenom() + " " + utilisateur.getNom()) : "N/A";
        String salleCode = (salle != null) ? salle.getCode() : "N/A";

        return String.format("Reservation[ID=%d, Salle='%s', Utilisateur='%s', Début=%s, Fin=%s, Statut=%s, Motif='%s']",
                id, salleCode, userName, dateDebut.format(fmt), dateFin.format(fmt), statut, motif);
    }
}
