/*
 * JavaDBDownloadDOA.java
 *
 * Created on Oct 19, 2007, 9:13:00 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model.db;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import penny.download.DownloadStatus;
import penny.download.Downloads;
import penny.parser.LinkState;
import penny.recmd5.MD5State;

/**
 *
 * @author John
 */
public class JavaDBDownloadDAO implements DownloadDAO {

    public JavaDBDownloadDAO() {
    }

    private void setObjectParam(PreparedStatement statement, int param, Object o) throws SQLException {
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
        } else if (o instanceof UUID) {
            statement.setString(param, ((UUID) o).toString());
        }
    }

    @Override
    public List<Download> getDownloads() {
        List<Download> downloads = new ArrayList<Download>();
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "select " + Download.PROP_ID + " from download";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).fine(query);
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                try {
                    Download d = getDownload(UUID.fromString(rs.getString("ID")));
                    if (d != null) {
                        downloads.add(d);
                    } else {
                        Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, "Could not create a Download with {0} but it is in the db.", rs.getString(Download.PROP_ID));
                    }
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, "UUID from db \"{0}\" is not valid.", rs.getString(Download.PROP_ID));
                    throw new IllegalStateException("There was an Illegal Argument Exception", ex);
                }
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("There was a SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return downloads;
    }

    @Override
    public Download getDownload(UUID uuid) {
        Download d = null;
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            d = new Download(uuid);
            Statement statement = connection.createStatement();
            String query = "select * from download where " + Download.PROP_ID + " = '" + uuid + "'";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).fine(query);
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                try {
                    d.setUrl(new URL(rs.getString(Download.PROP_URL)));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IllegalStateException("There is a bad url in the db " + rs.getString(Download.PROP_URL));
                }
                d.setAttempts(rs.getInt(Download.PROP_ATTEMPTS));
                d.setContentType(rs.getString(Download.PROP_CONTENTTYPE));
                d.setDownloadTime(rs.getLong(Download.PROP_DOWNLOADTIME));
                d.setDownloaded(rs.getLong(Download.PROP_DOWNLOADED));
                d.setMessage(rs.getString(Download.PROP_MESSAGE));
                d.setResponseCode(rs.getInt(Download.PROP_RESPONSECODE));
                d.setSize(rs.getLong(Download.PROP_SIZE));
                DownloadStatus s = (DownloadStatus) rs.getObject(Download.PROP_STATUS);
                if (s == DownloadStatus.COMPLETE) {
                    Downloads.setStatus(d, DownloadStatus.COMPLETE);
                } else if (s == DownloadStatus.STOPPED) {
                    Downloads.setStatus(d, DownloadStatus.STOPPED);
                } else if (s == DownloadStatus.ERROR) {
                    Downloads.setStatus(d, DownloadStatus.ERROR);
                }
                d.setMD5((MD5State) rs.getObject(Download.PROP_MD5));
                d.setLinkState((LinkState) rs.getObject(Download.PROP_LINKSTATE));
                d.setWordBuffer(rs.getString(Download.PROP_WORDBUFFER));
                d.setSavePath(rs.getString(Download.PROP_SAVEPATH));
                d.setTempPath(rs.getString(Download.PROP_TEMPPATH));
                d.addExtraProperties((Map<String, Object>) DAOFactory.getInstance().getPropertyDAO().getProperties(uuid));
                d.addSrcLinks(DAOFactory.getInstance().getLinkDAO().getLinks(uuid, Download.SRC));
                d.addHrefLinks(DAOFactory.getInstance().getLinkDAO().getLinks(uuid, Download.HREF));
                d.addWords(DAOFactory.getInstance().getWordDAO().getWords(uuid));
            } else {
                d = null;
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        return d;
    }

    @Override
    public void insertDownload(Download download) {
        if (downloadExists(download.getId())) {
            throw new IllegalArgumentException(download + "  exists in db");
        }
        StringBuilder query = new StringBuilder();
        List<String> propertyNames = JavaDBDataSource.saveProps;
        query.append("insert into DOWNLOAD\n(");
        query.append(propertyNames.get(0));
        for (int i = 1; i < propertyNames.size(); i++) {
            query.append(", ").append(propertyNames.get(i));
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
                setObjectParam(statement, param, o);
                param++;
            }

            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query.toString());
            int executeUpdate = statement.executeUpdate();
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "returned {0} on {1}", new Object[]{executeUpdate, query.toString()});
            statement.close();

            if (executeUpdate != 1) {
                throw new IllegalStateException("executeUpdate is " + executeUpdate + " there should be 1 insert");
            }

            DAOFactory.getInstance().getLinkDAO().addLinks(download.getId(), download.getHrefLinks(), Download.HREF);
            DAOFactory.getInstance().getLinkDAO().addLinks(download.getId(), download.getSrcLinks(), Download.SRC);
            DAOFactory.getInstance().getLinkDAO().addLinks(download.getId(), download.getLocations(), Download.REDIRECT);

            for (String s : download.getWords()) {
                DAOFactory.getInstance().getWordDAO().addWord(download.getId(), s);
            }

            for (String s : download.getExtraProperties().keySet()) {
                DAOFactory.getInstance().getPropertyDAO().saveProperty(download.getId(), s, download.getExtraProperties().get(s));
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void updateDownload(Download download, String property) {
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        int executeUpdate = 0;
        try {
            if (!download.getExtraProperties().keySet().contains(property)) {
                PreparedStatement statement = connection.prepareStatement("update download set " + property + " = ? where " + Download.PROP_ID + " = '" + download.getId() + "'");
                Object o = download.getProperty(property);
                setObjectParam(statement, 1, o);
                executeUpdate = statement.executeUpdate();
                Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "returned {0} on update download set {1} = {2} where " + Download.PROP_ID + " = ''{3}''", new Object[]{executeUpdate, property, o, download.getId()});
                statement.close();

                if (executeUpdate != 1) {
                    throw new IllegalStateException("executeUpdate is " + executeUpdate + " there should be 1 update");
                }

            } else {
                DAOFactory.getInstance().getPropertyDAO().saveProperty(download.getId(), property, download.getExtraProperties().get(property));
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, "property " + property + " value " + download.getProperty(property), ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
    }

    //TODO add change url for download
    @Override
    public void updateDownload(Download download) {
        StringBuilder query = new StringBuilder();
        List<String> propertyNames = JavaDBDataSource.saveProps;
        query.append("update DOWNLOAD\n set ");
        query.append(propertyNames.get(0));
        for (int i = 1; i < propertyNames.size(); i++) {
            query.append(" = ?, ").append(propertyNames.get(i));
        }

        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query.toString());
            int param = 1;
            for (String s : propertyNames) {
                Object o = download.getProperty(s);
                setObjectParam(statement, param, o);
                param++;
            }

            int executeUpdate = statement.executeUpdate();
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "returned {0} on {1}", new Object[]{executeUpdate, query.toString()});
            statement.close();

            if (executeUpdate != 1) {
                throw new IllegalStateException("executeUpdate is " + executeUpdate + " there should be 1 update");
            }

            LinkDAO linkDao = DAOFactory.getInstance().getLinkDAO();

            List<String> href = linkDao.getLinks(download.getId(), Download.HREF);
            List<String> copy = new ArrayList<>();
            Collections.copy(copy, download.getHrefLinks());
            copy.removeAll(href);
            linkDao.addLinks(download.getId(), copy, Download.HREF);

            List<String> src = linkDao.getLinks(download.getId(), Download.SRC);
            copy.clear();
            Collections.copy(copy, download.getSrcLinks());
            copy.removeAll(src);
            linkDao.addLinks(download.getId(), copy, Download.SRC);

            List<String> locations = linkDao.getLinks(download.getId(), Download.REDIRECT);
            copy.clear();
            Collections.copy(copy, download.getLocations());
            copy.removeAll(locations);
            linkDao.addLinks(download.getId(), copy, Download.REDIRECT);

            WordDAO wordDao = DAOFactory.getInstance().getWordDAO();

            List<String> words = wordDao.getWords(download.getId());
            copy.clear();
            Collections.copy(copy, download.getWords());
            copy.removeAll(words);
            wordDao.addWords(download.getId(), copy);

            for (String s : download.getExtraProperties().keySet()) {
                DAOFactory.getInstance().getPropertyDAO().saveProperty(download.getId(), s, download.getExtraProperties().get(s));
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteDownload(UUID uuid) {
        if (!downloadExists(uuid)) {
            throw new IllegalArgumentException(uuid + " does not exist in db");
        }
        DAOFactory.getInstance().getLinkDAO().deleteLinks(uuid);
        DAOFactory.getInstance().getWordDAO().deleteWords(uuid);
        DAOFactory.getInstance().getPropertyDAO().deleteProperties(uuid);
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "delete from download where " + Download.PROP_ID + " = '" + uuid + "'";
            int executeUpdate = statement.executeUpdate(query);
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, "returned {0} on {1}", new Object[]{executeUpdate, query});
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
    }

    @Override
    public void clearDownloads(List<Download> downloads) {
        for (Download d : downloads) {
            deleteDownload(d.getId());
        }
    }

    @Override
    public void clearDownloads() {
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "delete from url";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            statement.executeUpdate(query);
            query = "delete from word";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            statement.executeUpdate(query);
            query = "delete from property";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            statement.executeUpdate(query);
            query = "delete from download";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
        } finally {
            JavaDBDataSource.getInstance().returnConnection(connection);
        }
    }

    private boolean downloadExists(UUID uuid) {
        boolean r = false;
        Connection connection = JavaDBDataSource.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "select " + Download.PROP_ID + " from download where " + Download.PROP_ID + " = '" + uuid + "'";
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.FINE, query);
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                r = true;
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(JavaDBDownloadDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("There was an SQL Exception", ex);
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

    private static void printDownloads(List<Download> downloads) {
        for (Download d : downloads) {
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
        Download google = new Download(UUID.randomUUID());
        google.setUrl(new URL("http://www.google.com/"));
        google.addHrefLink("http://www.gmail.com/");
        google.addHrefLink("http://www.gmail.com/");
        google.addHrefLink("http://www.gmail.com/asdf");
        google.addSrcLink("http://www.igoogle.com/logo.jpg");
        dao.insertDownload(google);
        Download gmail = new Download(UUID.randomUUID());
        gmail.setUrl(new URL("http://www.gmail.com/"));
        dao.insertDownload(gmail);
        google.addHrefLink("http://www.hotmail.com/");
        DAOFactory.getInstance().getLinkDAO().addLink(gmail.getId(), "http://www.yahoo.com/", "src");
        DAOFactory.getInstance().getLinkDAO().addLink(gmail.getId(), "http://www.yahoo.com/", "src");
        DAOFactory.getInstance().getLinkDAO().addLink(gmail.getId(), "http://www.yahoo.com/", "href");
        google.setDownloaded(80);
        dao.updateDownload(google, Download.PROP_DOWNLOADED);
        printDownloads(dao.getDownloads());
        dao.deleteDownload(google.getId());
        printDownloads(dao.getDownloads());
        dao.clearDownloads();
        printDownloads(dao.getDownloads());
        db.deleteDB();
    }
}
