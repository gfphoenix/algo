package open.ds;

/**
 * Created by wuhao on 7/22/16.
 */
public class TreeNode3<T extends Comparable<T>> extends TreeNode<T> {
    TreeNode3<T> parent;
    public TreeNode3(T x) {
        super(x);
    }

    public static <T extends Comparable<T>> TreeNode3<T>
    __left_rotate(TreeNode3<T> node, TreeNode3<T> root) {
        TreeNode3<T> parent = node.parent;
        TreeNode3<T> right = (TreeNode3<T>)node.right;

        if ((node.right = right.left) != null)
            ((TreeNode3<T>) node.right).parent = node;
        node.parent = right;
        right.left = node;
        right.parent = parent;

        if (parent!=null) {
            if (parent.left == node)
                parent.left = right;
            else
                parent.right = right;
            return root;
        }
        return right;
    }
    public static <T extends Comparable<T>> TreeNode3<T>
    __right_rotate(TreeNode3<T> node, TreeNode3<T> root) {

        TreeNode3<T> parent = node.parent;
        TreeNode3<T> left = (TreeNode3<T>) node.left;

        if ((node.left=left.left)!=null)
            ((TreeNode3<T>) node.left).parent = node;
        node.parent = left;
        left.right = node;
        left.parent = parent;
        if (parent!=null) {
            if (parent.left == node)
                parent.left = left;
            else
                parent.right = left;
            return root;
        }
        return left;
    }
    public static <T extends Comparable<T>> TreeNode3<T>
    __lr_rotate(TreeNode3<T> parent, TreeNode3<T> root) {
        root = __left_rotate((TreeNode3<T>)parent.left, root);
        root = __right_rotate(parent, root);
        return root;
    }
    public static <T extends Comparable<T>> TreeNode3<T>
    __rl_rotate(TreeNode3<T> parent, TreeNode3<T> root) {
        root = __right_rotate((TreeNode3<T>)parent.right, root);
        root = __left_rotate(parent, root);
        return root;
    }

    public TreeNode3<T> left_rotate(TreeNode3<T> root) {
        return __left_rotate(this, root);
    }
    public TreeNode3<T> right_rotate(TreeNode3<T> root) {
        return __right_rotate(this, root);
    }
    public TreeNode3<T> LR_rotate(TreeNode3<T> root) {
        return __lr_rotate(this, root);
    }
    public TreeNode3<T> RL_rotate(TreeNode3<T> root) {
        return __rl_rotate(this, root);
    }
}
