package nfc.cair.project.Database;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The class DbConnect  implements a database
 * connection and query implementation
 * Class dbConnect inherits variables for connecting
 * to the database from the DbConfig class
 * @author WhaleOpop
 * @version 1.0
 * @since 2022-06-13
 */

public class DbConnect extends DbConfig {


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
            String query="UPDATE `nfccairpepepeepe`.`nfcinfo` SET `FIO` = '"+values.get(1)+"', `NUMBERs` = '"+values.get(2)+"', `ABOUTME` = '"+values.get(3)+"', `COMPANY` = '"+values.get(4)+"', `PROFESION` = '"+values.get(5)+"', `LINKPHOTO` = '"+values.get(6)+"', `SITECOMPANY` = '"+values.get(7)+"', `INST` = '"+values.get(8)+"', `TELE` = '"+values.get(9)+"', `VKON` = '"+values.get(10)+"' WHERE (`ID` = '"+values.get(0)+"');";

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
            System.out.println(colCount);
            while (rs.next()) {
                for (int i = 1; i <= colCount; i++) {
                    resultTable.add(rs.getString(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(resultTable);
        return resultTable;
    }


    /**
     * The function for sql insert queries
     * @param query  This is where your sql select query comes in
     * @param values Arraylist variables in order per query
     */

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
        String query ="DELETE FROM `nfccairpepepeepe`.`nfcinfo` WHERE (`ID` = "+id.toString()+")";
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
