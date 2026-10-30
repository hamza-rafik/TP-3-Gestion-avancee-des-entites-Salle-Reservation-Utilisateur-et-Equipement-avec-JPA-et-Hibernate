package com.tp.service;

import com.tp.dao.IDao;
import com.tp.dao.UtilisateurDao;
import com.tp.model.Utilisateur;

import java.util.List;

/**
 * Couche service pour l'entité Utilisateur.
 */
public class UserService implements IDao<Utilisateur> {

    private final UtilisateurDao utilisateurDao;

    public UserService() {
        this.utilisateurDao = new UtilisateurDao();
    }

    public UserService(UtilisateurDao utilisateurDao) {
        this.utilisateurDao = utilisateurDao;
    }

    @Override
    public boolean create(Utilisateur o) {
        if (o == null || o.getEmail() == null || o.getEmail().trim().isEmpty()) {
            System.err.println("  [Validation] L'email de l'utilisateur est obligatoire.");
            return false;
        }
        return utilisateurDao.create(o);
    }

    @Override
    public boolean update(Utilisateur o) {
        if (o == null || o.getId() == null) return false;
        return utilisateurDao.update(o);
    }

    @Override
    public boolean delete(Utilisateur o) {
        if (o == null || o.getId() == null) return false;
        return utilisateurDao.delete(o);
    }

    @Override
    public Utilisateur findById(Long id) {
        if (id == null) return null;
        return utilisateurDao.findById(id);
    }

    public Utilisateur findByEmail(String email) {
        if (email == null) return null;
        return utilisateurDao.findByEmail(email);
    }

    @Override
    public List<Utilisateur> findAll() {
        return utilisateurDao.findAll();
    }
}
