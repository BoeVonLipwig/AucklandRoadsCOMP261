package trie;

import java.util.Collections;
import java.util.List;

/**
 * Created by Shaun Sinclair
 * COMP 261
 * 20/03/2017.
 */

public class Trie {

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void add(String s) {
        root.add(s);
    }

    public List<String> find(String s) {
        TrieNode cur = root;
        for (char c : s.toCharArray()) {
            cur = cur.children[c];
            if (cur == null) return Collections.emptyList();
        }
        return cur.search();
    }
}
//catch
