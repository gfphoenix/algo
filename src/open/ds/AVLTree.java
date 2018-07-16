package open.ds;

/**
 * Created by wuhao on 7/22/16.
 */
public class AVLTree<Key extends Comparable<Key>, Value> {
    static class ANode<U extends Comparable<U>> extends TreeNode3<U> {
        int balance=0;
        public ANode(U x) {
            super(x);
        }
    }
    static class Pair<K extends Comparable<K>,V> implements Comparable<Pair<K, V>>{
        K key;
        V value;

        @Override
        public int compareTo(Pair<K, V> o) {
            return key.compareTo(o.key);
        }
        public K key(){
            return key;
        }
        public V value(){
            return value;
        }
    }
    ANode<Pair<Key,Value>> root;
    public AVLTree(){}


    // insert or update, return old value that match key
    Value set(Key key, Value value){
        return null;
    }
    Value search(Key key) {
        ANode<Pair<Key, Value>> node = root;
        while (node != null) {
            int cmp = node.value.key.compareTo(key);
            if (cmp == 0) return node.value.value;
            node = (ANode<Pair<Key, Value>>) (cmp > 0 ? node.left : node.right);
        }
        return null;
    }
    boolean has(Key key) {
        ANode<Pair<Key, Value>> node = root;
        while (node != null) {
            int cmp = node.value.key.compareTo(key);
            if (cmp == 0) return true;
            node = (ANode<Pair<Key, Value>>) (cmp > 0 ? node.left : node.right);
        }
        return false;
    }
    Value delete(Key key) {
        return null;
    }

    protected void fixup_insert(ANode<Pair<Key, Value>> x) {
        ANode<Pair<Key,Value>> parent, child;
        for (; (parent= (ANode<Pair<Key, Value>>) x.parent)!=null; x = parent) {
            if (parent.left == x) {
                parent.balance++;
                if (parent.balance == 0) break;
                if (parent.balance == 1) continue;

                if (x.balance == 1) { // LL
                    x.balance = 0;
                    parent.balance = 0;
                    root = (ANode<Pair<Key, Value>>) parent.right_rotate(root);
                    break;
                }
                child = (ANode<Pair<Key, Value>>) x.right;
                parent.balance = x.balance = 0;
                if (child.balance == 1) parent.balance = -1;
                else if (child.balance == -1) x.balance = 1;
                child.balance = 0;
                root = (ANode<Pair<Key, Value>>) parent.LR_rotate(root);
                break;
            } else {
                parent.balance--;
                if (parent.balance == 0) break;
                if (parent.balance == -1) continue;

                if (x.balance == -1) {
                    x.balance = parent.balance = 0;
                    root = (ANode<Pair<Key, Value>>) parent.left_rotate(root);
                    break;
                }
                child = (ANode<Pair<Key, Value>>) x.left;
                parent.balance = x.balance = 0;
                if (child.balance == -1) parent.balance = 1;
                else if (child.balance == 1) x.balance = -1;
                child.balance = 0;
                root = (ANode<Pair<Key, Value>>) parent.RL_rotate(root);
                break;
            }
        }
    }

    protected void fixup_del(ANode<Pair<Key, Value>> x, boolean left) {
        ANode<Pair<Key, Value>> parent, child, gc;
        for (; x!=null; x= (ANode<Pair<Key, Value>>) x.parent) {
            parent = (ANode<Pair<Key, Value>>) x.parent;
            if (left) {
                x.balance--;
                if (x.balance == -1) break;
                if (parent!=null && parent.right == x) left = false;
                if (x.balance == 0) continue;
                child = (ANode<Pair<Key, Value>>) x.right;
                if (child.balance == 0) {
                    x.balance = -1;
                    child.balance = 1;
                    root = (ANode<Pair<Key, Value>>) x.left_rotate(root);
                    break;
                }
                if (child.balance == -1) {
                    x.balance = child.balance = 0;
                    root = (ANode<Pair<Key, Value>>) x.left_rotate(root);
                    continue;
                }
                gc = (ANode<Pair<Key, Value>>) child.left;
                x.balance = child.balance = 0;
                if (gc.balance == -1) x.balance = 1;
                else if (gc.balance == 1) child.balance = -1;
                gc.balance = 0;
                root = (ANode<Pair<Key, Value>>) x.RL_rotate(root);
            }else {
                x.balance++;
                if (x.balance == 1) break;
                if (parent!=null && parent.left==x) left = true;
                if (x.balance == 0) continue;
                child = (ANode<Pair<Key, Value>>) x.left;
                if (child.balance == 0) {
                    x.balance = 1;
                    child.balance = -1;
                    root = (ANode<Pair<Key, Value>>) x.right_rotate(root);
                    break;
                }
                if (child.balance == 1) {
                    x.balance = child.balance = 0;
                    root = (ANode<Pair<Key, Value>>) x.right_rotate(root);
                    continue;
                }
                gc = (ANode<Pair<Key, Value>>) child.right;
                x.balance = child.balance = 0;
                if (gc.balance == 1) x.balance = -1;
                else if (gc.balance == -1) child.balance = -1;
                gc.balance = 0;
                root = (ANode<Pair<Key, Value>>) x.LR_rotate(root);
            }
        }
    }
}
