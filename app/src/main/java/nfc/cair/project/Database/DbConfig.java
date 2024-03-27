package nfc.cair.project.Database;
/**
 * This class contains configs for the database that are protected
 *  from intruders and used in the Dbconnect class
 * @author WhaleOpop
 * @version 1.0
 * @since 2022-06-13
 */
public class DbConfig {
    protected static String dbname="sql11664074";
    protected static String dbhost="sql11.freemysqlhosting.net";
    protected static String port= "3306";
    protected static String user = "sql11664074";
    protected static String password = "yiBVYSxrnr";
    protected static String url = "jdbc:mysql://" + dbhost + ":" + port + "/" + dbname + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";


}
