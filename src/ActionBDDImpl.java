import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class ActionBDDImpl implements ActionBDD {

    // Connexion à la base de donnée
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
           // System.out.println("Connected to database successfully");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // Liste des programmeurs
    @Override
    public ArrayList<Programmeur> ListeProgrammeurs(Connection conn) {
        ArrayList<Programmeur> liste = new ArrayList<>();

        try {
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * FROM PROGRAMMEUR");

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
                liste.add(p);
            }
            System.out.println("**** Liste des programmeurs **** :");
            for (Programmeur p : liste) {
                System.out.println(p);
                System.out.println("____________________________");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    // Affichage des programmeurs
    @Override
    public void affichageProgrammeurByID(Connection conn) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("ID du programmeur à afficher : ");
            Integer sc = Integer.valueOf(scanner.nextLine());

            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT * FROM `programmeur` WHERE id = ? ");
            pstmt.setInt(1, sc);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
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
                System.out.println(p.toString());
                System.out.println("____________________________");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Supprimer un programmeur
    @Override
    public void supprimerProgrammeur(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        boolean suppressionReussie = false;

        while (!suppressionReussie) {
            try {
                System.out.print("ID du programmeur à supprimer : ");
                Integer id = Integer.valueOf(scanner.nextLine());

                PreparedStatement pstmt = conn.prepareStatement(
                        "DELETE FROM `programmeur` WHERE id = ?");
                pstmt.setInt(1, id);
                int rs = pstmt.executeUpdate();

                if (rs > 0) {
                    System.out.println("Suppression réussie !");
                    suppressionReussie = true;
                } else {
                    throw new IllegalArgumentException("Supression KO. Saisissez à nouveau l'id :");
                }

                pstmt.close();

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("\n");
            } catch (SQLException e) {
                System.out.println("Erreur SQL : " + e.getMessage());
                break;
            }
        }
    }

    // Ajouter un programmeur
    @Override
    public void ajouterProgrammeur(Connection conn) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("**** Ajout d'un programmeur **** : \n");
            System.out.print("Nom : ");
            String nom = scanner.nextLine();

            System.out.print("Prénom : ");
            String prenom = scanner.nextLine();

            System.out.print("Adresse : ");
            String adresse = scanner.nextLine();

            System.out.print("Pseudo : ");
            String pseudo = scanner.nextLine();

            System.out.print("Responsable : ");
            String responsable = scanner.nextLine();

            System.out.print("Hobby : ");
            String hobby = scanner.nextLine();

            System.out.print("Année de naissance : ");
            int annaissance = Integer.parseInt(scanner.nextLine());

            System.out.print("Salaire : ");
            double salaire = Double.parseDouble(scanner.nextLine());

            System.out.print("Prime : ");
            double prime = Double.parseDouble(scanner.nextLine());

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
                System.out.println("Ajout réussi !");
            }
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }

    // Modifier le salaire
    @Override
    public void modifierSalaire(Connection conn) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("**** Modifier le salaire ****");
            System.out.print("ID du programmeur à modifier : ");
            int id = Integer.parseInt(scanner.nextLine());

            PreparedStatement selectStmt = conn.prepareStatement(
                    "SELECT salaire FROM programmeur WHERE id = ?"
            );
            selectStmt.setInt(1, id);
            ResultSet rsSelect = selectStmt.executeQuery();

            if (!rsSelect.next()) {
                System.out.println("Aucun programmeur trouvé avec cet ID.");
                return;
            }

            double ancienSalaire = rsSelect.getDouble("salaire");

            System.out.print("Nouveau salaire : ");
            double nouveauSalaire = Double.parseDouble(scanner.nextLine());

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
            }

            rsSelect.close();
            selectStmt.close();
            updateStmt.close();

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Entrée invalide.");
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

    // Ajouter un projet
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
                            "(intitule, date_debut, date_fin_prevue) " +
                            "VALUES (?, ?, ?)"
            );

            pstmt.setString(1, intitule);
            pstmt.setDate(2, date_debut);
            pstmt.setDate(3, date_fin_prevue);

            int rs = pstmt.executeUpdate();

            if (rs > 0) {
                System.out.println("Ajout du projet réussi !");
            }
            pstmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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