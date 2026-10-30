package com.tp.dao;

import com.tp.model.Equipement;
import com.tp.model.Salle;
import com.tp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DAO pour l'entité Salle avec support des associations ManyToMany et OneToMany.
 */
public class SalleDao implements IDao<Salle> {

    @Override
    public boolean create(Salle o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Si des équipements détachés sont passés, on les recharge dans la session courante
            if (o.getEquipements() != null && !o.getEquipements().isEmpty()) {
                Set<Equipement> managedEquipements = new HashSet<>();
                for (Equipement eq : o.getEquipements()) {
                    if (eq.getId() != null) {
                        Equipement mEq = session.get(Equipement.class, eq.getId());
                        if (mEq != null) {
                            managedEquipements.add(mEq);
                        }
                    } else {
                        session.persist(eq);
                        managedEquipements.add(eq);
                    }
                }
                o.setEquipements(managedEquipements);
            }

            session.persist(o);
            tx.commit();
            System.out.printf("  [SalleDao] Salle créée : ID=%d, Code='%s'%n", o.getId(), o.getCode());
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ignored) {}
            }
            System.err.println("  [SalleDao Error] Échec de création : " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public boolean update(Salle o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.merge(o);
            tx.commit();
            System.out.printf("  [SalleDao] Salle mise à jour : ID=%d, Code='%s'%n", o.getId(), o.getCode());
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ignored) {}
            }
            System.err.println("  [SalleDao Error] Échec de mise à jour : " + e.getMessage());
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public boolean delete(Salle o) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Salle managed = session.get(Salle.class, o.getId());
            if (managed != null) {
                session.remove(managed);
                tx.commit();
                System.out.printf("  [SalleDao] Salle supprimée : ID=%d%n", o.getId());
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ignored) {}
            }
            System.err.println("  [SalleDao Error] Échec de suppression : " + e.getMessage());
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public Salle findById(Long id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Salle.class, id);
        } catch (Exception e) {
            System.err.println("  [SalleDao Error] Erreur findById : " + e.getMessage());
            return null;
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    /**
     * Récupère une salle avec sa collection d'équipements chargée via un JOIN FETCH.
     */
    public Salle findByIdWithEquipements(Long id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Salle> q = session.createQuery(
                    "select distinct s from Salle s left join fetch s.equipements where s.id = :id", Salle.class);
            q.setParameter("id", id);
            return q.uniqueResult();
        } catch (Exception e) {
            System.err.println("  [SalleDao Error] Erreur findByIdWithEquipements : " + e.getMessage());
            return null;
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    public Salle findByCode(String code) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Salle> q = session.createQuery("from Salle s where s.code = :code", Salle.class);
            q.setParameter("code", code);
            return q.uniqueResult();
        } catch (Exception e) {
            System.err.println("  [SalleDao Error] Erreur findByCode : " + e.getMessage());
            return null;
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }

    @Override
    public List<Salle> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("select distinct s from Salle s left join fetch s.equipements order by s.id asc", Salle.class).list();
        } catch (Exception e) {
            System.err.println("  [SalleDao Error] Erreur findAll : " + e.getMessage());
            return Collections.emptyList();
        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }
}
