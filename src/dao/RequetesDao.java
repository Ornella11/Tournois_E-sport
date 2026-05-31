package dao;

import java.sql.*;
import java.util.Scanner;
import util.ConnexionBDD;

public class RequetesDao {
    private Connection conn;
    private Scanner scanner;

    public RequetesDao() throws SQLException {
        this.conn = ConnexionBDD.getConnection();
        this.scanner = new Scanner(System.in);
    }

    // R1
    public void executionR1() {
        String sql = """
            SELECT pseudo, nom_joueur, prenom_joueur, nationalite
            FROM Joueurs
            ORDER BY pseudo ASC;
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n=== R1 : LISTE DES JOUEURS TRIÉS ===");
            while (rs.next()) {
                System.out.println(rs.getString("pseudo") + " | " + rs.getString("prenom_joueur") + " " + rs.getString("nom_joueur") + " | " + rs.getString("nationalite"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur R1 : " + e.getMessage());
        }
    }

    // R2
    public void executionR2() {
        String sql = """
            SELECT t.nom_tournoi, j.nom_jeu, t.dotation
            FROM Tournoi t
            JOIN Jeu j ON t.id_jeu = j.id_jeu
            WHERE t.dotation > 10000;
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n=== R2 : TOURNOIS DOTATION > 10000€ ===");
            while (rs.next()) {
                System.out.println(rs.getString("nom_tournoi") + " | Jeu : " + rs.getString("nom_jeu") + " | Dotation : " + rs.getInt("dotation") + "€");
            }
        } catch (SQLException e) {
            System.out.println("Erreur R2 : " + e.getMessage());
        }
    }

    // R3
    public void executionR3() {
        System.out.print("Entrez l'ID du tournoi : ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return;
        int idTournoi = Integer.parseInt(input);

        String sql = """
            SELECT m.date_match, e1.nom_equipe AS equipe1, e2.nom_equipe AS equipe2,
                   m.score_equipe1, m.score_equipe2
            FROM `Match` m
            JOIN Phase p ON m.id_phase = p.id_phase
            JOIN Tournoi t ON p.id_tournoi = t.id_tournoi
            JOIN Equipe e1 ON m.id_equipe_1 = e1.id_equipe
            JOIN Equipe e2 ON m.id_equipe_2 = e2.id_equipe
            WHERE t.id_tournoi = ?;
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idTournoi);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n=== R3 : MATCHS DU TOURNOI " + idTournoi + " ===");
                while (rs.next()) {
                    System.out.println(rs.getDate("date_match") + " | " + rs.getString("equipe1") + " " + rs.getInt("score_equipe1") + " - " + rs.getInt("score_equipe2") + " " + rs.getString("equipe2"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur R3 : " + e.getMessage());
        }
    }

    // R4 :
    public void executionR4() {
        System.out.print("Entrez l'ID de l'équipe : ");
        String inputEquipe = scanner.nextLine().trim();
        System.out.print("Entrez l'ID du jeu : ");
        String inputJeu = scanner.nextLine().trim();
        if (inputEquipe.isEmpty() || inputJeu.isEmpty()) return;
        int idEquipe = Integer.parseInt(inputEquipe);
        int idJeu = Integer.parseInt(inputJeu);

        String sql = """
            SELECT e.nom_equipe, j.pseudo, j.nom_joueur, j.prenom_joueur
            FROM Roster r
            JOIN Joueurs j ON r.id_joueur = j.id_joueur
            JOIN Equipe e ON r.id_equipe = e.id_equipe
            WHERE r.id_equipe = ? AND r.id_jeu = ?;
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEquipe);
            pstmt.setInt(2, idJeu);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n=== R4 : JOUEURS DE L'ÉQUIPE PAR JEU ===");
                while (rs.next()) {
                    System.out.println(rs.getString("nom_equipe") + " | " + rs.getString("pseudo") + " (" + rs.getString("prenom_joueur") + " " + rs.getString("nom_joueur") + ")");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur R4 : " + e.getMessage());
        }
    }

    // R5
    public void executionR5() {
        String sql = """
            SELECT DISTINCT t.nom_tournoi, t.dotation,
                   CASE 
                       WHEN m.resultat = 'Equipe 1' THEN e1.nom_equipe
                       WHEN m.resultat = 'Equipe 2' THEN e2.nom_equipe
                   END AS vainqueur
            FROM Tournoi t
            JOIN Phase p ON p.id_tournoi = t.id_tournoi
            JOIN `Match` m ON m.id_phase = p.id_phase
            JOIN Equipe e1 ON m.id_equipe_1 = e1.id_equipe
            JOIN Equipe e2 ON m.id_equipe_2 = e2.id_equipe
            WHERE t.statut = 'Terminé';
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n=== R5 : VAINQUEURS DES TOURNOIS TERMINÉS ===");
            while (rs.next()) {
                System.out.println("Tournoi : " + rs.getString("nom_tournoi") + " | Vainqueur : " + rs.getString("vainqueur") + " | Dotation : " + rs.getInt("dotation") + "€");
            }
        } catch (SQLException e) {
            System.out.println("Erreur R5 : " + e.getMessage());
        }
    }

    // R6
    public void executionR6() {
        String sql = """
            SELECT m.id_match, e.nom_equipe,
                   SUM(s.nb_kills) AS kills,
                   SUM(s.nb_deaths) AS deaths
            FROM Statistiques s
            JOIN `Match` m ON s.id_match = m.id_match
            JOIN Equipe e ON (e.id_equipe = m.id_equipe_1 OR e.id_equipe = m.id_equipe_2)
            GROUP BY m.id_match, e.id_equipe;
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n=== R6 : STATS AGRÉGÉES PAR ÉQUIPE ===");
            while (rs.next()) {
                System.out.println("Match ID : " + rs.getInt("id_match") + " | Équipe : " + rs.getString("nom_equipe") + " | Kills : " + rs.getInt("kills") + " | Deaths : " + rs.getInt("deaths"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur R6 : " + e.getMessage());
        }
    }

    // R7 :
    public void executionR7() {
        String sql = """
            SELECT j.nom_jeu, COUNT(t.id_tournoi) AS nb_tournois
            FROM Jeu j
            LEFT JOIN Tournoi t ON j.id_jeu = t.id_jeu
            GROUP BY j.id_jeu
            ORDER BY nb_tournois DESC;
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n=== R7 : NOMBRE DE TOURNOIS PAR JEU ===");
            while (rs.next()) {
                System.out.println("Jeu : " + rs.getString("nom_jeu") + " | Nombre de tournois : " + rs.getInt("nb_tournois"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur R7 : " + e.getMessage());
        }
    }

    // R8 : équipes ayant participé à au moins 2 tournois
    public void executionR8() {
        String sql = """
            SELECT e.nom_equipe
            FROM Equipe e
            JOIN `Match` m ON e.id_equipe = m.id_equipe_1 OR e.id_equipe = m.id_equipe_2
            JOIN Phase p ON m.id_phase = p.id_phase
            GROUP BY e.id_equipe
            HAVING COUNT(DISTINCT p.id_tournoi) >= 2;
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n=== R8 : ÉQUIPES PARTICIPANT À AU MOINS 2 TOURNOIS ===");
            while (rs.next()) {
                System.out.println("Équipe : " + rs.getString("nom_equipe"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur R8 : " + e.getMessage());
        }
    }

    // R9
    public void executionR9() {
        String sql = """
            SELECT j.pseudo,
                   SUM(s.nb_kills)/NULLIF(SUM(s.nb_deaths),0) AS ratio
            FROM Joueurs j
            JOIN Statistiques s ON j.id_joueur = s.id_joueur
            GROUP BY j.id_joueur
            HAVING COUNT(s.id_match) >= 5
            ORDER BY ratio DESC
            LIMIT 1;
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n=== R9 : MEILLEUR JOUEUR RATIO K/D (MIN 5 MATCHS) ===");
            if (rs.next()) {
                System.out.println("Joueur : " + rs.getString("pseudo") + " | Ratio K/D : " + rs.getDouble("ratio"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur R9 : " + e.getMessage());
        }
    }

    // R10
    public void executionR10() {
        String sql = """
            SELECT t.nom_tournoi, e.nom_equipe,
                   AVG(s.score) AS moyenne_score
            FROM Statistiques s
            JOIN `Match` m ON s.id_match = m.id_match
            JOIN Phase p ON m.id_phase = p.id_phase
            JOIN Tournoi t ON p.id_tournoi = t.id_tournoi
            JOIN Equipe e ON (e.id_equipe = m.id_equipe_1 OR e.id_equipe = m.id_equipe_2)
            GROUP BY t.id_tournoi, e.id_equipe;
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n=== R10 : MOYENNE DES PERFORMANCES ===");
            while (rs.next()) {
                System.out.println("Tournoi : " + rs.getString("nom_tournoi") + " | Équipe : " + rs.getString("nom_equipe") + " | Score Moyen : " + rs.getDouble("moyenne_score"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur R10 : " + e.getMessage());
        }
    }

    // R11
    public void executionR11() {
        String sql = """
            SELECT j.pseudo
            FROM Joueurs j
            WHERE NOT EXISTS (
                SELECT 1
                FROM Statistiques s
                JOIN `Match` m ON s.id_match = m.id_match
                JOIN Phase p ON m.id_phase = p.id_phase
                JOIN Tournoi t ON p.id_tournoi = t.id_tournoi
                WHERE t.type = 'LAN' AND s.id_joueur = j.id_joueur
            );
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n=== R11 : JOUEURS JAMAIS EN LAN ===");
            while (rs.next()) {
                System.out.println("Joueur : " + rs.getString("pseudo"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur R11 : " + e.getMessage());
        }
    }

    // R12
    public void executionR12() {
        String sql = """
            SELECT e.nom_equipe
            FROM Equipe e
            WHERE NOT EXISTS (
                SELECT 1
                FROM `Match` m
                JOIN Phase p ON m.id_phase = p.id_phase
                WHERE p.nom_phase = 'Phase de groupes'
                  AND (m.id_equipe_1 = e.id_equipe OR m.id_equipe_2 = e.id_equipe)
                  AND m.resultat <> 'Equipe 1'
            );
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n=== R12 : ÉQUIPES INVAINCUES EN PHASE DE GROUPE ===");
            while (rs.next()) {
                System.out.println("Équipe : " + rs.getString("nom_equipe"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur R12 : " + e.getMessage());
        }
    }

    // R13
    public void executionR13() {
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
            GROUP BY e.id_equipe
            ORDER BY victoires DESC;
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n=== R13 : CLASSEMENT COMPLET GLOBAL ===");
            while (rs.next()) {
                System.out.println("Équipe : " + rs.getString("nom_equipe") + " | Victoires : " + rs.getInt("victoires"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur R13 : " + e.getMessage());
        }
    }

    // R14
    public void executionR14() {
        String sql = """
            SELECT j.pseudo
            FROM Joueurs j
            JOIN Roster r ON j.id_joueur = r.id_joueur
            GROUP BY j.id_joueur
            HAVING COUNT(DISTINCT r.id_jeu) >= 2;
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n=== R14 : JOUEURS MULTI-JEUX ===");
            while (rs.next()) {
                System.out.println("Joueur : " + rs.getString("pseudo"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur R14 : " + e.getMessage());
        }
    }

    // R15
    public void executionR15() {
        String sql = """
            SELECT x.id_jeu, x.pseudo, x.moyenne_score
            FROM (
                SELECT r.id_jeu,
                       j.pseudo,
                       AVG(s.score) AS moyenne_score,
                       RANK() OVER (PARTITION BY r.id_jeu ORDER BY AVG(s.score) DESC) AS rnk
                FROM Statistiques s
                JOIN Joueurs j ON s.id_joueur = j.id_joueur
                JOIN Roster r ON r.id_joueur = j.id_joueur
                GROUP BY r.id_jeu, j.id_joueur
            ) x
            WHERE x.rnk = 1;
            """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n=== R15 : MEILLEUR JOUEUR PAR JEU ===");
            while (rs.next()) {
                System.out.println("Jeu ID : " + rs.getInt("id_jeu") + " | Joueur : " + rs.getString("pseudo") + " | Score Moyen : " + rs.getDouble("moyenne_score"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur R15 : " + e.getMessage());
        }
    }
}