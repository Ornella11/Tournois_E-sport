import java.sql.*;
import java.util.ArrayList;
import java.sql.*;


public class ActionBDDImpl {
    public static void main(String[] args) throws SQLException {
        Connection conn = connectToDatabase();
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
//        public static ArrayList<Programmeur> ListeProgrammeurs(Connection conn) {
//            ArrayList<Programmeur> liste = new ArrayList<>();
//
//            try {
//                Statement stmnt = conn.createStatement();
//                ResultSet rs = stmnt.executeQuery("SELECT * FROM PROGRAMMEUR");
//
//                while (rs.next()) {
//                    Programmeur p = new Programmeur(
//                            rs.getString(1),
//                            rs.getString(2),
//                            rs.getInt(3),
//                            rs.getDouble(4),
//                            rs.getDouble(5)
//                    );
//                    liste.add(p);
//
//                                   }
//                System.out.println("Liste des programmeurs:");
//                System.out.println(liste);
//
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            return liste;
//        }

//        // Affichage des programmeurs
//        public static void affichageProgrammeur(Connection conn) {
//            try {
//                System.out.println("Affichage des programmeurs:");
//                Statement stmnt = conn.createStatement();
//                ResultSet rs = stmnt.executeQuery("SELECT * FROM PROGRAMMEUR");
//
//                while (rs.next()) {
//                    Programmeur p = new Programmeur(
//                            rs.getString(1),
//                            rs.getString(2),
//                            rs.getInt(3),
//                            rs.getDouble(4),
//                            rs.getDouble(5)
//                    );
//                }
//
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
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

