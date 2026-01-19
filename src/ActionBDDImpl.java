import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;


public class ActionBDDImpl implements ActionBDD {

    // Connection à la base de donnée
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


    //  Afficher tous les programmeurs

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


     // Afficher un programmeur selon son id
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
                    tentatives++;
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
                            tentatives++;
                        }
                    }

                } catch (SQLException e) {
                    tentatives++;
                    System.out.println("Erreur SQL : " + e.getMessage());
                    break;
                }
                if (!trouve && tentatives >= MAX_TENTATIVES) {
                    System.out.println("Nombre maximum de tentatives atteint. Retour au menu principal.");
                    tentatives++;
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
                    System.out.println("____________________________");
                    System.out.println("Suppression réussie !");
                    suppressionReussie = true;
                } else {
                    tentatives++;
                    throw new IllegalArgumentException("Suppression KO. Saisissez à nouveau l'id :");
                }

            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez saisir un nombre valide.");
                tentatives++;

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                tentatives++;


            } catch (SQLException e) {
                System.out.println("Erreur SQL : " + e.getMessage());
                tentatives++;
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
                    tentatives++;
                    throw new IllegalArgumentException("Le nom ne peut pas être vide.");
                }

                System.out.print("Prénom du programmeur: ");
                String prenom = scanner.nextLine().trim();
                if (prenom.isEmpty()) {
                    tentatives++;
                    throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
                }

                System.out.print("Adresse du programmeur: ");
                String adresse = scanner.nextLine().trim();
                if (adresse.isEmpty()) {
                    tentatives++;
                    throw new IllegalArgumentException("L'adresse ne peut pas être vide.");
                }

                System.out.print("Pseudo du programmeur: ");
                String pseudo = scanner.nextLine().trim();
                if (pseudo.isEmpty()) {
                    tentatives++;
                    throw new IllegalArgumentException("Le pseudo ne peut pas être vide.");
                }

                System.out.print("Responsable du programmeur: ");
                String responsable = scanner.nextLine().trim();
                if (responsable.isEmpty()) {
                    tentatives++;
                    throw new IllegalArgumentException("Le responsable ne peut pas être vide.");
                }

                System.out.print("Hobby du programmeur: ");
                String hobby = scanner.nextLine().trim();
                if (hobby.isEmpty()) {
                    tentatives++;
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
                    tentatives++;
                    throw new SQLException("Aucune ligne insérée.");
                }
                pstmt.close();


            } catch (NumberFormatException e) {
                tentatives++;
                System.out.println("Erreur : Veuillez saisir des valeurs numériques valides.");

            } catch (IllegalArgumentException e) {
                tentatives++;
                System.out.println("Erreur : " + e.getMessage());

            } catch (SQLException e) {
                tentatives++;
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
                    tentatives++;
                    throw new IllegalArgumentException("Aucun programmeur trouvé avec cet ID.");
                }

                double ancienSalaire = rsSelect.getDouble("salaire");

                System.out.print("Nouveau salaire : ");
                double nouveauSalaire = Double.parseDouble(scanner.nextLine().trim());

                if (nouveauSalaire < 0) {
                    tentatives++;
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
                    tentatives++;
                    throw new SQLException("Aucune ligne modifiée.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Erreur : Veuillez saisir des valeurs numériques valides.");
                tentatives++;

            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
                tentatives++;

            } catch (SQLException e) {
                tentatives++;
                System.out.println("Erreur SQL : " + e.getMessage());
                break;

            }
        }

        if (!modificationReussie && tentatives >= MAX_TENTATIVES) {
            System.out.println("Nombre maximum de tentatives atteint. Retour au menu principal.");
        }
    }


    /** Afficher la liste des projets */
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

    /** Ajouter un projet */
    @Override
    public void AjoutProjet(Connection conn){
        Scanner scanner = new Scanner(System.in);

        try{
            System.out.print("**** Ajout d'un projet **** : \n");
            System.out.print("Intitulé du projet : ");
            String intitule = scanner.nextLine();

            System.out.print("Date de début du projet : ");
            Date date_debut = Date.valueOf(scanner.nextLine());

            System.out.print("Date de fin du projet : ");
            Date date_fin_prevue = Date.valueOf(scanner.nextLine());

            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO projet " +
                            "(intitule, date_debut, date_fin_prevue, etat) " +
                            "VALUES (?, ?, ?, 'Non débuté')"
            );

            pstmt.setString(1, intitule);
            pstmt.setDate(2, date_debut);
            pstmt.setDate(3, date_fin_prevue);

            int rs = pstmt.executeUpdate();

            if (rs > 0) {
                System.out.println("____________________________");
                System.out.println("Ajout du projet réussi !");
            }
            pstmt.close();

        } catch (IllegalArgumentException e) {

            System.out.println("Erreur : Le format de la date est incorrect. Utilisez le format YYYY-MM-DD.");
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }

    /** Assigner un projet à un programmeur */
    @Override
    public void assignerProjet(Connection conn) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Affichage de la liste des projets
            pstmt = conn.prepareStatement("SELECT id, intitule FROM projet");
            rs = pstmt.executeQuery();

            System.out.println("Choisir un projet : ");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " : " + rs.getString("intitule"));
            }
            rs.close();
            pstmt.close();

            // Saisie et validation du projet
            Scanner scanner = new Scanner(System.in);
            System.out.print("Id du projet : ");
            int projet = Integer.parseInt(scanner.nextLine());

            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM projet WHERE id = ?");
            pstmt.setInt(1, projet);
            rs = pstmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                throw new IllegalArgumentException("Erreur : l'ID du projet n'existe pas");
            }
            rs.close();
            pstmt.close();

            // Saisie et validation du programmeur
            System.out.print("Id du programmeur : ");
            int programmeur = Integer.parseInt(scanner.nextLine());

            pstmt = conn.prepareStatement(
                    "SELECT id, nom, prenom FROM programmeur WHERE id = ?");
            pstmt.setInt(1, programmeur);
            rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new IllegalArgumentException("Erreur : l'ID du programmeur n'existe pas");
            }

            // Récupération des données du programmeur pour l'affichage
            int progId = rs.getInt("id");
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            rs.close();
            pstmt.close();

            // Insertion de l'assignation
            pstmt = conn.prepareStatement(
                    "INSERT INTO programmeur_projet(projet_id, programmeur_id) VALUES (?, ?)");
            pstmt.setInt(1, projet);
            pstmt.setInt(2, programmeur);
            int nbLignes = pstmt.executeUpdate();

            if (nbLignes > 0) {
                System.out.println("Programmeur assigné !");
                System.out.println("Projet n° " + projet + " :");
                System.out.println("Id du programmeur : " + progId);
                System.out.println("Nom du programmeur : " + nom);
                System.out.println("Prénom du programmeur :" + prenom);
            } else {
                throw new IllegalArgumentException("Insertion KO.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /** Liste des programmeurs qui travaillent sur le même projet */
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
                System.out.println("____________________________");
                System.out.println(p.toString());

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return;
    }



    /** Questions sur les statistiques*/
    @Override
    public void stats(Connection conn){
        MenuStats menuStats = new MenuStats();
        menuStats.sousMenu(conn);
    }

    @Override
    public void salaireMoyen(Connection conn) {
        try {
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT AVG(salaire) FROM PROGRAMMEUR");
            if (rs.next()) {
                System.out.println("Salaire moyen des programmeurs : " + rs.getDouble(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void maxPrime(Connection conn) {
        try {
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * FROM PROGRAMMEUR WHERE prime =  (SELECT MAX(prime) FROM PROGRAMMEUR)");
            if (rs.next()) {
                System.out.println("Informations du programmeur ayant la prime la plus élevée : ");
                System.out.println("Id : " + rs.getInt("id"));
                System.out.println("Nom : " + rs.getString("nom"));
                System.out.println("Prénom : " + rs.getString("prenom"));
                System.out.println("Adresse : " + rs.getString("adresse"));
                System.out.println("Pseudo : " + rs.getString("pseudo"));
                System.out.println("Responsable : " + rs.getString("responsable"));
                System.out.println("Hobby : " + rs.getString("hobby"));
                System.out.println("Annaissance : " + rs.getInt("annaissance"));
                System.out.println("Salaire : " + rs.getDouble("salaire"));
                System.out.println("Prime : " + rs.getDouble("prime"));

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dureeMoyenneProjet(Connection conn) {
        try {
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT AVG(DATEDIFF(date_fin_prevue, date_debut)) AS duree_moyenne\n" +
                    "FROM projet;");
            if (rs.next()) {
                System.out.println("La durée moyenne des projets est de : " + rs.getDouble(1) + " jours.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void plusJeuneProgrammeur(Connection conn) {
        try {
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery(
                    "SELECT * FROM PROGRAMMEUR WHERE annaissance = (SELECT MAX(annaissance) FROM PROGRAMMEUR)"
            );


            System.out.println("Programmeur la/le plus jeune : ");
            while (rs.next()) {
                System.out.println("- Année de naissance : " + rs.getInt("annaissance") + " | " + rs.getString("nom") + " " + rs.getString("prenom"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}