package com.tp.dao;

import com.tp.model.Reservation;
import com.tp.model.Salle;
import com.tp.model.StatutReservation;
import com.tp.model.Utilisateur;
import com.tp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * DAO pour l'entité Reservation avec requêtes avancées HQL/JPQL.
 */
public class ReservationDao implements IDao<Reservation> {

    @Override
    public boolean create(Reservation o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Attacher les entités Salle et Utilisateur à la session courante
            if (o.getSalle() != null && o.getSalle().getId() != null) {
                o.setSalle(session.get(Salle.class, o.getSalle().getId()));
            }
            if (o.getUtilisateur() != null && o.getUtilisateur().getId() != null) {
                o.setUtilisateur(session.get(Utilisateur.class, o.getUtilisateur().getId()));
            }

            session.persist(o);
            tx.commit();
            System.out.printf("  [ReservationDao] Réservation créée : ID=%d, Motif='%s'%n", o.getId(), o.getMotif());
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ignored) {}
            }
            System.err.println("  [ReservationDao Error] Échec de création : " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    @Override
    public boolean update(Reservation o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.merge(o);
            tx.commit();
            System.out.printf("  [ReservationDao] Réservation mise à jour : ID=%d, Statut=%s%n", o.getId(), o.getStatut());
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ignored) {}
            }
            System.err.println("  [ReservationDao Error] Échec de mise à jour : " + e.getMessage());
            return false;
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    @Override
    public boolean delete(Reservation o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Reservation managed = session.get(Reservation.class, o.getId());
            if (managed != null) {
                session.remove(managed);
                tx.commit();
                System.out.printf("  [ReservationDao] Réservation supprimée : ID=%d%n", o.getId());
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ignored) {}
            }
            System.err.println("  [ReservationDao Error] Échec de suppression : " + e.getMessage());
            return false;
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    @Override
    public Reservation findById(Long id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Reservation> q = session.createQuery(
                    "select r from Reservation r join fetch r.utilisateur join fetch r.salle where r.id = :id",
                    Reservation.class);
            q.setParameter("id", id);
            return q.uniqueResult();
        } catch (Exception e) {
            System.err.println("  [ReservationDao Error] Erreur findById : " + e.getMessage());
            return null;
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    @Override
    public List<Reservation> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "select r from Reservation r join fetch r.utilisateur join fetch r.salle order by r.dateDebut asc",
                    Reservation.class).list();
        } catch (Exception e) {
            System.err.println("  [ReservationDao Error] Erreur findAll : " + e.getMessage());
            return Collections.emptyList();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    /**
     * Recherche les réservations associées à une salle spécifique.
     */
    public List<Reservation> findBySalle(Salle salle) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Reservation> q = session.createQuery(
                    "select r from Reservation r join fetch r.utilisateur join fetch r.salle " +
                            "where r.salle.id = :salleId order by r.dateDebut asc",
                    Reservation.class);
            q.setParameter("salleId", salle.getId());
            return q.list();
        } catch (Exception e) {
            System.err.println("  [ReservationDao Error] Erreur findBySalle : " + e.getMessage());
            return Collections.emptyList();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    /**
     * Recherche les réservations effectuées par un utilisateur spécifique.
     */
    public List<Reservation> findByUtilisateur(Utilisateur utilisateur) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Reservation> q = session.createQuery(
                    "select r from Reservation r join fetch r.utilisateur join fetch r.salle " +
                            "where r.utilisateur.id = :userId order by r.dateDebut asc",
                    Reservation.class);
            q.setParameter("userId", utilisateur.getId());
            return q.list();
        } catch (Exception e) {
            System.err.println("  [ReservationDao Error] Erreur findByUtilisateur : " + e.getMessage());
            return Collections.emptyList();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    /**
     * Vérifie si un créneau horaire est déjà occupé pour une salle donnée (détection de conflit).
     */
    public boolean hasConflict(Salle salle, LocalDateTime debut, LocalDateTime fin) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Long> q = session.createQuery(
                    "select count(r) from Reservation r " +
                            "where r.salle.id = :salleId " +
                            "and r.statut = :statut " +
                            "and (r.dateDebut < :fin and r.dateFin > :debut)",
                    Long.class);
            q.setParameter("salleId", salle.getId());
            q.setParameter("statut", StatutReservation.CONFIRMEE);
            q.setParameter("debut", debut);
            q.setParameter("fin", fin);
            Long count = q.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            System.err.println("  [ReservationDao Error] Erreur hasConflict : " + e.getMessage());
            return false;
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }
}
