/*
 * JavaDBDAOFactory.java
 *
 * Created on Oct 20, 2007, 10:57:34 AM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.downloadmanager.model.db;

/**
 *
 * @author John
 */
public class JavaDBDAOFactory extends DAOFactory {
    @Override
    public DownloadDAO getDownloadDAO() {
        return new JavaDBDownloadDAO();
    }

    @Override
    public WordDAO getWordDAO() {
        return new JavaDBWordDAO();
    }

    @Override
    public LinkDAO getLinkDAO() {
        return new JavaDBLinkDAO();
    }

    @Override
    public PropertyDAO getPropertyDAO() {
        return new JavaDBPropertyDAO();
    }
}