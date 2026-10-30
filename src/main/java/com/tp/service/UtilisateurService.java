package com.tp.service;

import com.tp.dao.UtilisateurDao;

/**
 * Alias français de UserService.
 */
public class UtilisateurService extends UserService {

    public UtilisateurService() {
        super();
    }

    public UtilisateurService(UtilisateurDao dao) {
        super(dao);
    }
}
