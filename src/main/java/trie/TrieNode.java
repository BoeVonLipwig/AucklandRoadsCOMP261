package trie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shaun Sinclair
 * COMP 261
 * 20/03/2017.
 */
public class TrieNode {
    private boolean hasChild = false;
    private boolean isWord = false;
    private char nodeID;
    private TrieNode parent;
    TrieNode[] children;

    private TrieNode(char c) {
        this();
        nodeID = c;
    }

    TrieNode() {
        children = new TrieNode[256];
    }

    List<String> search() {
        List<String> find = new ArrayList<>();
        if (isWord) find.add(toString());
        if (hasChild) {
            for (TrieNode c : children) {
                if (c != null) find.addAll(c.search());
            }
        }
        return find;
    }

    void add(String s) {
        if (s.length() == 0) {
            isWord = true;
            return;
        }
        hasChild = true;
        char c = s.charAt(0);
        if (children[c] == null) {
            children[c] = new TrieNode(c);
            children[c].parent = this;
        }
        children[c].add(s.substring(1));

    }

    @Override
    public String toString() {
        if (parent.nodeID == Character.UNASSIGNED) {
            return String.valueOf(nodeID);
        } else {
            return parent.toString() + String.valueOf(nodeID);
        }
    }
}
