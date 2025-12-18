import java.sql.*;
import java.util.ArrayList;
import java.sql.*;
import java.util.Scanner;


public class ActionBDDImpl {
    public static void main(String[] args) throws SQLException {
        Connection conn = connectToDatabase();
        ListeProgrammeurs(conn);
        affichageProgrammeurByID(conn);
        supprimerProgrammeur(conn);
    }


    // Connexion à la base de donnée
    public static Connection connectToDatabase() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Prog_BD",
                    "melek",
                    "password"
            );
            System.out.println("Connected to database successfully");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //        // Liste des programmeurs
    public static ArrayList<Programmeur> ListeProgrammeurs(Connection conn) {
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
            System.out.println("Liste des programmeurs:");
            System.out.println(liste);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    // Affichage des programmeurs
    public static void affichageProgrammeurByID(Connection conn) {
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
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Supprimer un programmeur
    public static void supprimerProgrammeur(Connection conn) {
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



//
//        // Affichage des programmeurs dont le nom est différent de Simpson
//        public static void ProgrammeursExceptSimpson(Connection conn) {
//            try {
//                Statement stmnt = conn.createStatement();
//                ResultSet rs = stmnt.executeQuery("SELECT * FROM programmeur WHERE NOM != 'Simpson'");
//
//                System.out.println("\nProgrammeurs sauf Simpson:");
//                while (rs.next()) {
//                    System.out.println(
//                            rs.getString(1) + " " +
//                                    rs.getString(2) + " " +
//                                    rs.getInt(3)
//                    );
//                }
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

    }


