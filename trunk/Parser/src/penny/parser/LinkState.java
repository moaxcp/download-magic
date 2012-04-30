/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.parser;

import java.io.Serializable;

/**
 *
 * @author john
 */
public class LinkState implements Serializable {
    private StringBuffer link;
    private boolean extract;
    private boolean srcLink;

    public LinkState() {
        link = new StringBuffer();
        extract = false;
        srcLink = false;
    }

    /**
     * @return the link
     */
    public StringBuffer getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(StringBuffer link) {
        this.link = link;
    }

    /**
     * @return the extract
     */
    public boolean isExtract() {
        return extract;
    }

    /**
     * @param extract the extract to set
     */
    public void setExtract(boolean extract) {
        this.extract = extract;
    }

    /**
     * @return the srcLink
     */
    public boolean isSrcLink() {
        return srcLink;
    }

    /**
     * @param srcLink the srcLink to set
     */
    public void setSrcLink(boolean srcLink) {
        this.srcLink = srcLink;
    }

    public String toString() {
        return link.toString();
    }
}
