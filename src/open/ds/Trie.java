package open.ds;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Trie {
	private static class TrieNode {
		// Initialize your data structure here.
		public TrieNode() {
			word = false;
		}
		// i = 0, 1, ... 25
		TrieNode at(int i){
			if(child == null) child = new TrieNode[26];
			if(child[i] == null) child[i] = new TrieNode();
			return child[i];
		}
		TrieNode tryAt(int i){
			return child==null ? null : child[i];
		}
		TrieNode []child;
		boolean word;
	}
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // Inserts a word into the trie.
    public void insert(CharSequence word) {
    	TrieNode node = root;
        for(int i=0,n=word.length(); i<n; i++){
        	int idx = word.charAt(i) - 'a';
        	node = node.at(idx);
        }
        node.word = true;
    }

    // Returns if the word is in the trie.
    public boolean search(CharSequence word) {
    	TrieNode node = root;
        for(int i=0,n=word.length(); i<n; i++){
        	int idx = word.charAt(i) - 'a';
        	node = node.tryAt(idx);
        	if(node == null) return false;
        }
        return node.word;
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(CharSequence prefix) {
    	TrieNode node = root;
        for(int i=0,n=prefix.length(); i<n; i++){
        	int idx = prefix.charAt(i) - 'a';
        	node = node.tryAt(idx);
        	if(node == null) return false;
        }
        return true;
    }
    
    // others
    // two word search , mainly use dfs
    ///////////////////////////////////
    //// WORD SEARCH ...
    public List<String> findWords(char[][] board, String[] words) {
        ArrayList<String> list = new ArrayList<>();
        if(words==null || words.length==0 
        		|| board==null || board[0].length==0) return list;
        final int M = board.length;
        final int N = board[0].length;
        Trie trie = new Trie();
        int maxlen = words[0].length();
        for(int i=0; i<words.length; i++){
        	trie.insert(words[i]);
        	if(maxlen<words[i].length())
        		maxlen = words[i].length();
        }
        char []buffer = new char[maxlen+1];
        boolean [][]visited = new boolean[M][];
        for(int i=0; i<visited.length; i++)
        	visited[i] = new boolean[N];
        
        HashSet<String> set = new HashSet<>(M+N);
        for(int i=0; i<M; i++)
        	for(int j=0; j<N; j++)
        		dfs_visit(set, board, trie, buffer, visited, i, j, 0);
        
        list.addAll(set);
        return list;
    }
    
	private void dfs_visit(Set<String> set, char[][] board, Trie trie,
			char []buffer, boolean[][] visited, int i, int j, int chars) {
		buffer[chars++] = board[i][j];
		visited[i][j] = true;
		final int ni = board.length;
		final int nj = board[0].length;
		// check 
		// 1. exactly a word, maybe also a valid prefix
		// 2. make a valid prefix
		// 3. no prefix
		int ret = query(trie, buffer, chars);
		
		switch (ret) {
		case 1:
			set.add(new String(buffer, 0, chars));
			// fall
		case 2:
			// prefix
			if(i-1>=0 && !visited[i-1][j]) dfs_visit(set, board, trie, buffer, visited, i-1, j, chars);
			if(i+1<ni && !visited[i+1][j]) dfs_visit(set, board, trie, buffer, visited, i+1, j, chars);
			if(j-1>=0 && !visited[i][j-1]) dfs_visit(set, board, trie, buffer, visited, i, j-1, chars);
			if(j+1<nj && !visited[i][j+1]) dfs_visit(set, board, trie, buffer, visited, i, j+1, chars);
			
			break;
		case 3:
		default:
			break;
		}
		visited[i][j] = false;
	}

	private int query(Trie trie, char[] buffer, int chars) {
		TrieNode node = root;
        for(int i=0,n=chars; i<n; i++){
        	int idx = buffer[i] - 'a';
        	node = node.tryAt(idx);
        	if(node == null) return 3;
        }
        return node.word ? 1 : 2;
	}
	
	// word search I
	// 0 : exist
	// 1 : good search
	// 2 : not match
	boolean search1(char[][] board, String word, boolean [][]visited, int i, int j, int index){
		boolean ok = board[i][j] == word.charAt(index);
		if(!ok) return false;
		if(index+1==word.length()) return true;

		final int ni = board.length;
		final int nj = board[0].length;
		visited[i][j] = true;
		index++;
		boolean ret;
		if(i-1>=0 && !visited[i-1][j]){
			ret = search1(board, word, visited, i-1, j, index);
			if(ret)
				return true;
		}
		if(i+1<ni && !visited[i+1][j]){
			ret = search1(board, word, visited, i+1, j, index);
			if(ret)
				return true;
		}
		if(j-1>=0 && !visited[i][j-1]) {
			ret = search1(board, word, visited, i, j-1, index);
			if(ret)
				return true;
		}
		if(j+1<nj && !visited[i][j+1]){
			ret = search1(board, word, visited, i, j+1, index);
			if(ret)
				return true;
		}
		visited[i][j] = false;
		return false;
	}
	public boolean exist(char[][] board, String word) {
		if(board==null || board.length==0
				|| board[0].length==0 || word==null || word.length()==0)
			return false;
		final int M = board.length;
		final int N = board[0].length;
		boolean [][]visited = new boolean[M][];
        for(int i=0; i<visited.length; i++){
        	visited[i] = new boolean[N];
        }
        for(int i=0; i<M; i++)
        	for(int j=0; j<N; j++)
        		if(search1(board, word, visited, i, j, 0))
        			return true;
        return false;
    }

}