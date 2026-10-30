package com.tp.dao;

import com.tp.model.Equipement;
import com.tp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

/**
 * DAO pour l'entité Equipement.
 */
public class EquipementDao implements IDao<Equipement> {

    @Override
    public boolean create(Equipement o) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(o);
            tx.commit();
            System.out.printf("  [EquipementDao] Équipement créé : ID=%d, Nom='%s'%n", o.getId(), o.getNom());
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            System.err.println("  [EquipementDao Error] Échec de création : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Equipement o) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(o);
            tx.commit();
            System.out.printf("  [EquipementDao] Équipement mis à jour : ID=%d, Nom='%s'%n", o.getId(), o.getNom());
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            System.err.println("  [EquipementDao Error] Échec de mise à jour : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Equipement o) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Equipement managed = session.get(Equipement.class, o.getId());
            if (managed != null) {
                session.remove(managed);
                tx.commit();
                System.out.printf("  [EquipementDao] Équipement supprimé : ID=%d%n", o.getId());
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            System.err.println("  [EquipementDao Error] Échec de suppression : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Equipement findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Equipement.class, id);
        } catch (Exception e) {
            System.err.println("  [EquipementDao Error] Erreur findById : " + e.getMessage());
            return null;
        }
    }

    public Equipement findByCode(String code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Equipement> q = session.createQuery("from Equipement e where e.code = :code", Equipement.class);
            q.setParameter("code", code);
            return q.uniqueResult();
        } catch (Exception e) {
            System.err.println("  [EquipementDao Error] Erreur findByCode : " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Equipement> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Equipement order by id asc", Equipement.class).list();
        } catch (Exception e) {
            System.err.println("  [EquipementDao Error] Erreur findAll : " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
