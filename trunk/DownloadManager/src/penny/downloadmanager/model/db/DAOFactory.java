/*
 * DAOFactory.java
 * 
 * Created on Oct 20, 2007, 10:54:48 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.model.db;

/**
 *
 * @author John
 */
public abstract class DAOFactory {
    public static final int JAVADB = 0;
    public static final int POSTGRESQL = 1;
    public static final int MYSQL = 2;

    private static int db = 0;

    private static JavaDBDAOFactory javaFactory;
    
    public abstract DownloadDAO getDownloadDAO();
    
    public static DAOFactory getInstance() {
        switch(db) {
        case JAVADB:
            if(javaFactory == null) {
                javaFactory = new JavaDBDAOFactory();
                return javaFactory;
            } else {
                return javaFactory;
            }

        default:
            throw new IllegalArgumentException();
        }
    }

    public static void setDB(int db) {
        DAOFactory.db = db;
    }
}
