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
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class JavaDBLinkDAO implements LinkDAO {

    @Override
    public List<String> getLinks(UUID uuid, String type) {
        List<String> urls = new ArrayList<String>();
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "select * from url where " + Download.PROP_ID + " = '" + uuid + "' and TYPE = '" + type + "' order by LINKINDEX";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).fine(query);
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (type.equals(Download.HREF)) {
                    urls.add(rs.getString("link"));
                } else if (type.equals(Download.SRC)) {
                    urls.add(rs.getString("link"));
                } else if (type.equals(Download.REDIRECT)) {
                    urls.add(rs.getString("link"));
                }
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return urls;
    }

    @Override
    public void addLink(UUID uuid, String link, String type) {

        Connection connection = JavaDBDataSource.getInstance().getConnection();
        int executeUpdate = 0;
        long linkIndex = 0;
        try {
            Statement statement = connection.createStatement();
            String query = "select max(linkindex) from url where " + Download.PROP_ID + " = '" + uuid + "'";
            Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.FINE, query);
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                linkIndex = rs.getLong(1) + 1;
            }
            statement.close();
            PreparedStatement insert = connection.prepareStatement("insert into url\n(" + Download.PROP_ID + ", link, type, linkindex)\nvalues\n(?, ?, ?, ?)");
            insert.setString(1, uuid.toString());
            insert.setString(2, link);
            insert.setString(3, type);
            insert.setLong(4, linkIndex);
            executeUpdate = insert.executeUpdate();
            Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.FINE, "returned {0} insert into url (" + Download.PROP_ID + ", link, type, count) values ({1}, {2}, {3}, {4})", new Object[]{executeUpdate, uuid, link, type, linkIndex});
            insert.close();

            if (executeUpdate != 1) {
                throw new IllegalStateException("executeUpdate is " + executeUpdate + " there should be 1 insert");
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
        for (String url : list) {
            addLink(uuid, url, type);
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
    public void deleteLinks(UUID uuid, List<String> list) {
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            for (String link : list) {
                int executeUpdate = 0;
                Statement statement = connection.createStatement();
                String query = "delete from url where " + Download.PROP_ID + " = '" + uuid + "' and link = '" + link + "'";
                executeUpdate = statement.executeUpdate(query);
                Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.FINE, "returned {0} on {1}", new Object[]{executeUpdate, query});
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBLinkDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
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
