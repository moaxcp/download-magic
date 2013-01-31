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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class JavaDBLinkDAO implements LinkDAO {

    static Map<String, Integer> countUrls(List<String> urls) {
        Map<String, Integer> counts = new HashMap<String, Integer>();
        for (String s : urls) {
            int c = 0;
            for (int i = 0; i < urls.size(); i++) {
                if (urls.get(i).equals(s)) {
                    c++;
                }
            }
            counts.put(s, c);
        }
        return counts;
    }

    @Override
    public void addLink(UUID uuid, String link, String type) {

        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            int count = 0;
            Statement s = connection.createStatement();
            String query = "select count from url where " + Download.PROP_ID + " = '" + uuid + "' and link = '" + link + "' and type = '" + type + "'";
            Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.FINE, query);
            ResultSet rs = s.executeQuery(query);
            if (rs.next()) {
                count = rs.getInt("count");
            }
            s.close();
            int executeUpdate = 0;
            if (count > 0) {
                PreparedStatement statement = connection.prepareStatement("update url\n set count = ?\n where " + Download.PROP_ID + " = ? and link = ? and type = ?");
                statement.setInt(1, count + 1);
                statement.setString(2, uuid.toString());
                statement.setString(3, link);
                statement.setString(4, type);
                executeUpdate = statement.executeUpdate();
                Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.FINE, "returned {0} on udate url set count = {1} where " + Download.PROP_ID + " = {2} and link = {3} and type = {4}", new Object[]{executeUpdate, count, uuid, link, type});
                statement.close();
            } else {
                PreparedStatement statement = connection.prepareStatement("insert into url\n(" + Download.PROP_ID + ", link, type, count)\nvalues\n(?, ?, ?, ?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, link);
                statement.setString(3, type);
                statement.setInt(4, count + 1);
                executeUpdate = statement.executeUpdate();
                Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.FINE, "returned {0} insert into url (" + Download.PROP_ID + ", link, type, count) values ({1}, {2}, {3}, {4})", new Object[]{executeUpdate, uuid, link, type, count});
                statement.close();
            }

            if (executeUpdate != 1) {
                throw new IllegalStateException("executeUpdate is " + executeUpdate + " there should be 1 update/insert");
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.SEVERE, "Exception saving link " + uuid + " " + link + " " + type, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void addLink(UUID uuid, String link, String type, int count) {

        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            int dbCount = 0;
            Statement s = connection.createStatement();
            String query = "select count from url where " + Download.PROP_ID + " = '" + uuid + "' and link = '" + link + "' and type = '" + type + "'";
            Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.FINE, query);
            ResultSet rs = s.executeQuery(query);
            if (rs.next()) {
                dbCount = rs.getInt("count");
            }
            s.close();
            int executeUpdate = 0;
            if (dbCount > 0) {
                PreparedStatement statement = connection.prepareStatement("update url\n set count = ?\n where " + Download.PROP_ID + " = ? and link = ? and type = ?");
                statement.setInt(1, count + dbCount);
                statement.setString(2, uuid.toString());
                statement.setString(3, link);
                statement.setString(4, type);
                executeUpdate = statement.executeUpdate();
                Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.FINE, "returned {0} on udate url set count = {1} where " + Download.PROP_ID + " = {2} and link = {3} and type = {4}", new Object[]{executeUpdate, dbCount, uuid, link, type});
                statement.close();
            } else {
                PreparedStatement statement = connection.prepareStatement("insert into url\n(" + Download.PROP_ID + ", link, type, count)\nvalues\n(?, ?, ?, ?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, link);
                statement.setString(3, type);
                statement.setInt(4, count);
                executeUpdate = statement.executeUpdate();
                Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.FINE, "returned {0} insert into url (" + Download.PROP_ID + ", link, type, count) values ({1}, {2}, {3}, {4})", new Object[]{executeUpdate, uuid, link, type, dbCount});
                statement.close();
            }

            if (executeUpdate != 1) {
                throw new IllegalStateException("executeUpdate is " + executeUpdate + " there should be 1 update/insert");
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.SEVERE, "Exception saving link " + uuid + " " + link + " " + type, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void addLinks(UUID uuid, List<String> list, String type) {
        Map<String, Integer> count = countUrls(list);
        for(String url : count.keySet()) {
            addLink(uuid, url, type, count.get(url));
        }
    }

    @Override
    public void deleteLinks(UUID uuid) {
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            int executeUpdate = 0;
            Statement statement = connection.createStatement();
            String query = "delete from url where " + Download.PROP_ID + " = '" + uuid + "'";
            executeUpdate = statement.executeUpdate(query);
            Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.FINE, "returned {0} on {1}", new Object[]{executeUpdate, query});
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void deleteLinks(UUID uuid, List<String> list, String type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getUrlCount() {
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        long count = 0;
        try {
            Statement statement = connection.createStatement();
            String query = "select count(*) from url";
            Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.FINE, query);
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                count = rs.getLong(1);
                statement.close();
                return count;
            } else {
                return count;
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return count;
    }
    
}
