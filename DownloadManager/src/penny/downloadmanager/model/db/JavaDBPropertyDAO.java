/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class JavaDBPropertyDAO implements PropertyDAO {

    @Override
    public HashMap<String, Object> getProperties(UUID uuid) {
        HashMap<String, Object> props = new HashMap<String, Object>();
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "select * from property where " + Download.PROP_ID + " = '" + uuid + "'";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).fine(query);
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("name");
                Object prop = rs.getObject("property");
                props.put(name, prop);
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return props;
    }
    
    @Override
    public List<String> getPropertyNames() {
        List<String> props = new ArrayList<String>();
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "select distinct NAME from property";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).fine(query);
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("name");
                props.add(name);
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return props;
    }

    @Override
    public void saveProperty(UUID uuid, String name, Object property) {

        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "select name from property where " + Download.PROP_ID + " = '" + uuid + "' and name = '" + name + "'";
            Logger.getLogger(JavaDBPropertyDAO.class.getName()).log(Level.FINE, query);
            ResultSet rs = statement.executeQuery(query);
            int executeUpdate = 0;
            if (rs.next()) {
                statement.close();
                PreparedStatement insert = connection.prepareStatement("update property set property = ? where " + Download.PROP_ID + " = ? and name = ?");
                insert.setObject(1, property);
                insert.setString(2, uuid.toString());
                insert.setString(3, name);
                executeUpdate = insert.executeUpdate();
                Logger.getLogger(JavaDBPropertyDAO.class.getName()).log(Level.FINE, "returned {0} on update property set property = {1} where " + Download.PROP_ID + " = {2} and name = {3}", new Object[]{executeUpdate, property, uuid, name});
                insert.close();
            } else {
                statement.close();
                PreparedStatement insert = connection.prepareStatement("insert into property\n(" + Download.PROP_ID + ", name, property)\nvalues\n(?, ?, ?)");
                insert.setString(1, uuid.toString());
                insert.setString(2, name);
                insert.setObject(3, property);
                executeUpdate = insert.executeUpdate();
                Logger.getLogger(JavaDBPropertyDAO.class.getName()).log(Level.FINE, "returned {0} on insert into property (" + Download.PROP_ID + ", name, property) values ({1}, {2}, {3})", new Object[]{executeUpdate, uuid, name, property});
                insert.close();
            }
            if (executeUpdate != 1) {
                throw new IllegalStateException("executeUpdate is " + executeUpdate + " there should be 1 update/insert");
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBPropertyDAO.class.getName()).log(Level.SEVERE, "There was an Exception saving property " + uuid + " " + name + " " + property, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void deleteProperties(UUID uuid) {
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            int executeUpdate = 0;
            Statement statement = connection.createStatement();
            String query = "delete from property where " + Download.PROP_ID + " = '" + uuid + "'";
            executeUpdate = statement.executeUpdate(query);
            Logger.getLogger(JavaDBPropertyDAO.class.getName()).log(Level.FINE, "returned {0} on {1}", new Object[]{executeUpdate, query});
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBPropertyDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void deleteProperty(UUID uuid, String name) {
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            int executeUpdate = 0;
            Statement statement = connection.createStatement();
            String query = "delete from property where " + Download.PROP_ID + " = '" + uuid + "' and name = '" + name + "'";
            executeUpdate = statement.executeUpdate(query);
            Logger.getLogger(JavaDBPropertyDAO.class.getName()).log(Level.FINE, "returned {0} on {1}", new Object[]{executeUpdate, query});
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBPropertyDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
    }

    @Override
    public long getPropertyCount() {
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        long count = 0;
        try {
            Statement statement = connection.createStatement();
            String query = "select count(*) from property";
            Logger.getLogger(JavaDBPropertyDAO.class.getName()).log(Level.FINE, query);
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                count = rs.getLong(1);
                statement.close();
                return count;
            } else {
                return count;
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBPropertyDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return count;
    }
    
}
