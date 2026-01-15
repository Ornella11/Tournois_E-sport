import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;


public class ActionBDDImpl implements ActionBDD {

    /** Connection à la base de donnée */
    @Override
    public Connection connectToDatabase() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Prog_BD",
                    "groupe10",
                    "password"
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Afficher tous les programmeurs
     */
    @Override
    public String afficherProgrammeurs(Connection conn) {

        try {
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * FROM PROGRAMMEUR");

            System.out.println("**** Liste des programmeurs **** :");
            while (rs.next()) {
                Programmeur p = new Programmeur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("adresse"),
                        rs.getString("pseudo"),
                        rs.getString("responsable"),
                        rs.getString("hobby"),
                        rs.getInt("annaissance"),
                        rs.getDouble("salaire"),
                        rs.getDouble("prime")
                );
                System.out.print(p);
                System.out.print("----------------------");
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
        return null;
    }

    /**
     * Afficher un programmeur selon son id
     */
    @Override
    public void affichageProgrammeurByID(Connection conn) {
        try {
            Scanner scanner = new Scanner(System.in);
            boolean trouve = false;
            final int MAX_TENTATIVES = 3;
            int tentatives = 0;

            while (!trouve && tentatives < MAX_TENTATIVES) {
                System.out.print("ID du programmeur à afficher : ");

                String input = scanner.nextLine();
                int id;

                try {
                    id = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Erreur : veuillez saisir un entier valide !");
                    continue;
                }

                String sql = "SELECT * FROM `programmeur` WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, id);

                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {  // on lit la ligne
                            Programmeur p = new Programmeur(
                                    rs.getInt(1),
                                    rs.getString(2),
                                    rs.getString(3),
                                    rs.getString(4),
                                    rs.getString(5),
                                    rs.getString(6),
                                    rs.getString(7),
                                    rs.getInt(8),
                                    rs.getDouble(9),
                                    rs.getDouble(10)
                            );
                            System.out.println(p);
                            trouve = true;
                        } else {
                            System.out.println("Recherche KO : ID non trouvé. Réessayez.");
                        }
                    }

                } catch (SQLException e) {
                    System.out.println("Erreur SQL : " + e.getMessage());
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }
    }


    /**
     * Supprimer un programmeur
     */
    @Override
    public void supprimerProgrammeur(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        boolean suppressionReussie = false;
        final int MAX_TENTATIVES = 3;
        int tentatives = 0;

        while (!suppressionReussie && tentatives < MAX_TENTATIVES) {
            PreparedStatement pstmt = null;

            try {
                System.out.print("ID du programmeur à supprimer : ");
                Integer id = Integer.valueOf(scanner.nextLine().trim());

                pstmt = conn.prepareStatement("DELETE FROM `programmeur` WHERE id = ?");
                pstmt.setInt(1, id);
                int rs = pstmt.executeUpdate();

                if (rs > 0) {
                    System.out.println("Suppression réussie !");
                    suppressionReussie = true;
                } else {
                    throw new IllegalArgumentException("Suppression KO. Saisissez à nouveau l'id :");
                }

            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez saisir un nombre valide.");

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());

            } catch (SQLException e) {
                System.out.println("Erreur SQL : " + e.getMessage());
                break;

            }
        }
    }

    /**
     * Ajouter un programmeur
     */
    @Override
    public void ajouterProgrammeur(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        final int MAX_TENTATIVES = 3;
        int tentatives = 0;
        boolean ajoutReussie = false;
        while (!ajoutReussie && tentatives < MAX_TENTATIVES) {
            try {
                System.out.print("Nom du programmeur: ");
                String nom = scanner.nextLine().trim();
                if (nom.isEmpty()) {
                    throw new IllegalArgumentException("Le nom ne peut pas être vide.");
                }

                System.out.print("Prénom du programmeur: ");
                String prenom = scanner.nextLine().trim();
                if (prenom.isEmpty()) {
                    throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
                }

                System.out.print("Adresse du programmeur: ");
                String adresse = scanner.nextLine().trim();
                if (adresse.isEmpty()) {
                    throw new IllegalArgumentException("L'adresse ne peut pas être vide.");
                }

                System.out.print("Pseudo du programmeur: ");
                String pseudo = scanner.nextLine().trim();
                if (pseudo.isEmpty()) {
                    throw new IllegalArgumentException("Le pseudo ne peut pas être vide.");
                }

                System.out.print("Responsable du programmeur: ");
                String responsable = scanner.nextLine().trim();
                if (responsable.isEmpty()) {
                    throw new IllegalArgumentException("Le responsable ne peut pas être vide.");
                }

                System.out.print("Hobby du programmeur: ");
                String hobby = scanner.nextLine().trim();
                if (hobby.isEmpty()) {
                    throw new IllegalArgumentException("Le hobby ne peut pas être vide.");
                }

                System.out.print("Année de naissance du programmeur: ");
                int annaissance = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Salaire du programmeur: ");
                double salaire = Double.parseDouble(scanner.nextLine().trim());

                System.out.print("Prime du programmeur: ");
                double prime = Double.parseDouble(scanner.nextLine().trim());

                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO programmeur " +
                                "(nom, prenom, adresse, pseudo, responsable, hobby, annaissance, salaire, prime) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );

                pstmt.setString(1, nom);
                pstmt.setString(2, prenom);
                pstmt.setString(3, adresse);
                pstmt.setString(4, pseudo);
                pstmt.setString(5, responsable);
                pstmt.setString(6, hobby);
                pstmt.setInt(7, annaissance);
                pstmt.setDouble(8, salaire);
                pstmt.setDouble(9, prime);

                int rs = pstmt.executeUpdate();

                if (rs > 0) {
                    System.out.println("AJOUT REUSSI !");
                    ajoutReussie = true;
                } else {
                    throw new SQLException("Aucune ligne insérée.");
                }
                pstmt.close();


            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez saisir des valeurs numériques valides.");

            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());

            } catch (SQLException e) {
                System.out.println("Erreur SQL : " + e.getMessage());

            }
        }
    }

    /**
     * Modifier le salaire
     */
    @Override
    public void modifierSalaire(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        final int MAX_TENTATIVES = 3;
        int tentatives = 0;
        boolean modificationReussie = false;

        while (!modificationReussie && tentatives < MAX_TENTATIVES) {
            try {
                System.out.print("ID du programmeur à modifier : ");
                int id = Integer.parseInt(scanner.nextLine().trim());

                PreparedStatement selectStmt = conn.prepareStatement(
                        "SELECT salaire FROM programmeur WHERE id = ?"
                );
                selectStmt.setInt(1, id);
                ResultSet rsSelect = selectStmt.executeQuery();

                if (!rsSelect.next()) {
                    throw new IllegalArgumentException("Aucun programmeur trouvé avec cet ID.");
                }

                double ancienSalaire = rsSelect.getDouble("salaire");

                System.out.print("Nouveau salaire : ");
                double nouveauSalaire = Double.parseDouble(scanner.nextLine().trim());

                if (nouveauSalaire < 0) {
                    throw new IllegalArgumentException("Le salaire ne peut pas être négatif.");
                }

                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE programmeur SET salaire = ? WHERE id = ?"
                );
                updateStmt.setDouble(1, nouveauSalaire);
                updateStmt.setInt(2, id);

                int rows = updateStmt.executeUpdate();

                if (rows > 0) {
                    System.out.println("Salaire modifié avec succès !");
                    System.out.println("Ancien salaire : " + ancienSalaire);
                    System.out.println("Nouveau salaire : " + nouveauSalaire);
                    modificationReussie = true;
                } else {
                    throw new SQLException("Aucune ligne modifiée.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez saisir des valeurs numériques valides.");
                tentatives++;

            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
                tentatives++;

            } catch (SQLException e) {
                System.out.println("Erreur SQL : " + e.getMessage());
                break;

            }
        }

        if (!modificationReussie && tentatives >= MAX_TENTATIVES) {
            System.out.println("Nombre maximum de tentatives atteint. Retour au menu principal.");
        }
    }


    // Liste des projets
    @Override
    public void ListeProjet(Connection conn) {
        try {
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * FROM PROJET");

            System.out.println("Liste des projets:");
            while (rs.next()) {
                System.out.println(
                        "Projet n° : " + rs.getInt("id") +
                                "\nIntitulé : " + rs.getString("intitule") +
                                "\nDate de début : " + rs.getDate("date_debut") +
                                "\nDate de fin prévue : " + rs.getDate("date_fin_prevue") +
                                "\nÉtat : " + rs.getString("etat") + "\n"
                );
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return;
    }

    // Assigner un projet à un programmeur
    @Override
    public void assignerProjet(Connection conn) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Id du projet : ");
            int projet = Integer.parseInt(scanner.nextLine());

            Scanner sc = new Scanner(System.in);
            System.out.print("Id du programmeur : ");
            int programmeur = Integer.parseInt(scanner.nextLine());


            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO programmeur_projet(id, projet_id, programmeur_id) VALUES (id, ?, ?)");
            pstmt.setInt(1, projet);
            pstmt.setInt(2, programmeur);
            int rs = pstmt.executeUpdate();

            if (rs > 0) {
                System.out.println("Programmeur assigné !");
            } else {
                throw new IllegalArgumentException("Insertion KO.");
            }

            pstmt.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    // Liste des programmeurs qui travaillent sur le même projet
    @Override
    public void afficherProgrammeursByProjet(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Saisir l'intitulé du projet : ");
        String intitule = scanner.nextLine();

        String sql = "SELECT pr.id, pr.nom, pr.prenom, pr.adresse, pr.pseudo, pr.responsable, pr.hobby, pr.annaissance, pr.salaire, pr.prime FROM programmeur pr JOIN programmeur_projet pp ON pp.programmeur_id = pr.id JOIN projet p ON pp.projet_id = p.id WHERE p.intitule = ? ";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, intitule);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\nListe des programmeurs : ");
            while (rs.next()) {
                Programmeur p = new Programmeur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("adresse"),
                        rs.getString("pseudo"),
                        rs.getString("responsable"),
                        rs.getString("hobby"),
                        rs.getInt("annaissance"),
                        rs.getDouble("salaire"),
                        rs.getDouble("prime")
                );
                System.out.println(p.toString());

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return;
    }

}