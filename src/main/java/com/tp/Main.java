package com.tp;

import com.tp.model.*;
import com.tp.service.*;
import com.tp.util.HibernateUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Application principale du :
 * TP 3 : Gestion avancée des entités Salle, Réservation, Utilisateur et Équipement avec JPA et Hibernate.
 */
public class Main {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        afficherBanniere();

        EquipementService equipementService = new EquipementService();
        SalleService salleService = new SalleService();
        UserService userService = new UserService();
        ReservationService reservationService = new ReservationService();

        // ====================================================================
        // ÉTAPE 1 : CRÉATION ET INSERTION DES ÉQUIPEMENTS (CREATE)
        // ====================================================================
        System.out.println("\n==========================================================================================");
        System.out.println(">>> ÉTAPE 1 : Insertion des entités Équipement en base de données H2");
        System.out.println("==========================================================================================");

        Equipement eq1 = new Equipement("EQ-VID-4K", "Vidéoprojecteur Laser 4K Ultra-HD", "Audiovisuel", "Opérationnel");
        Equipement eq2 = new Equipement("EQ-TBI-85", "Tableau Blanc Interactif 85 pouces", "Informatique", "Opérationnel");
        Equipement eq3 = new Equipement("EQ-VIS-POLY", "Système Visioconférence Polycom 360°", "Télécom", "Opérationnel");
        Equipement eq4 = new Equipement("EQ-SON-HF", "Système Sonore Ambiophonique & Micros HF", "Audiovisuel", "Opérationnel");
        Equipement eq5 = new Equipement("EQ-CLI-ECO", "Climatisation Réversible Intelligente", "Domotique", "Opérationnel");

        equipementService.create(eq1);
        equipementService.create(eq2);
        equipementService.create(eq3);
        equipementService.create(eq4);
        equipementService.create(eq5);

        // ====================================================================
        // ÉTAPE 2 : INSERTION DES SALLES ET ASSOCIATION D'ÉQUIPEMENTS (ManyToMany)
        // ====================================================================
        System.out.println("\n==========================================================================================");
        System.out.println(">>> ÉTAPE 2 : Insertion des Salles et association d'équipements (Association ManyToMany)");
        System.out.println("==========================================================================================");

        Salle s1 = new Salle("S-101", "Amphithéâtre Ibn Battuta", 250, "Rez-de-chaussée");
        s1.addEquipement(eq1);
        s1.addEquipement(eq4);
        s1.addEquipement(eq5);

        Salle s2 = new Salle("LAB-201", "Laboratoire Intelligence Artificielle & Big Data", 40, "1er Étage");
        s2.addEquipement(eq1);
        s2.addEquipement(eq2);
        s2.addEquipement(eq5);

        Salle s3 = new Salle("CONF-301", "Salle de Conférence Internationale Atlas", 80, "2ème Étage");
        s3.addEquipement(eq1);
        s3.addEquipement(eq3);
        s3.addEquipement(eq4);

        Salle s4 = new Salle("SEM-401", "Salle de Séminaire Toubkal", 30, "3ème Étage");
        s4.addEquipement(eq2);

        salleService.create(s1);
        salleService.create(s2);
        salleService.create(s3);
        salleService.create(s4);

        // ====================================================================
        // ÉTAPE 3 : INSERTION DES UTILISATEURS (CREATE)
        // ====================================================================
        System.out.println("\n==========================================================================================");
        System.out.println(">>> ÉTAPE 3 : Insertion des Utilisateurs du système");
        System.out.println("==========================================================================================");

        Utilisateur u1 = new Utilisateur("Rafik", "Hamza", "hamza.rafik@example.com", "+212 600-112233", "Lead Software Engineer");
        Utilisateur u2 = new Utilisateur("Ait Said", "Oumaima", "oumaima.aitsaid@example.com", "+212 611-445566", "Ingénieur Manager & Instructrice");
        Utilisateur u3 = new Utilisateur("Bennani", "Karim", "karim.bennani@example.com", "+212 622-778899", "Responsable Laboratoire IA");
        Utilisateur u4 = new Utilisateur("Alami", "Sofia", "sofia.alami@example.com", "+212 633-990011", "Chercheuse Data Science");

        userService.create(u1);
        userService.create(u2);
        userService.create(u3);
        userService.create(u4);

        // ====================================================================
        // ÉTAPE 4 : CRÉATION DES RÉSERVATIONS (ManyToOne Utilisateur & Salle)
        // ====================================================================
        System.out.println("\n==========================================================================================");
        System.out.println(">>> ÉTAPE 4 : Création et persistance des Réservations (Relations ManyToOne)");
        System.out.println("==========================================================================================");

        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);

        Reservation r1 = new Reservation(
                now.plusDays(1).withHour(9),
                now.plusDays(1).withHour(12),
                "Atelier Pratique Deep Learning & Agents IA",
                StatutReservation.CONFIRMEE,
                u1, s2
        );

        Reservation r2 = new Reservation(
                now.plusDays(1).withHour(14),
                now.plusDays(1).withHour(17),
                "Keynote Hybride : Avenir de l'IA Générative",
                StatutReservation.CONFIRMEE,
                u2, s3
        );

        Reservation r3 = new Reservation(
                now.plusDays(2).withHour(10),
                now.plusDays(2).withHour(13),
                "Soutenance des Projets de Fin d'Études",
                StatutReservation.CONFIRMEE,
                u3, s1
        );

        Reservation r4 = new Reservation(
                now.plusDays(3).withHour(14),
                now.plusDays(3).withHour(16),
                "Séance de Travaux Pratiques Data Mining",
                StatutReservation.EN_ATTENTE,
                u4, s2
        );

        reservationService.create(r1);
        reservationService.create(r2);
        reservationService.create(r3);
        reservationService.create(r4);

        // ====================================================================
        // ÉTAPE 5 : DÉTECTION ET PRÉVENTION DE CONFLIT DE RÉSERVATION
        // ====================================================================
        System.out.println("\n==========================================================================================");
        System.out.println(">>> ÉTAPE 5 : Test de prévention des conflits (Tentative de double réservation)");
        System.out.println("==========================================================================================");

        System.out.println("  [Test Conflit] Tentative de réservation de la salle LAB-201 sur un créneau chevauchant (10h-11h)...");
        Reservation rConflit = new Reservation(
                now.plusDays(1).withHour(10),
                now.plusDays(1).withHour(11),
                "Réunion non autorisée sur créneau occupé",
                StatutReservation.CONFIRMEE,
                u4, s2
        );
        boolean conflitSucces = reservationService.create(rConflit);
        if (!conflitSucces) {
            System.out.println("  [Succès du Contrôle] Le système a bloqué la réservation avec succès pour cause de conflit !");
        }

        // ====================================================================
        // ÉTAPE 6 : AFFICHAGE DES ÉQUIPEMENTS (READ ALL)
        // ====================================================================
        System.out.println("\n==========================================================================================");
        System.out.println(">>> ÉTAPE 6 : Liste des Équipements répertoriés (HQL : from Equipement)");
        System.out.println("==========================================================================================");
        afficherTableauEquipements(equipementService.findAll());

        // ====================================================================
        // ÉTAPE 7 : AFFICHAGE DES SALLES AVEC LEURS ÉQUIPEMENTS (ManyToMany)
        // ====================================================================
        System.out.println("\n==========================================================================================");
        System.out.println(">>> ÉTAPE 7 : Liste des Salles et Équipements associés (JOIN FETCH)");
        System.out.println("==========================================================================================");
        afficherTableauSallesAvecEquipements(salleService.findAll());

        // ====================================================================
        // ÉTAPE 8 : AFFICHAGE DES RÉSERVATIONS DÉTAILLÉES (READ ALL)
        // ====================================================================
        System.out.println("\n==========================================================================================");
        System.out.println(">>> ÉTAPE 8 : Liste complète des Réservations actives (Jointures EAGER / HQL)");
        System.out.println("==========================================================================================");
        afficherTableauReservations(reservationService.findAll());

        // ====================================================================
        // ÉTAPE 9 : MISE À JOUR D'UNE RÉSERVATION ET D'UNE SALLE (UPDATE)
        // ====================================================================
        System.out.println("\n==========================================================================================");
        System.out.println(">>> ÉTAPE 9 : Opérations de mise à jour (UPDATE)");
        System.out.println("==========================================================================================");

        Reservation resAModifier = reservationService.findById(4L);
        if (resAModifier != null) {
            System.out.println("  Avant modification : " + resAModifier);
            resAModifier.setStatut(StatutReservation.CONFIRMEE);
            resAModifier.setMotif("Séance TP Data Mining & Machine Learning (Validée)");
            reservationService.update(resAModifier);
            System.out.println("  Après validation   : " + reservationService.findById(4L));
        }

        // ====================================================================
        // ÉTAPE 10 : SUPPRESSION D'UNE RÉSERVATION (DELETE)
        // ====================================================================
        System.out.println("\n==========================================================================================");
        System.out.println(">>> ÉTAPE 10 : Suppression d'une réservation (DELETE)");
        System.out.println("==========================================================================================");

        Reservation resASupprimer = reservationService.findById(3L);
        if (resASupprimer != null) {
            System.out.println("  Suppression de la réservation : ID=" + resASupprimer.getId() + " - " + resASupprimer.getMotif());
            reservationService.delete(resASupprimer);
        }

        // ====================================================================
        // ÉTAPE 11 : ÉTAT FINAL DE LA BASE DE DONNÉES
        // ====================================================================
        System.out.println("\n==========================================================================================");
        System.out.println(">>> ÉTAPE 11 : État final des Réservations après suppression");
        System.out.println("==========================================================================================");
        afficherTableauReservations(reservationService.findAll());

        System.out.println("\n==========================================================================================");
        System.out.println(" [SUCCÈS] Exécution complète du TP 3 validée avec succès sans aucune erreur !");
        System.out.println("==========================================================================================");

        HibernateUtil.shutdown();
    }

    private static void afficherBanniere() {
        System.out.println("************************************************************************************************");
        System.out.println("*                                                                                              *");
        System.out.println("*   TP 3 : Gestion avancée des entités Salle, Réservation, Utilisateur et Équipement           *");
        System.out.println("*   Technologies : Hibernate ORM 6.4 | Jakarta Persistence | Base de données H2 In-Memory      *");
        System.out.println("*   Associations : ManyToOne / OneToMany (Réservations) | ManyToMany (Salles - Équipements)   *");
        System.out.println("*                                                                                              *");
        System.out.println("************************************************************************************************");
    }

    private static void afficherTableauEquipements(List<Equipement> liste) {
        String format = "  | %-4s | %-14s | %-42s | %-16s | %-14s |%n";
        String sep = "  +------+----------------+--------------------------------------------+------------------+----------------+";
        System.out.println(sep);
        System.out.printf(format, "ID", "Code", "Nom de l'Équipement", "Catégorie", "État");
        System.out.println(sep);
        for (Equipement e : liste) {
            System.out.printf(format, e.getId(), e.getCode(), e.getNom(), e.getType(), e.getEtat());
        }
        System.out.println(sep);
        System.out.printf("  Total : %d équipement(s) répertorié(s).%n", liste.size());
    }

    private static void afficherTableauSallesAvecEquipements(List<Salle> liste) {
        String format = "  | %-4s | %-10s | %-38s | %-8s | %-45s |%n";
        String sep = "  +------+------------+----------------------------------------+----------+-----------------------------------------------+";
        System.out.println(sep);
        System.out.printf(format, "ID", "Code", "Libellé de la Salle", "Capacité", "Équipements Associés");
        System.out.println(sep);
        for (Salle s : liste) {
            String eqList = s.getEquipements().isEmpty() ? "Aucun équipement" :
                    s.getEquipements().stream().map(Equipement::getCode).collect(Collectors.joining(", "));
            System.out.printf(format, s.getId(), s.getCode(), s.getLibelle(), s.getCapacite() + " p.", eqList);
        }
        System.out.println(sep);
        System.out.printf("  Total : %d salle(s) répertoriée(s).%n", liste.size());
    }

    private static void afficherTableauReservations(List<Reservation> liste) {
        String format = "  | %-4s | %-10s | %-20s | %-16s | %-16s | %-12s | %-38s |%n";
        String sep = "  +------+------------+----------------------+------------------+------------------+--------------+----------------------------------------+";
        System.out.println(sep);
        System.out.printf(format, "ID", "Salle", "Utilisateur", "Début", "Fin", "Statut", "Motif de la Réservation");
        System.out.println(sep);
        for (Reservation r : liste) {
            String salleCode = (r.getSalle() != null) ? r.getSalle().getCode() : "N/A";
            String userName = (r.getUtilisateur() != null) ? (r.getUtilisateur().getPrenom() + " " + r.getUtilisateur().getNom()) : "N/A";
            System.out.printf(format, r.getId(), salleCode, userName,
                    r.getDateDebut().format(DATE_FMT), r.getDateFin().format(DATE_FMT),
                    r.getStatut(), r.getMotif());
        }
        System.out.println(sep);
        System.out.printf("  Total : %d réservation(s) répertoriée(s).%n", liste.size());
    }
}
