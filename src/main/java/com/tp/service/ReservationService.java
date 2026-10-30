package com.tp.service;

import com.tp.dao.IDao;
import com.tp.dao.ReservationDao;
import com.tp.model.Reservation;
import com.tp.model.Salle;
import com.tp.model.Utilisateur;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Couche service pour la gestion métier des réservations.
 * Gère notamment la détection des conflits de créneaux horaires.
 */
public class ReservationService implements IDao<Reservation> {

    private final ReservationDao reservationDao;

    public ReservationService() {
        this.reservationDao = new ReservationDao();
    }

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Override
    public boolean create(Reservation o) {
        if (o == null || o.getSalle() == null || o.getUtilisateur() == null) {
            System.err.println("  [Validation] La salle et l'utilisateur sont obligatoires pour réserver.");
            return false;
        }
        if (o.getDateDebut() == null || o.getDateFin() == null || o.getDateFin().isBefore(o.getDateDebut())) {
            System.err.println("  [Validation] Les dates de début et fin de réservation sont invalides.");
            return false;
        }

        // Vérification de disponibilité (détection de chevauchement horaire)
        if (reservationDao.hasConflict(o.getSalle(), o.getDateDebut(), o.getDateFin())) {
            System.err.printf("  [Conflit Détecté] La salle '%s' est déjà occupée sur ce créneau !%n",
                    o.getSalle().getCode());
            return false;
        }

        return reservationDao.create(o);
    }

    @Override
    public boolean update(Reservation o) {
        if (o == null || o.getId() == null) return false;
        return reservationDao.update(o);
    }

    @Override
    public boolean delete(Reservation o) {
        if (o == null || o.getId() == null) return false;
        return reservationDao.delete(o);
    }

    @Override
    public Reservation findById(Long id) {
        if (id == null) return null;
        return reservationDao.findById(id);
    }

    public List<Reservation> findBySalle(Salle salle) {
        if (salle == null) return List.of();
        return reservationDao.findBySalle(salle);
    }

    public List<Reservation> findByUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null) return List.of();
        return reservationDao.findByUtilisateur(utilisateur);
    }

    public boolean hasConflict(Salle salle, LocalDateTime debut, LocalDateTime fin) {
        if (salle == null || debut == null || fin == null) return false;
        return reservationDao.hasConflict(salle, debut, fin);
    }

    @Override
    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }
}
