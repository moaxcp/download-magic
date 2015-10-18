/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package penny.parser;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author john
 */
public class LinkState implements Serializable {
    private StringBuilder link;
    private boolean extract;
    private boolean srcLink;

    public LinkState() {
        link = new StringBuilder();
        extract = false;
        srcLink = false;
    }

    public void copy(LinkState state) {
        link.delete(0, link.length());
        link.append(state.link);
        extract = state.extract;
        srcLink = state.srcLink;
    }

    /**
     * @return the link
     */
    public StringBuilder getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(StringBuilder link) {
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

    @Override
    public String toString() {
        return link.toString() + " " + extract + " " + srcLink;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof LinkState) {
            LinkState l = (LinkState) o;
            return link.toString().equals(l.toString()) && extract == l.extract && srcLink == l.srcLink;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.link);
        hash = 79 * hash + (this.extract ? 1 : 0);
        hash = 79 * hash + (this.srcLink ? 1 : 0);
        return hash;
    }
}
