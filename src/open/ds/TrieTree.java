package open.ds;

import java.util.ArrayList;

/**
 * Created by wuhao on 6/30/16.
 */
public class TrieTree {
    public interface CharSet {
        default int size(){ return 26; }
        default int index(char ch) {
            if(ch>='a' && ch<='z') return ch-'a';
            if(ch>='A' && ch<='Z') return ch-'A';
            return -1;
        }

        // helper function, no need to override
        default boolean validString(CharSequence str) {
            for (int i=0,n=str.length(); i<n; i++)
                if(index(str.charAt(i)) == -1)
                    return false;
            return true;
        }
    }
    public static final  CharSet LOWER = new CharSet(){
        @Override
        public int index(char ch) {
            return ch>='a' && ch<='z' ? ch-'a' : -1;
        }
    };
    public static final CharSet UPPER = new CharSet(){
        @Override
        public int index(char ch) {
            return (ch<'A' || ch>'Z') ? -1 : ch - 'A';
        }
    };
    public static final CharSet LETTER = new CharSet() {
        @Override
        public int size() {
            return 52;
        }
        @Override
        public int index(char ch) {
            if(ch>='a' && ch<='z') return ch - 'a';
            if(ch>='A' && ch<='Z') return ch - 'A' + 26;
            return -1;
        }
    };
    public static final CharSet ALPHABET = new CharSet(){};
    private class Node {
        // Initialize your data structure here.
        private Node() {
            word = false;
        }
        Node at(int i){
            if(child == null) child = new Node[charSet.size()];
            if(child[i] == null) child[i] = new Node();
            return child[i];
        }
        Node tryAt(int i){
            return child==null ? null : child[i];
        }
        boolean tryNull(int i) {
            if (child==null || child[i]==null)
                return false;
            child[i] = null;
            return true;
        }
        void setNull(int i) {
            child[i] = null;
        }
        boolean hasChild() {
            if(child==null) return false;
            for (int i=0,n=child.length; i<n; i++)
                if (child[i] != null)
                    return true;
            return false;
        }
        Node []child;
        boolean word;
    }
    CharSet charSet;
    private Node root = new Node();

    public TrieTree(){
        this(LOWER);
    }
    public TrieTree(CharSet charset) {
        this.charSet = charset;
    }
    public CharSet getCharSet() {
        return charSet;
    }
    // Inserts a word into the trie.
    public void insert(CharSequence word) {
        Node node = root;
        for(int i=0,n=word.length(); i<n; i++){
            int idx = charSet.index(word.charAt(i));
            if(idx==-1)
                throw new IllegalArgumentException("bad char = "+word.charAt(i));
            node = node.at(idx);
        }
        node.word = true;
    }
    public boolean delete(CharSequence word) {
        int n = word.length();
        java.util.List<Node> list = new ArrayList<>(n+1);
        Node node = root;
        list.add(node);
        for (int i=0; i<n; i++) {
            int idx = charSet.index(word.charAt(i));
            if (idx == -1) return false;
            node = node.tryAt(idx);
            if(node == null) return false;
            list.add(node);
        }
        if (!node.word)
            return false;
        node.word = false;
        boolean has = node.hasChild();
        if(has) return true;
        for (int i=n-1; i>=0; i--) {
            node = list.get(i);
            int idx = charSet.index(word.charAt(i));
            node.setNull(idx);
            if(node.word || node.hasChild())
                break;
        }
        return true;
    }

    // Returns if the word is in the trie.
    public boolean search(CharSequence word) {
        Node node = root;
        for(int i=0,n=word.length(); i<n; i++){
            int idx = charSet.index(word.charAt(i));
            if(idx == -1) return false;
            node = node.tryAt(idx);
            if(node == null) return false;
        }
        return node.word;
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(CharSequence prefix) {
        Node node = root;
        for(int i=0,n=prefix.length(); i<n; i++){
            int idx = charSet.index(prefix.charAt(i));
            if (idx == -1) return false;
            node = node.tryAt(idx);
            if(node == null) return false;
        }
        return true;
    }
}
