/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model.db;

import penny.downloadmanager.model.DownloadData;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class JavaDBDataSource {

    private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private String url = "jdbc:derby:downloads";
    private String user = "downloaduser";
    private String password = "downloadpassword";
    private String dbName = "downloads";
    private String strCreateDownloadTable =
            "create table DOWNLOAD (\n"
            + "    " + DownloadData.PROP_URL + "                VARCHAR(32672) NOT NULL PRIMARY KEY, \n"
            + "    " + DownloadData.PROP_SIZE + "               BIGINT, \n"
            + "    " + DownloadData.PROP_DOWNLOADED + "         BIGINT, \n"
            + "    " + DownloadData.PROP_STATUS + "             DownloadStatus, \n"
            + "    " + DownloadData.PROP_DOWNLOADTIME + "       BIGINT, \n"
            + "    " + DownloadData.PROP_SAVEPATH + "       VARCHAR(32672), \n"
            + "    " + DownloadData.PROP_TEMPPATH + "        VARCHAR(32672), \n"
            + "    " + DownloadData.PROP_ATTEMPTS + "        SMALLINT, \n"
            + "    " + DownloadData.PROP_CONTENTTYPE + "        VARCHAR(32672), \n"
            + "    " + DownloadData.PROP_HOST + "               VARCHAR(32672), \n"
            + "    " + DownloadData.PROP_PROTOCOL + "           VARCHAR(32672), \n"
            + "    " + DownloadData.PROP_QUERY + "              VARCHAR(32672), \n"
            + "    " + DownloadData.PROP_PATH + "               VARCHAR(32672), \n"
            + "    " + DownloadData.PROP_FILE + "               VARCHAR(32672), \n"
            + "    " + DownloadData.PROP_PROTOCOLFILENAME + "      VARCHAR(32672), \n"
            + "    " + DownloadData.PROP_FILEEXTENTION + "      VARCHAR(32672), \n"
            + "    " + DownloadData.PROP_MESSAGE + "      VARCHAR(32672), \n"
            + "    " + DownloadData.PROP_RETRYTIME + "        BIGINT, \n"
            + "    " + DownloadData.PROP_RESPONSECODE + "        SMALLINT, \n"
            + "    " + DownloadData.PROP_LOCATIONS + "        List, \n"
            + "    " + DownloadData.PROP_EXTRAPROPS + "        Map, \n"
            + "    " + DownloadData.PROP_MD5 + "        MD5State, \n"
            + "    " + DownloadData.PROP_LINKSTATE + "        LinkState, \n"
            + "    " + DownloadData.PROP_WORDBUFFER + "        VARCHAR(32672) \n"
            + ")";
    private String strCreateUrlTable =
            "create table URL (\n"
            + "    URL                VARCHAR(32672) NOT NULL,\n"
            + "    LINK                VARCHAR(32672) NOT NULL,\n"
            + "    TYPE                VARCHAR(4),\n"
            + "    COUNT                INTEGER,\n"
            + "    PRIMARY KEY        (URL, LINK, TYPE),\n"
            + "    FOREIGN KEY        (URL) REFERENCES DOWNLOAD (" + DownloadData.PROP_URL + ")\n"
            + ")";
    private String strCreateWordTable =
            "create table WORD (\n"
            + "    URL                VARCHAR(32672) NOT NULL,\n"
            + "    WORD                VARCHAR(32672) NOT NULL,\n"
            + "    WORDINDEX                BIGINT,\n"
            + "    PRIMARY KEY        (URL, WORD, WORDINDEX),\n"
            + "    FOREIGN KEY        (URL) REFERENCES DOWNLOAD (" + DownloadData.PROP_URL + ")\n"
            + ")";
    private String strCreatePropertyTable =
            "create table PROPERTY (\n"
            + "    URL                VARCHAR(32672) NOT NULL,\n"
            + "    NAME               VARCHAR(32672) NOT NULL,\n"
            + "    PROPERTY           Object,\n"
            + "    PRIMARY KEY        (URL, NAME),\n"
            + "    FOREIGN KEY        (URL) REFERENCES DOWNLOAD (" + DownloadData.PROP_URL + ")\n"
            + ")";
    private String strCreateMD5StateType =
            "create type MD5STATE\n " +
            "EXTERNAL NAME 'penny.recmd5.MD5State'\n " +
            "LANGUAGE JAVA";
    private String strCreateLinkStateType =
            "create type LinkState\n " +
            "EXTERNAL NAME 'penny.parser.LinkState'\n " +
            "LANGUAGE JAVA";
    private String strCreateObjectType =
            "create type OBJECT\n " +
            "EXTERNAL NAME 'java.lang.Object'\n " +
            "LANGUAGE JAVA";
    private String strCreateDownloadStatusType =
            "create type DownloadStatus\n " +
            "EXTERNAL NAME 'penny.download.DownloadStatus'\n " +
            "LANGUAGE JAVA";
    private String strCreateListType =
            "create type List\n " +
            "EXTERNAL NAME 'java.util.List'\n " +
            "LANGUAGE JAVA";
    private String strCreateMapType =
            "create type Map\n " +
            "EXTERNAL NAME 'java.util.Map'\n " +
            "LANGUAGE JAVA";
    private static JavaDBDataSource dataSource;
    private Queue<Connection> conPool;
    private Queue<Connection> conOut;

    public JavaDBDataSource() {
        conPool = new LinkedList<Connection>();
        conOut = new LinkedList<Connection>();
    }

    public static JavaDBDataSource getInstance() {
        if (dataSource == null) {
            dataSource = new JavaDBDataSource();
        }
        return dataSource;
    }

    Connection getConnection() {
        Connection con = conPool.poll();
        try {
            if (con != null && !con.isValid(10)) {
                con = null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (con == null) {
            con = createConnection();
        }
        conOut.add(con);
        return con;
    }

    void returnConnection(Connection connection) {
        boolean add = conOut.remove(connection);
        if (!add) {
            throw new IllegalArgumentException("attempted to return illegal connection");
        }
        conPool.add(connection);
    }

    public void initDB() throws SQLException, NullPointerException {
        setDBSystemDir();
        Properties dbProperties = loadProperties();
        if (!dbExists()) {
            createDatabase(dbProperties);
        }
        createConnection().close();
    }

    public void shutdownDB() {
        if (dbExists()) {
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException ex) {
                if (ex.getErrorCode() == 50000 && "XJ015".equals(ex.getSQLState())) {
                    Logger.getLogger(JavaDBDataSource.class.getName()).log(Level.INFO, "Derby shut down normally", ex);
                } else {
                    Logger.getLogger(JavaDBDataSource.class.getName()).log(Level.SEVERE, "Derby did not shut down normally", ex);
                }
            }

        }
    }

    public void deleteDB() {
        shutdownDB();
        File file = new File(getDatabaseLocation());
        deleteDir(file);
    }
    
    private static boolean deleteDir(File dir) {
    if (dir.isDirectory()) {
        String[] children = dir.list();
        for (int i=0; i<children.length; i++) {
            boolean success = deleteDir(new File(dir, children[i]));
            if (!success) {
                return false;
            }
        }
    }

    // The directory is now empty so delete it
    return dir.delete();
}

    private Properties loadProperties() {
        Properties prop = new Properties();
        prop.setProperty("user", user);
        prop.setProperty("password", password);
        prop.setProperty("derby.driver", driver);
        prop.setProperty("derby.url", url);
        prop.setProperty("derby.stream.error.logSeverityLevel", "0");
        return prop;
    }

    private Connection createConnection() {

        Properties dbProperties = loadProperties();

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, dbProperties);
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDAOFactory.class.getName()).log(Level.SEVERE, null, ex);
            connection = null;
        }

        return connection;
    }

    private void createDatabase(Properties dbProperties) {
        Connection dbConnection = null;
        dbProperties.put("create", "true");

        try {
            dbConnection = DriverManager.getConnection(url, dbProperties);
            createObjects(dbConnection);
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDAOFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        dbProperties.remove("create");
    }

    private void createObjects(Connection dbConnection) {
        Statement statement = null;
        try {
            statement = dbConnection.createStatement();
            statement.execute(strCreateMD5StateType);
            statement.execute(strCreateLinkStateType);
            statement.execute(strCreateObjectType);
            statement.execute(strCreateDownloadStatusType);
            statement.execute(strCreateListType);
            statement.execute(strCreateMapType);
            statement.execute(strCreateDownloadTable);
            statement.execute(strCreateUrlTable);
            statement.execute(strCreateWordTable);
            statement.execute(strCreatePropertyTable);
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDAOFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean dbExists() {
        boolean bExists = false;
        String dbLocation = getDatabaseLocation();
        File dbFileDir = new File(dbLocation);
        if (dbFileDir.exists()) {
            bExists = true;
        }
        return bExists;
    }

    private String getDatabaseLocation() {
        String dbLocation = System.getProperty("derby.system.home") + "/" + dbName;
        return dbLocation;
    }

    private void setDBSystemDir() {
        String systemDir = "./data";
        System.setProperty("derby.system.home", systemDir);

        File fileSystemDir = new File(systemDir);
        fileSystemDir.mkdir();
    }
}
