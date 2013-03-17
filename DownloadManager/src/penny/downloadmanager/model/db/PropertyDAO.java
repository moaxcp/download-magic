/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.model.db;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author john
 */
public interface PropertyDAO {
    
    public HashMap<String, Object> getProperties(UUID uuid);
    
    public List<String> getPropertyNames();

    public void saveProperty(UUID uuid, String name, Object property);

    public void deleteProperties(UUID uuid);

    public void deleteProperty(UUID uuid, String name);

    public long getPropertyCount();
    
}
