package dao;

import util.ConnexionBDD;

import java.sql.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class MatchDao {
    Connection conn = ConnexionBDD.getConnection();

    public MatchDao() throws SQLException {
    }


    public static void main(String[] args) throws SQLException {
        MatchDao dao = new MatchDao();

        dao.saisirStatistiqueIndividuelle();
    }

    public void saisirScoreMatch() {
        Scanner scanner = new Scanner(System.in);
        final int MAX_TENTATIVES = 3;
        int tentatives = 0;
        boolean reussie = false;

        while (!reussie && tentatives < MAX_TENTATIVES) {
            try {
                System.out.print("Id de la phase : ");
                int id_phase = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Id de l'équipe 1 : ");
                int id_equipe_1 = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Id de l'équipe 2 : ");
                int id_equipe_2 = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Score équipe 1 : ");
                int score1 = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Score équipe 2 : ");
                int score2 = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Nombre de maps : ");
                int nb_maps = Integer.parseInt(scanner.nextLine().trim());

                if (score1 < 0 || score2 < 0) {
                    throw new IllegalArgumentException("Les scores ne peuvent pas être négatifs.");
                }
                if (nb_maps <= 0) {
                    throw new IllegalArgumentException("Le nombre de maps doit être supérieur à 0.");
                }
                if (id_equipe_1 == id_equipe_2) {
                    throw new IllegalArgumentException("Les deux équipes doivent être différentes.");
                }

                String resultat;
                if (score1 > score2) resultat = "Equipe 1";
                else if (score2 > score1) resultat = "Equipe 2";
                else resultat = "Egalité";

                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO `Match` " +
                                "(date_match, score_equipe1, score_equipe2, resultat, nombre_maps, id_phase, id_equipe_1, id_equipe_2) " +
                                "VALUES (NOW(), ?, ?, ?, ?, ?, ?, ?)"
                );

                pstmt.setInt(1, score1);
                pstmt.setInt(2, score2);
                pstmt.setString(3, resultat);
                pstmt.setInt(4, nb_maps);
                pstmt.setInt(5, id_phase);
                pstmt.setInt(6, id_equipe_1);
                pstmt.setInt(7, id_equipe_2);

                int rs = pstmt.executeUpdate();

                if (rs > 0) {
                    System.out.println("Résultat inséré ");
                    reussie = true;
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

    public void saisirStatistiqueIndividuelle() {
        Scanner scanner = new Scanner(System.in);
        final int MAX_TENTATIVES = 3;
        int tentatives = 0;
        boolean reussie = false;

        while (!reussie && tentatives < MAX_TENTATIVES) {
            try {
                System.out.print("Id du joueur : ");
                int id_joueur = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Id du match : ");
                int id_match = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Nombre de kills : ");
                int nb_kills = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Nombre de deaths : ");
                int nb_deaths = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Nombre d'assists : ");
                int nb_assists = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Score : ");
                int score = Integer.parseInt(scanner.nextLine().trim());

                if (nb_kills < 0 || nb_deaths < 0 || nb_assists < 0 || score < 0) {
                    throw new IllegalArgumentException("Les statistiques ne peuvent pas être négatives.");
                }

                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO statistiques " +
                                "(id_joueur, id_match, nb_kills, nb_deaths, nb_assists, score) " +
                                "VALUES (?, ?, ?, ?, ?, ?)"
                );

                pstmt.setInt(1, id_joueur);
                pstmt.setInt(2, id_match);
                pstmt.setInt(3, nb_kills);
                pstmt.setInt(4, nb_deaths);
                pstmt.setInt(5, nb_assists);
                pstmt.setInt(6, score);

                int rs = pstmt.executeUpdate();

                if (rs > 0) {
                    System.out.println("Statistiques enregistrées !");
                    reussie = true;
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

    public void afficherStatistiquesMatch() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Id du match : ");
            int id_match = Integer.parseInt(scanner.nextLine().trim());

            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT j.pseudo, s.nb_kills, s.nb_deaths, s.nb_assists, s.score " +
                            "FROM Statistiques s " +
                            "JOIN Joueurs j ON s.id_joueur = j.id_joueur " +
                            "WHERE s.id_match = ?"
            );

            pstmt.setInt(1, id_match);
            ResultSet rs = pstmt.executeQuery();

            boolean found = false;
            System.out.println("\n--- Statistiques du match " + id_match + " ---");
            System.out.printf("%-20s %-10s %-10s %-10s %-10s%n",
                    "Joueur", "Kills", "Deaths", "Assists", "Score");
            System.out.println("-".repeat(60));

            while (rs.next()) {
                found = true;
                System.out.printf("%-20s %-10d %-10d %-10d %-10d%n",
                        rs.getString("pseudo"),
                        rs.getInt("nb_kills"),
                        rs.getInt("nb_deaths"),
                        rs.getInt("nb_assists"),
                        rs.getInt("score")
                );
            }

            if (!found) {
                System.out.println("Aucune statistique trouvée pour ce match.");
            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }
}
