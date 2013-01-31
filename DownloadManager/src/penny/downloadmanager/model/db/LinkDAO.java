/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model.db;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author john
 */
public interface LinkDAO {

    public void addLink(UUID uuid, String link, String type);

    public void addLinks(UUID uuid, List<String> list, String type);

    public void addLink(UUID uuid, String link, String type, int count);

    public void deleteLinks(UUID uuid);

    public void deleteLinks(UUID uuid, List<String> list, String type);

    public long getUrlCount();
    
}
