/*
 * JavaDBDownloadDOA.java
 *
 * Created on Oct 19, 2007, 9:13:00 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model.db;

import penny.download.DownloadStatus;
import penny.recmd5.MD5State;
import penny.downloadmanager.model.DownloadData;
import java.net.MalformedURLException;
import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import penny.parser.LinkState;

/**
 *
 * @author John
 */
public class JavaDBDownloadDAO implements DownloadDAO {

    public JavaDBDownloadDAO() {
    }

    private Map<String, Integer> countUrls(List<String> urls) {
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

    private HashMap<String, Object> getProperties(String url) {
        HashMap<String, Object> props = new HashMap<String, Object>();
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "select * from property where url = '" + url + "'";
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
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return props;
    }

    private List<String> getLinks(String url, String type) {
        List<String> urls = new ArrayList<String>();
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "select * from url where url = '" + url + "' and TYPE = '" + type + "'";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).fine(query);
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                int count = rs.getInt("count");
                for (int i = 0; i < count; i++) {
                    if (type.equals(DownloadData.HREF)) {
                        urls.add(rs.getString("link"));
                    } else if (type.equals(DownloadData.SRC)) {
                        urls.add(rs.getString("link"));
                    }
                }
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return urls;
    }

    private List<String> getWords(String url) {
        List<String> words = new ArrayList<String>();
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "select * from word where url = '" + url + "' order by wordindex";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).fine(query);
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                words.add(rs.getString("WORD"));
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return words;
    }

    @Override
    public List<DownloadData> getDownloads() {
        List<DownloadData> downloads = new ArrayList<DownloadData>();
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "select url from download";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).fine(query);
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                DownloadData d = getDownload(rs.getString("url"));
                if(d != null) {
                    downloads.add(d);
                } else {
                    Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, "Could not create a DownloadData with {0} but it is in the db. It may not be a url.", rs.getString("url"));
                }
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return downloads;
    }

    public DownloadData getDownload(String url) {
        DownloadData d = null;
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            d = new DownloadData(new URL(url));
            Statement statement = connection.createStatement();
            String query = "select * from download where url = '" + url + "'";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).fine(query);
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                d.setAttempts(rs.getInt(DownloadData.PROP_ATTEMPTS));
                d.setContentType(rs.getString(DownloadData.PROP_CONTENTTYPE));
                d.setDownloadTime(rs.getLong(DownloadData.PROP_DOWNLOADTIME));
                d.setDownloaded(rs.getLong(DownloadData.PROP_DOWNLOADED));
                d.setMessage(rs.getString(DownloadData.PROP_MESSAGE));
                d.setResponseCode(rs.getInt(DownloadData.PROP_RESPONSECODE));
                d.setSavePath(rs.getString(DownloadData.PROP_SAVEPATH));
                d.setSize(rs.getLong(DownloadData.PROP_SIZE));
                d.setTempPath(rs.getString(DownloadData.PROP_TEMPPATH));
                d.setMD5((MD5State) rs.getObject(DownloadData.PROP_MD5));
                d.setLinkState((LinkState) rs.getObject(DownloadData.PROP_LINKSTATE));
                d.setWordBuffer(rs.getString(DownloadData.PROP_WORDBUFFER));
                DownloadStatus s = (DownloadStatus) rs.getObject(DownloadData.PROP_STATUS);
                if (s == DownloadStatus.COMPLETE) {
                    d.complete();
                } else if (s == DownloadStatus.STOPPED) {
                    d.stop();
                } else if (s == DownloadStatus.ERROR) {
                    d.error();
                }
                d.setExtraProps(new HashMap<String, Object>((Map<String, Object>) getProperties(url)));
                d.getSrcLinks().addAll(getLinks(url, DownloadData.SRC));
                d.getHrefLinks().addAll(getLinks(url, DownloadData.HREF));
                d.getWords().addAll(getWords(url));
            } else {
                d = null;
            }
            statement.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return d;
    }

    @Override
    public boolean insertDownload(DownloadData download) {
        if(downloadExists(download.getUrl().toString())) {
            throw new IllegalArgumentException(download.getUrl().toString() + "  exists in db");
        }
        StringBuilder query = new StringBuilder();
        List<String> propertyNames = download.getPropertyNames();
        propertyNames.remove(DownloadData.PROP_HREFLINKS);
        propertyNames.remove(DownloadData.PROP_SRCLINKS);
        propertyNames.remove(DownloadData.PROP_WORDS);
        propertyNames.removeAll(download.getExtraProps().keySet());
        query.append("insert into DOWNLOAD\n(");
        for (String s : propertyNames) {
            if (propertyNames.indexOf(s) != 0) {
                query.append(", ");
            }
            if (download.getExtraProps(s) == null) {
                query.append(s);
            }
        }
        query.append(")\nvalues(?");
        for (int i = 1; i < propertyNames.size(); i++) {
            query.append(", ?");
        }
        query.append(")");
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query.toString());
            int param = 1;
            for (String s : propertyNames) {
                Object o = download.getProperty(s);
                if (o instanceof Long) {
                    statement.setLong(param, (Long) o);
                } else if (o instanceof Integer) {
                    statement.setInt(param, (Integer) o);
                } else if (o instanceof URL) {
                    statement.setString(param, ((URL) o).toString());
                } else if (o instanceof DownloadStatus) {
                    statement.setObject(param, (DownloadStatus) o);
                } else if (o instanceof String) {
                    statement.setString(param, (String) o);
                } else if (o instanceof List) {
                    statement.setObject(param, (List) o);
                } else if (o instanceof Map) {
                    statement.setObject(param, (Map) o);
                } else if (o instanceof MD5State) {
                    statement.setObject(param, (MD5State) o);
                } else if (o instanceof LinkState) {
                    statement.setObject(param, (LinkState) o);
                }
                param++;
            }
            
            int executeUpdate = statement.executeUpdate();
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "returned {0} on {1}", new Object[]{executeUpdate, query.toString()});
            statement.close();

            if (executeUpdate == 0) {
                return false;
            }

            Map<String, Integer> href = countUrls(download.getHrefLinks());
            for (String s : href.keySet()) {
                if(!saveLink(download.getUrl().toString(), s, DownloadData.HREF)) {
                    deleteDownload(download);
                    return false;
                }
            }

            href = null;

            Map<String, Integer> src = countUrls(download.getSrcLinks());
            for (String s : src.keySet()) {
                if(!saveLink(download.getUrl().toString(), s, DownloadData.HREF)) {
                    deleteDownload(download);
                    return false;
                }
            }

            for (String s : download.getWords()) {
                if(!saveWord(download.getUrl().toString(), s)) {
                    deleteDownload(download);
                    return false;
                }
            }
            
            for (String s : download.getExtraProps().keySet()) {
                if(!saveProperty(download.getUrl().toString(), s, download.getProperty(s))) {
                    deleteDownload(download);
                    return false;
                }
            }

            return executeUpdate > 0;

        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return false;
    }

    @Override
    public boolean deleteDownload(DownloadData download) {
        if(!downloadExists(download.getUrl().toString())) {
            throw new IllegalArgumentException(download.getUrl().toString() + " does not exist in db");
        }
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            int del = 0;
            int executeQuery = 0;

            Statement statement = connection.createStatement();

            String query = "delete from url where url = '" + download.getUrl() + "'";
            executeQuery = statement.executeUpdate(query);
            del += executeQuery;
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "returned {0} on {1}", new Object[]{executeQuery, query});

            query = "delete from word where url = '" + download.getUrl() + "'";
            executeQuery = statement.executeUpdate(query);
            del += executeQuery;
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "returned {0} on {1}", new Object[]{executeQuery, query});

            query = "delete from property where url = '" + download.getUrl() + "'";
            executeQuery = statement.executeUpdate(query);
            del += executeQuery;
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "returned {0} on {1}", new Object[]{executeQuery, query});

            query = "delete from download where url = '" + download.getUrl() + "'";
            executeQuery = statement.executeUpdate(query);
            del += executeQuery;
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "returned {0} on {1}", new Object[]{executeQuery, query});
            
            statement.close();
            return del > 0;
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return false;
    }

    @Override
    public boolean clearDownloads(DownloadStatus status) {
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            String deleteWord = "delete from word where url in (select url from download where downloadstatus = ?)";
            String deleteProperty = "delete from property where url in (select url from download where downloadstatus = ?)";
            String deleteUrl = "delete from url where url in (select url from download where downloadstatus = ?)";
            String deleteDownload = "delete from download where downloadstatus = ?";

            int executeUpdate = 0;
            int del = 0;

            PreparedStatement statement = connection.prepareStatement(deleteUrl);
            statement.setObject(1, status);
            executeUpdate = statement.executeUpdate();
            del += executeUpdate;
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "delete from url where url in (select url from download where downloadstatus = " + status + ")");
            statement.close();

            statement = connection.prepareStatement(deleteWord);
            statement.setObject(1, status);
            executeUpdate = statement.executeUpdate();
            del += executeUpdate;
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "delete from word where url in (select url from download where downloadstatus = " + status + ")");
            statement.close();

            statement = connection.prepareStatement(deleteProperty);
            statement.setObject(1, status);
            executeUpdate = statement.executeUpdate();
            del += executeUpdate;
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "delete from property where url in (select url from download where downloadstatus = " + status + ")");
            statement.close();

            statement = connection.prepareStatement(deleteDownload);
            statement.setObject(1, status);
            executeUpdate = statement.executeUpdate();
            del += executeUpdate;
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "delete from download where downloadstatus = " + status + "");
            statement.close();

            return del > 0;
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return false;
    }

    @Override
    public long clearDownloads() {
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "delete from url";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            int executeUpdate = statement.executeUpdate(query);
            query = "delete from word";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            executeUpdate += statement.executeUpdate(query);
            query = "delete from property";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            executeUpdate += statement.executeUpdate(query);
            query = "delete from download";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            executeUpdate += statement.executeUpdate(query);
            statement.close();
            return executeUpdate;
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return -1;
    }

    @Override
    public boolean updateDownload(DownloadData download, String property) {
        if(!downloadExists(download.getUrl().toString())) {
            throw new IllegalArgumentException(download.getUrl().toString() + " does not exist in db");
        }
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        int executeUpdate = 0;
        try {
            if (!download.getExtraProps().keySet().contains(property)) {
                PreparedStatement statement = connection.prepareStatement("update download set " + property + " = ? where url = '" + download.getUrl().toString() + "'");
                Object o = download.getProperty(property);
                if (o instanceof Long) {
                    statement.setLong(1, (Long) o);
                } else if (o instanceof Integer) {
                    statement.setInt(1, (Integer) o);
                } else if (o instanceof URL) {
                    statement.setString(1, ((URL) o).toString());
                } else if (o instanceof DownloadStatus) {
                    statement.setObject(1, (DownloadStatus) o);
                } else if (o instanceof String) {
                    statement.setString(1, (String) o);
                } else if (o instanceof List) {
                    statement.setObject(1, (List) o);
                } else if (o instanceof Map) {
                    statement.setObject(1, (Map) o);
                } else if (o instanceof MD5State) {
                    statement.setObject(1, (MD5State) o);
                } else if (o instanceof LinkState) {
                    statement.setObject(1, (LinkState) o);
                }
                executeUpdate = statement.executeUpdate();
                Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "returned {0} on update download set {1} = {2} where url = ''{3}''", new Object[]{executeUpdate, property, o, download.getUrl().toString()});
                statement.close();
            } else {
                saveProperty(download.getUrl().toString(), property, download.getProperty(property));
            }
            return executeUpdate > 0;
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return false;
    }

    @Override
    public boolean saveLink(String url, String link, String type) {
        if(!downloadExists(url)) {
            throw new IllegalArgumentException(url + " does not exist in db");
        }
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            int count = 0;
            Statement s = connection.createStatement();
            String query = "select count from url where url = '" + url + "' and link = '" + link + "' and type = '" + type + "'";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            ResultSet rs = s.executeQuery(query);
            if (rs.next()) {
                count = rs.getInt("count");
            }
            s.close();
            int executeUpdate = 0;
            if (count > 0) {
                PreparedStatement statement = connection.prepareStatement("update url\n set count = ?\n where url = ? and link = ? and type = ?");
                statement.setInt(1, count + 1);
                statement.setString(2, url);
                statement.setString(3, link);
                statement.setString(4, type);
                executeUpdate = statement.executeUpdate();
                Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "returned {0} on udate url set count = {1} where url = {2} and link = {3} and type = {4}", new Object[]{executeUpdate, count, url, link, type});
                statement.close();
            } else {
                PreparedStatement statement = connection.prepareStatement("insert into url\n(url, link, type, count)\nvalues\n(?, ?, ?, ?)");
                statement.setString(1, url);
                statement.setString(2, link);
                statement.setString(3, type);
                statement.setInt(4, count + 1);
                executeUpdate = statement.executeUpdate();
                Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "returned {0} insert into url (url, link, type, count) values ({1}, {2}, {3}, {4})", new Object[]{executeUpdate, url, link, type, count});
                statement.close();
            }
            return executeUpdate > 0;
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return false;
    }

    @Override
    public boolean saveWord(String url, String word) {
        if(!downloadExists(url)) {
            throw new IllegalArgumentException(url + " does not exist in db");
        }
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        int executeUpdate = 0;
        long wordIndex = 0;
        try {
            Statement statement = connection.createStatement();
            String query = "select max(wordindex) from word where url = '" + url + "'";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                wordIndex = rs.getLong(1) + 1;
            }
            PreparedStatement insert = connection.prepareStatement("insert into word\n(url, word, wordindex)\nvalues\n(?, ?, ?)");
            insert.setString(1, url);
            insert.setString(2, word);
            insert.setLong(3, wordIndex);
            executeUpdate = insert.executeUpdate();
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "returned {0} on insert into word (url, word, wordindex) values ({1}, {2}, {3})", new Object[]{executeUpdate, url, word, wordIndex});
            insert.close();
            return executeUpdate > 0;
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return false;
    }

    @Override
    public boolean saveProperty(String url, String name, Object property) {
        if(!downloadExists(url)) {
            throw new IllegalArgumentException(url + " does not exist in db");
        }
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        int executeUpdate = 0;
        try {
            Statement statement = connection.createStatement();
            String query = "select name from property where url = '" + url + "'";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                statement.close();
                PreparedStatement insert = connection.prepareStatement("update property set property = ? where url = ? and name = ?");
                insert.setObject(1, property);
                insert.setString(2, url);
                insert.setString(3, name);
                executeUpdate = insert.executeUpdate();
                Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "returned {0} on update property set property = {1} where url = {2} and name = {3}", new Object[]{executeUpdate, property, url, name});
                return executeUpdate > 0;
            }
            statement.close();
            PreparedStatement insert = connection.prepareStatement("insert into property\n(url, name, property)\nvalues\n(?, ?, ?)");
            insert.setString(1, url);
            insert.setString(2, name);
            insert.setObject(3, property);
            executeUpdate = insert.executeUpdate();
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "returned {0} on insert into property (url, name, property) values ({1}, {2}, {3})", new Object[]{executeUpdate, url, name, property});
            insert.close();
            return executeUpdate > 0;
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return false;
    }

    @Override
    public boolean updateDownload(DownloadData download) {
        //TODO implement update for all properties!
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private boolean downloadExists(String url) {
        boolean r = false;
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "select url from download where url = '" + url + "'";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
                r = true;
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return r;
    }

    @Override
    public long getDownloadCount() {
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        long count = 0;
        try {
            Statement statement = connection.createStatement();
            String query = "select count(*) from download";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                count = rs.getLong(1);
                statement.close();
                return count;
            } else {
                return count;
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return count;
    }

    @Override
    public long getUrlCount() {
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        long count = 0;
        try {
            Statement statement = connection.createStatement();
            String query = "select count(*) from url";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                count = rs.getLong(1);
                statement.close();
                return count;
            } else {
                return count;
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return count;
    }

    @Override
    public long getPropertyCount() {
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        long count = 0;
        try {
            Statement statement = connection.createStatement();
            String query = "select count(*) from property";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                count = rs.getLong(1);
                statement.close();
                return count;
            } else {
                return count;
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return count;
    }

    @Override
    public long getWordCount() {
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        long count = 0;
        try {
            Statement statement = connection.createStatement();
            String query = "select count(*) from word";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                count = rs.getLong(1);
                statement.close();
                return count;
            } else {
                return count;
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return count;
    }

    private static void printDownloads(List<DownloadData> downloads) {
        for (DownloadData d : downloads) {
            System.out.println(d);
            for (String s : d.getHrefLinks()) {
                System.out.println("    href: " + s);
            }
            for (String s : d.getSrcLinks()) {
                System.out.println("    src: " + s);
            }
            System.out.println();
        }
    }

    public static void main(String... args) throws MalformedURLException {
        JavaDBDataSource db = JavaDBDataSource.getInstance();
        try {
            db.initDB();
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        DownloadDAO dao = DAOFactory.getInstance().getDownloadDAO();
        DownloadData google = new DownloadData(new URL("http://www.google.com/"));
        google.addHrefLink("http://www.gmail.com/");
        google.addHrefLink("http://www.gmail.com/");
        google.addHrefLink("http://www.gmail.com/asdf");
        google.addSrcLink("http://www.igoogle.com/logo.jpg");
        dao.insertDownload(google);
        dao.insertDownload(new DownloadData(new URL("http://www.gmail.com/")));
        google.addHrefLink("http://www.hotmail.com/");
        dao.saveLink("http://www.gmail.com/", "http://www.yahoo.com/", "src");
        dao.saveLink("http://www.gmail.com/", "http://www.yahoo.com/", "src");
        dao.saveLink("http://www.gmail.com/", "http://www.yahoo.com/", "href");
        google.setDownloaded(80);
        dao.updateDownload(google, DownloadData.PROP_DOWNLOADED);
        printDownloads(dao.getDownloads());
        dao.deleteDownload(google);
        printDownloads(dao.getDownloads());
        dao.clearDownloads();
        printDownloads(dao.getDownloads());
        db.deleteDB();
    }
}
