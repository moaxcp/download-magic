/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moaxcp.downloadmanager.model.db;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author john
 */
public interface LinkDAO {
    
    public List<String> getLinks(UUID uuid, String type);

    public void addLink(UUID uuid, String link, String type);

    public void addLinks(UUID uuid, List<String> list, String type);

    public void deleteLinks(UUID uuid);

    public void deleteLinks(UUID uuid, List<String> list);

    public long getUrlCount();
    
}
