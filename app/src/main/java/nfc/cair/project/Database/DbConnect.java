package nfc.cair.project.Database;

import android.content.SharedPreferences;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Diseases
 * The class DbConnect  implements a database
 * connection and query implementation
 * Class dbConnect inherits variables for connecting
 * to the database from the DbConfig class
 * @author WhaleOpop
 * @version 1.0
 * @since 2022-06-13
 */

public class DbConnect extends DbConfig {

    SharedPreferences sharedPref;
    /**
     * This function checks for a connection to the JBDC driver and database.
     */

    public void run() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("DB", "Driver connect");
        } catch (ClassNotFoundException e) {
            Log.e("DB", "Driver non connect");
            return;
        }

        // 連接資料庫
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Log.v("DB", "Database connection");
        } catch (SQLException e) {
            Log.e("DB", "No database connection");
            Log.e("DB", e.toString());
        }
    }


    /**
     * This function is needed to serialize Arraylist with data
     * for sql query for insert
     *
     * @param values Arraylist variables in order per query
     * @return Returns a string complementing the insert query
     * Example return: "VALUES ('id','name','fio')"
     */

    private String ValuesSerilizedInsert(ArrayList<String> values) {
        String strValuer = " VALUES ('";

        for (String id : values) {
            strValuer += id + "','";
            System.out.println(strValuer);
        }
        strValuer = strValuer.substring(0, strValuer.length() - 2);
        strValuer += ")";
        return strValuer;
    }




    public void UpdateQuery(ArrayList<String> values){
        try {
            String value = sharedPref.getString("id", "400");
            String query="UPDATE `sql11664074`.`Diseases` SET `Medicine` = '"+values.get(1)+"', `Name_of_the_disease` = '"+values.get(2)+"', `Symptoms` = '"+values.get(3)+"', `First_aid_for_symptoms` = '"+values.get(4)+"', `Number_Doctor` = '"+values.get(5)+"', `FIO_Doctor` = '"+values.get(6)+", 'Patient' = "+values.get(7)+"' WHERE (`Risk` = '"+values.get(0)+"');";
            Statement stmt = null;
            Connection con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            System.out.println(query);
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * The function for select queries searches for one row
     * This method always returns Arraylist
     *
     * @param query This is where your sql select query comes in
     * @return Returns an arraylist with the value of the tables in order
     */
    public ArrayList<String> SelectQuery(String query) {
        ArrayList<String> resultTable = new ArrayList<String>();
        try {
            Statement stmt = null;
            Connection con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int colCount = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= colCount; i++) {
                    resultTable.add(rs.getString(i));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultTable;
    }


    /**
     * The function for sql insert queries
     * @param query  This is where your sql select query comes in
     * @param values Arraylist variables in order per query
     */

    public void InsertQuery(String query) {


        try {
            Statement stmt = null;
            Connection con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void InsertQuery(String query, ArrayList<String> values) {


        try {
            Statement stmt = null;
            Connection con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            query += ValuesSerilizedInsert(values);
            System.out.println(query);
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void DeleteQuery(Integer id){
        String query ="DELETE FROM `sql11664074`.`Diseases` WHERE (`Risk` = "+id.toString()+")";
        try {
            Statement stmt = null;
            Connection con = DriverManager.getConnection(url, user, password);
            stmt = con.prepareStatement(query);
            stmt.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
