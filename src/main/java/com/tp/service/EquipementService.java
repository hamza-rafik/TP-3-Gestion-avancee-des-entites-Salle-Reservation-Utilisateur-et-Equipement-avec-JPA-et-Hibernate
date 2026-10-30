package com.tp.service;

import com.tp.dao.EquipementDao;
import com.tp.dao.IDao;
import com.tp.model.Equipement;

import java.util.List;

/**
 * Couche service pour l'entité Equipement.
 */
public class EquipementService implements IDao<Equipement> {

    private final EquipementDao equipementDao;

    public EquipementService() {
        this.equipementDao = new EquipementDao();
    }

    public EquipementService(EquipementDao equipementDao) {
        this.equipementDao = equipementDao;
    }

    @Override
    public boolean create(Equipement o) {
        if (o == null || o.getNom() == null || o.getNom().trim().isEmpty()) {
            System.err.println("  [Validation] Le nom de l'équipement est obligatoire.");
            return false;
        }
        return equipementDao.create(o);
    }

    @Override
    public boolean update(Equipement o) {
        if (o == null || o.getId() == null) return false;
        return equipementDao.update(o);
    }

    @Override
    public boolean delete(Equipement o) {
        if (o == null || o.getId() == null) return false;
        return equipementDao.delete(o);
    }

    @Override
    public Equipement findById(Long id) {
        if (id == null) return null;
        return equipementDao.findById(id);
    }

    public Equipement findByCode(String code) {
        if (code == null) return null;
        return equipementDao.findByCode(code);
    }

    @Override
    public List<Equipement> findAll() {
        return equipementDao.findAll();
    }
}
