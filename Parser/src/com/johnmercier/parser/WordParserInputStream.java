/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnmercier.parser;

import java.io.IOException;
import java.io.InputStream;
import java.text.StringCharacterIterator;

/**
 *
 * @author john
 */
public class WordParserInputStream extends InputStream {

    private InputStream in;
    private WordEater eater;
    private WordReplacer replacer;
    private StringCharacterIterator replaceWord;

    public WordParserInputStream(InputStream in, WordEater eater) {
        this.in = in;
        this.eater = eater;
        this.replaceWord = new StringCharacterIterator("");
    }

    public WordParserInputStream(InputStream in, WordEater eater, WordReplacer replacer) {
        this.in = in;
        this.eater = eater;
        this.replacer = replacer;
        this.replaceWord = new StringCharacterIterator("");
    }

    @Override
    public int read() throws IOException {
        int returnCharacter = -1;
        returnCharacter = replaceWord.next();
        if (returnCharacter == StringCharacterIterator.DONE) {
            returnCharacter = in.read();
            if (Character.isLetter(returnCharacter)) {
                StringBuffer word = new StringBuffer();
                while (Character.isLetter(returnCharacter)) {
                    word.append((char) returnCharacter);
                    returnCharacter = in.read();
                }
                eater.eatWord(word.toString());
                if (replacer != null) {
                    replaceWord.setText(replacer.getReplaceWord(word.toString()) + (char) returnCharacter);
                } else {
                    replaceWord.setText(word.toString() + (char) returnCharacter);
                }
                returnCharacter = replaceWord.first();
            }
        }
        return returnCharacter;
    }

    /**
     * @return the in
     */
    public InputStream getIn() {
        return in;
    }
}
