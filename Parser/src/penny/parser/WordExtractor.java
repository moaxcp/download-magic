/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.parser;

import java.text.StringCharacterIterator;

/**
 *
 * @author john
 */
public class WordExtractor {

    private WordEater eater;
    StringBuilder word = new StringBuilder();

    public WordExtractor(WordEater eater) {
        this.eater = eater;
    }

    public void put(char c) {
        if (Character.isLetter(c)) {
            word.append(c);
        } else {
            if(word.length() > 0) {
                eater.eatWord(word.toString());
                word.setLength(0);
            }
        }
    }

    public void put(byte[] b, int off, int len) {
        if(len == -1) {
            eater.eatWord(word.toString());
            word.setLength(0);
        }
        for (int i = off; i < len; i++) {
            put((char) b[i]);
        }
    }

    public String getWordBuffer() {
        return word.toString();
    }

    public void setWordBuffer(String wordBuffer) {
        word.setLength(0);
        word.append(wordBuffer);
    }
}
