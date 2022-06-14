package nfc.cair.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DbConnect extends DbConfig {

    static Connection connection;


    public static Connection connector() {
        try {
            String url="jdbc:mysql://"+dbhost+":"+port+"/"+dbname;
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }


    public void InsertNfc(String FIO, String AboutMe, String Company, String Profesian, String LinkPhoto, String SiteCompany, String Inst, String Tele, String Vkon) {
        Integer id = 432879;
        String query = "INSERT INTO `nfcinfo`(`ID`, `FIO`, `ABOUTME`, `COMPANY`, `PROFESION`, `LINKPHOTO`, `SITECOMPANY`, `INST`, `TELE`, `VKON`) VALUES ('" + String.valueOf(id) + "','" + FIO + "','" + AboutMe + "','" + Company + "','" + Profesian + "','" + LinkPhoto + "','" + SiteCompany + "','" + Inst + "','" + Tele + "','" + Vkon + "')";

        try {

            Statement stmt = connector().createStatement();
            int i = stmt.executeUpdate(query);
            System.out.println("Rows inserted:" + i);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //init connect to db


}
