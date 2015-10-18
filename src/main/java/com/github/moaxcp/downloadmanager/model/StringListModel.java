/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.moaxcp.downloadmanager.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.swing.AbstractListModel;

/**
 *
 * @author john
 */
public class StringListModel extends AbstractListModel implements List {
    ArrayList<String> list;

    public StringListModel() {
        list = new ArrayList<String>();
    }

    public int getSize() {
        return list.size();
    }

    public Object getElementAt(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(Object o) {
        return list.contains(o.toString());
    }

    public Iterator iterator() {
        return list.iterator();
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public Object[] toArray(Object[] a) {
        return list.toArray(a);
    }

    public boolean containsAll(Collection c) {
        return list.containsAll(c);
    }

    public boolean addAll(Collection c) {
        boolean b = list.addAll(c);
        this.fireContentsChanged(this, 0, list.size());
        return b;
    }

    public boolean addAll(int index, Collection c) {
        boolean b = list.addAll(index, c);
        this.fireContentsChanged(this, 0, list.size());
        return b;
    }

    public boolean removeAll(Collection c) {
        int size = list.size();
        boolean b = list.removeAll(c);
        this.fireContentsChanged(this, 0, size);
        return b;
    }

    public boolean retainAll(Collection c) {
        int size = list.size();
        boolean b = list.retainAll(c);
        this.fireContentsChanged(this, 0, size);
        return b;
    }

    public void clear() {
        int size = list.size();
        list.clear();
        this.fireIntervalRemoved(this, 0, size);
    }

    public Object set(int index, Object element) {
        Object o = list.set(index, element.toString());
        this.fireContentsChanged(this, index, index);
        return o;
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    public ListIterator listIterator() {
        return list.listIterator();
    }

    public ListIterator listIterator(int index) {
        return list.listIterator(index);
    }

    public List subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    public boolean add(String e) {
        int size = list.size();
        boolean b = list.add(e);
        this.fireIntervalAdded(this, size, size);
        return b;
    }

    public String get(int index) {
        return list.get(index);
    }

    public String set(int index, String element) {
        String r = list.set(index, element);
        this.fireContentsChanged(this, index, index);
        return r;
    }

    public void add(int index, String element) {
        list.add(index, element);
        this.fireContentsChanged(this, index, list.size());
    }

    public String remove(int index) {
        String s = list.remove(index);
        this.fireIntervalRemoved(this, index, index);
        return s;
    }

    public boolean remove(Object o) {
        int i = list.indexOf(o);
        boolean b = list.remove(o.toString());
        this.fireIntervalRemoved(this, i, i);
        return b;
    }

    public boolean add(Object e) {
        int size = list.size();
        boolean b = list.add(e.toString());
        this.fireIntervalAdded(this, size, size);
        return b;
    }

    public void add(int index, Object element) {
        int size = list.size();
        list.add(index, element.toString());
        this.fireIntervalAdded(this, index, size);
    }

}
