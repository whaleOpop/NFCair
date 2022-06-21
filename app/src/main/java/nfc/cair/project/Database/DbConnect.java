package nfc.cair.project;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DbConnect extends DbConfig {


    String url = "jdbc:mysql://"+dbhost+":"+port+"/"+dbname+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";

    public void run() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("DB","Не ты хороший1");
        }catch( ClassNotFoundException e) {
            Log.e("DB","Пошел нахуй1");
            return;
        }

        // 連接資料庫
        try {
            Connection con = DriverManager.getConnection(url,user,password);
            Log.v("DB","Не ты хороший2");
        }catch(SQLException e) {
            Log.e("DB","Пошел нахуй2");
            Log.e("DB", e.toString());
        }
    }
    public String InsertData(String FIO, String AboutMe, String Company, String Profesian, String LinkPhoto, String SiteCompany, String Inst, String Tele, String Vkon) {
        Integer id=7429874;
        String data = "";
        try {

            Connection con = DriverManager.getConnection(url, user, password);
            String sql = "INSERT INTO `nfcinfo`(`ID`, `FIO`, `ABOUTME`, `COMPANY`, `PROFESION`, `LINKPHOTO`, `SITECOMPANY`, `INST`, `TELE`, `VKON`) VALUES ('" + String.valueOf(id) + "','" + FIO + "','" + AboutMe + "','" + Company + "','" + Profesian + "','" + LinkPhoto + "','" + SiteCompany + "','" + Inst + "','" + Tele + "','" + Vkon + "')";


            Statement stmt = con.createStatement();
            int i = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

}
