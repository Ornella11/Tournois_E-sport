package dao;

import modele.Tournoi;
import util.ConnexionBDD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class TournoiDAO {
    Connection conn = ConnexionBDD.getConnection();


    public static void main(String[] args) throws SQLException {
        TournoiDAO dao = new TournoiDAO();

        dao.inscriptionEquipe();
    }


    public TournoiDAO() throws SQLException {
    }


    public void ajouterTournoi() {
        Scanner scanner = new Scanner(System.in);
        final int MAX_TENTATIVES = 3;
        int tentatives = 0;
        boolean ajoutReussie = false;
        while (!ajoutReussie && tentatives < MAX_TENTATIVES) {
            try {
                System.out.print("Nom du tournoi: ");
                String nom = scanner.nextLine().trim();
                if (nom.isEmpty()) {
                    tentatives++;
                    throw new IllegalArgumentException("Le nom ne peut pas être vide.");
                }


                System.out.print("Date de début du tournoi: ");
                LocalDate date_debut = LocalDate.parse(scanner.nextLine().trim());

                System.out.print("Date de fin du tournoi: ");
                LocalDate date_fin = LocalDate.parse(scanner.nextLine().trim());

                System.out.print("Type : ");
                String type = scanner.nextLine().trim();

                System.out.print("Dotation : ");
                int dotation = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Statut : ");
                String statut = scanner.nextLine().trim();

                System.out.print("Id du jeu : ");
                int id_jeu = Integer.parseInt(scanner.nextLine().trim());

                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO Tournoi " +
                                "(nom_tournoi, date_debut_tournoi, date_fin_tournoi, type, dotation, statut, id_jeu) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)"
                );

                pstmt.setString(1, nom);
                pstmt.setString(2, String.valueOf(date_debut));
                pstmt.setString(3, String.valueOf(date_fin));
                pstmt.setString(4, type);
                pstmt.setInt(5, dotation);
                pstmt.setString(6, statut);
                pstmt.setInt(7, id_jeu);


                int rs = pstmt.executeUpdate();

                if (rs > 0) {
                    System.out.println("AJOUT REUSSI !");
                    ajoutReussie = true;
                } else {
                    tentatives++;
                    throw new SQLException("Aucune ligne insérée.");
                }
                pstmt.close();


            } catch (IllegalArgumentException e) {
                tentatives++;
                System.out.println("Erreur : " + e.getMessage());

            } catch (SQLException e) {
                tentatives++;
                System.out.println("Erreur SQL : " + e.getMessage());

            }
        }

    }

    public void inscriptionEquipe() {
        Scanner scanner = new Scanner(System.in);
        final int MAX_TENTATIVES = 3;
        int tentatives = 0;
        boolean ajoutReussie = false;
        while (!ajoutReussie && tentatives < MAX_TENTATIVES) {
            try {
                System.out.print("Id de l'équipe: ");
                Integer equipe = Integer.valueOf(scanner.nextLine().trim());

                System.out.print("Id du tournoi: ");
                Integer tournoi = Integer.valueOf(scanner.nextLine().trim());

                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO equipe_tournoi " +
                                "(id_equipe, id_tournoi, date_inscription) " +
                                "VALUES (?, ?, CURDATE())"
                );

                pstmt.setInt(1, equipe);
                pstmt.setInt(2, tournoi);


                int rs = pstmt.executeUpdate();

                if (rs > 0) {
                    System.out.println("AJOUT REUSSI !");
                    ajoutReussie = true;
                } else {
                    tentatives++;
                    throw new SQLException("Aucune ligne insérée.");
                }
                pstmt.close();


            } catch (IllegalArgumentException e) {
                tentatives++;
                System.out.println("Erreur : " + e.getMessage());

            } catch (SQLException e) {
                tentatives++;
                System.out.println("Erreur SQL : " + e.getMessage());

            }
        }

    }
}
