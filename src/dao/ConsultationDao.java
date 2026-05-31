package dao;

import java.sql.*;
import java.util.Scanner;
import util.ConnexionBDD;

public class ConsultationDao {
    private Connection conn;
    private Scanner scanner;

    public ConsultationDao() throws SQLException {
        this.conn = ConnexionBDD.getConnection();
        this.scanner = new Scanner(System.in);
    }

    // Recherche d'un joueur par mot clé
    public void rechercherParMotCle() {
        System.out.print("Entrez un mot clé (pseudo ou nom d'équipe) : ");
        String saisie = scanner.nextLine().trim();

        if (saisie.isEmpty()) {
            System.out.println("La recherche ne peut pas être vide.");
            return;
        }
        String motCle = "%" + saisie + "%";

        System.out.println("\n=== RÉSULTATS DANS LES JOUEURS ===");
        String sqlJoueurs = "SELECT * FROM Joueurs WHERE pseudo LIKE ? OR nom_joueur LIKE ? OR prenom_joueur LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlJoueurs)) {
            pstmt.setString(1, motCle);
            pstmt.setString(2, motCle);
            pstmt.setString(3, motCle);

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean trouve = false;
                while (rs.next()) {
                    trouve = true;
                    System.out.println("[Joueur] Pseudo : " + rs.getString("pseudo") +
                            " (" + rs.getString("prenom_joueur") + " " + rs.getString("nom_joueur") +
                            ") | Nationalité : " + rs.getString("nationalite") +
                            " | Elo : " + rs.getInt("niveau_elo"));
                }
                if (!trouve) System.out.println("Aucun joueur trouver");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL (Recherche Joueurs) : " + e.getMessage());
        }

        System.out.println("\n=== RÉSULTATS DANS LES ÉQUIPES ===");
        String sqlEquipes = "SELECT * FROM Equipe WHERE nom_equipe LIKE ? OR pays_origine LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlEquipes)) {
            pstmt.setString(1, motCle);
            pstmt.setString(2, motCle);

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean trouve = false;
                while (rs.next()) {
                    trouve = true;
                    System.out.println("[Équipe] Nom : " + rs.getString("nom_equipe") +
                            " | Pays d'origine : " + rs.getString("pays_origine") +
                            " | Création : " + rs.getDate("date_creation"));
                }
                if (!trouve) System.out.println("Aucune équipe trouver.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL (Recherche Équipes) : " + e.getMessage());
        }
    }

    // Statistiques équipe
    public void afficherStatsEquipe() {
        System.out.print("Entrez l'ID de l'équipe pour voir ses statistiques : ");
        String inputId = scanner.nextLine().trim();

        if (inputId.isEmpty()) {
            System.out.println("L'ID ne peut pas être vide.");
            return;
        }

        int idEquipe;
        try {
            idEquipe = Integer.parseInt(inputId);
        } catch (NumberFormatException e) {
            System.out.println("Veuillez saisir un nombre entier valide pour l'ID.");
            return;
        }

        String sql = """
            SELECT 
                COUNT(*) AS total_matchs,
                SUM(CASE 
                    WHEN (id_equipe_1 = ? AND resultat = 'Equipe 1') OR (id_equipe_2 = ? AND resultat = 'Equipe 2') THEN 1 
                    ELSE 0 
                END) AS victoires
            FROM `Match`
            WHERE id_equipe_1 = ? OR id_equipe_2 = ?;
            """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEquipe);
            pstmt.setInt(2, idEquipe);
            pstmt.setInt(3, idEquipe);
            pstmt.setInt(4, idEquipe);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int totalMatchs = rs.getInt("total_matchs");

                    if (totalMatchs == 0) {
                        System.out.println("\nAucun match trouver");
                        return;
                    }

                    int victoires = rs.getInt("victoires");
                    int defaites = totalMatchs - victoires;

                    System.out.println("\n=== STATISTIQUES GLOBALES ÉQUIPE ===");
                    System.out.println("Nombre total de matchs joués : " + totalMatchs);
                    System.out.println("Nombre de victoires : " + victoires);
                    System.out.println("Nombre de défaites : " + defaites);

                    double winrate = ((double) victoires / totalMatchs) * 100;
                    System.out.printf("Taux de victoire : %.2f%%\n", winrate);
                    System.out.println("==============================================");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors du calcul des statistiques de l'équipe : " + e.getMessage());
        }
    }

    // Classement
    public void afficherClassementTournoi() {
        System.out.print("Entrez l'ID du tournoi : ");
        String inputId = scanner.nextLine().trim();

        if (inputId.isEmpty()) {
            System.out.println("L'ID ne peut pas être vide.");
            return;
        }

        int idTournoi;
        try {
            idTournoi = Integer.parseInt(inputId);
        } catch (NumberFormatException e) {
            System.out.println("Veuillez saisir un nombre entier valide pour l'ID du tournoi.");
            return;
        }

        String sql = """
            SELECT e.nom_equipe,
                   SUM(
                       CASE 
                           WHEN (m.resultat = 'Equipe 1' AND e.id_equipe = m.id_equipe_1) THEN 1
                           WHEN (m.resultat = 'Equipe 2' AND e.id_equipe = m.id_equipe_2) THEN 1
                           ELSE 0
                       END
                   ) AS victoires
            FROM Equipe e
            JOIN `Match` m ON e.id_equipe = m.id_equipe_1 OR e.id_equipe = m.id_equipe_2
            JOIN Phase p ON m.id_phase = p.id_phase
            WHERE p.id_tournoi = ?
            GROUP BY e.id_equipe, e.nom_equipe
            ORDER BY victoires DESC;
            """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idTournoi);

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n=== CLASSEMENT DU TOURNOI ===");

                boolean aDesResultats = false;
                int rang = 1;

                while (rs.next()) {
                    aDesResultats = true;
                    String nomEquipe = rs.getString("nom_equipe");
                    int victoires = rs.getInt("victoires");

                    System.out.println(rang + ". " + nomEquipe + " | " + victoires + " victoire(s)");
                    rang++;
                }

                if (!aDesResultats) {
                    System.out.println("Aucun match ou classement disponible pour ce tournoi.");
                }
                System.out.println("==============================================");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de l'affichage du classement : " + e.getMessage());
        }
    }

    // Palmarès
    public void afficherPalmaresJoueur() {
        System.out.print("Entrez le pseudo : ");
        String pseudo = scanner.nextLine().trim();

        if (pseudo.isEmpty()) {
            System.out.println("Le pseudo ne peut pas être vide.");
            return;
        }

        String sql = """
            SELECT t.nom_tournoi, 
                   COUNT(DISTINCT m.id_match) AS matchs_joues,
                   SUM(
                       CASE 
                           WHEN (m.resultat = 'Equipe 1' AND r.id_equipe = m.id_equipe_1) THEN 1
                           WHEN (m.resultat = 'Equipe 2' AND r.id_equipe = m.id_equipe_2) THEN 1
                           ELSE 0
                       END
                   ) AS matchs_gagnes
            FROM Joueurs j
            JOIN Statistiques s ON j.id_joueur = s.id_joueur
            JOIN `Match` m ON s.id_match = m.id_match
            JOIN Phase p ON m.id_phase = p.id_phase
            JOIN Tournoi t ON p.id_tournoi = t.id_tournoi
            JOIN Roster r ON j.id_joueur = r.id_joueur AND r.id_jeu = t.id_jeu
            WHERE j.pseudo = ?
            GROUP BY t.id_tournoi, t.nom_tournoi;
            """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pseudo);

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n=== PALMARÈS DU JOUEUR : " + pseudo + " ===");

                boolean aDesDonnees = false;
                while (rs.next()) {
                    aDesDonnees = true;
                    String nomTournoi = rs.getString("nom_tournoi");
                    int joues = rs.getInt("matchs_joues");
                    int gagnes = rs.getInt("matchs_gagnes");

                    System.out.println("- Tournoi : " + nomTournoi);
                    System.out.println("-> Matchs disputés : " + joues);
                    System.out.println("-> Victoires : " + gagnes);
                }

                if (!aDesDonnees) {
                    System.out.println("Aucune statistique ou tournoi enregistré pour ce joueur.");
                }
                System.out.println("==============================================");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la récupération du palmarès : " + e.getMessage());
        }
    }

    // TEST !
    public static void main(String[] args) {
        try {
            ConsultationDao dao = new ConsultationDao();
            dao.afficherPalmaresJoueur();
        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
    }
}