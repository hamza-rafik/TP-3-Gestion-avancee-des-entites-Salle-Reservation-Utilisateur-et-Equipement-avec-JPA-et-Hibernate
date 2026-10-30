package com.tp.dao;

import com.tp.model.Utilisateur;
import com.tp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

/**
 * DAO pour l'entité Utilisateur.
 */
public class UtilisateurDao implements IDao<Utilisateur> {

    @Override
    public boolean create(Utilisateur o) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(o);
            tx.commit();
            System.out.printf("  [UtilisateurDao] Utilisateur créé : ID=%d, Nom='%s %s'%n",
                    o.getId(), o.getPrenom(), o.getNom());
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            System.err.println("  [UtilisateurDao Error] Échec de création : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Utilisateur o) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(o);
            tx.commit();
            System.out.printf("  [UtilisateurDao] Utilisateur mis à jour : ID=%d, Nom='%s %s'%n",
                    o.getId(), o.getPrenom(), o.getNom());
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            System.err.println("  [UtilisateurDao Error] Échec de mise à jour : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Utilisateur o) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Utilisateur managed = session.get(Utilisateur.class, o.getId());
            if (managed != null) {
                session.remove(managed);
                tx.commit();
                System.out.printf("  [UtilisateurDao] Utilisateur supprimé : ID=%d%n", o.getId());
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            System.err.println("  [UtilisateurDao Error] Échec de suppression : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Utilisateur findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Utilisateur.class, id);
        } catch (Exception e) {
            System.err.println("  [UtilisateurDao Error] Erreur findById : " + e.getMessage());
            return null;
        }
    }

    public Utilisateur findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Utilisateur> q = session.createQuery("from Utilisateur u where u.email = :email", Utilisateur.class);
            q.setParameter("email", email);
            return q.uniqueResult();
        } catch (Exception e) {
            System.err.println("  [UtilisateurDao Error] Erreur findByEmail : " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Utilisateur> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Utilisateur order by id asc", Utilisateur.class).list();
        } catch (Exception e) {
            System.err.println("  [UtilisateurDao Error] Erreur findAll : " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
