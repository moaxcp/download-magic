/*
 * JavaDBDAOFactory.java
 *
 * Created on Oct 20, 2007, 10:57:34 AM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.model.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public class JavaDBDAOFactory extends DAOFactory {
    @Override
    public DownloadDAO getDownloadDAO() {
        return new JavaDBDownloadDAO();
    }
}