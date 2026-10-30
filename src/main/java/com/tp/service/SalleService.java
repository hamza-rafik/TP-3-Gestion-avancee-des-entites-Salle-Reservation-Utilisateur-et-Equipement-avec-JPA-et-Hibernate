package com.tp.service;

import com.tp.dao.IDao;
import com.tp.dao.SalleDao;
import com.tp.model.Equipement;
import com.tp.model.Salle;

import java.util.List;

/**
 * Couche service pour l'entité Salle.
 */
public class SalleService implements IDao<Salle> {

    private final SalleDao salleDao;

    public SalleService() {
        this.salleDao = new SalleDao();
    }

    public SalleService(SalleDao salleDao) {
        this.salleDao = salleDao;
    }

    @Override
    public boolean create(Salle o) {
        if (o == null || o.getCode() == null || o.getCode().trim().isEmpty()) {
            System.err.println("  [Validation] Le code de la salle est obligatoire.");
            return false;
        }
        return salleDao.create(o);
    }

    @Override
    public boolean update(Salle o) {
        if (o == null || o.getId() == null) return false;
        return salleDao.update(o);
    }

    @Override
    public boolean delete(Salle o) {
        if (o == null || o.getId() == null) return false;
        return salleDao.delete(o);
    }

    @Override
    public Salle findById(Long id) {
        if (id == null) return null;
        return salleDao.findById(id);
    }

    public Salle findByIdWithEquipements(Long id) {
        if (id == null) return null;
        return salleDao.findByIdWithEquipements(id);
    }

    public Salle findByCode(String code) {
        if (code == null) return null;
        return salleDao.findByCode(code);
    }

    /**
     * Associe un équipement à une salle.
     */
    public boolean associerEquipement(Salle salle, Equipement equipement) {
        if (salle == null || equipement == null) return false;
        salle.addEquipement(equipement);
        return salleDao.update(salle);
    }

    @Override
    public List<Salle> findAll() {
        return salleDao.findAll();
    }
}
