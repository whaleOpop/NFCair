package nfc.cair.project.Database;
/**
 * This class contains configs for the database that are protected
 *  from intruders and used in the Dbconnect class
 * @author WhaleOpop
 * @version 1.0
 * @since 2022-06-13
 */
public class DbConfig {
    protected static String dbname="nfccairpepepeepe";
    protected static String dbhost="db4free.net";
    protected static String port= "3306";
    protected static String user = "cairmysqlfsgtdrt";
    protected static String password = "KiD-Kqf-pzD-chh";
    protected static String url = "jdbc:mysql://" + dbhost + ":" + port + "/" + dbname + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";


}
