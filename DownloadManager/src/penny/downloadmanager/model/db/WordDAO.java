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
public interface WordDAO {
    public List<String> getWords(UUID uuid);
    public void addWord(UUID uuid, String word);
    public void addWords(UUID uuid, List<String> words);
    public void deleteWords(UUID uuid);
    public void deleteWords(UUID uuid, List<String> words);
    public long getWordCount();
}
