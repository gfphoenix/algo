package open.ds.btree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhao on 7/25/16.
 */
public class Btree<K extends Comparable<K>> {
    int M;

    enum NodeType {
        LEAF, POINTER,
    }

    class Node<K> {
        List<Node<K>> children;
        List<K> values;
        Node<K> parent;

        Node(boolean leaf) {
            if (leaf) values = new ArrayList<>();
            else children = new ArrayList<>();
        }

        public boolean isLeaf() {
            return values != null;
        }

        public boolean isRoot() {
            return parent == null;
        }

        public int size() {
            return isLeaf() ? values.size() : children.size();
        }
    }

    // root default to leaf node
    Node<K> root = new Node<>(true);


    Node<K> chooseLeaf(Node<K> node, K k) {
        if (node.isLeaf()) return node;
        for (int i=0, n=node.children.size(); i<n; i++) {

        }
        return node;
    }

    void add(K k) {
        Node node = chooseLeaf(root, k);
        add(node, k);
    }

    void add(Node<K> node, K k) {
        addValue(node, k);
        checkAdd(node);
    }

    int compare(K a, K b) {
        return 0;
    }

    // add values and sort
    void addValue(Node<K> node, K k) {
        assert node.isLeaf();
        List<K> values = node.values;
        int i;
        int n = values.size();
        if (n == 0) {
            values.add(k);
            return;
        }
        for (i = 0; i < n; i++) {
            K v = values.get(i);
            if (compare(v, k) > 0) break;
        }
        values.add(k);
        if (i < n)
            for (int j = n - 1; j > i; j--)
                values.set(j, values.get(j - 1));
        values.set(i, k);
    }

    void checkAdd(Node<K> node) {

    }
}
