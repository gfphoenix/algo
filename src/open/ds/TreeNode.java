package open.ds;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TreeNode<T extends Comparable<T>> implements Comparable<TreeNode<T>> {
	T value;
	TreeNode<T> left;
	TreeNode<T> right;

	public TreeNode(T x) {
		this.value = x;
	}

	public List<TreeNode<T>> preorderTraversal(TreeNode<T> root) {
		ArrayList<TreeNode<T>> list = new ArrayList<>();
		Stack<TreeNode<T>> stk = new Stack<>();
		if (root != null)
			stk.add(root);
		TreeNode<T> node;
		while (!stk.isEmpty()) {
			node = stk.pop();
			list.add(node);
			if (node.right != null)	stk.add(node.right);
			if (node.left != null)	stk.add(node.left);
		}
		return list;
	}

	public List<TreeNode<T>> inorderTraversal(TreeNode<T> root) {
		ArrayList<TreeNode<T>> list = new ArrayList<>();
		Stack<TreeNode<T>> stk = new Stack<>();
		TreeNode<T> node;
		for (node=root; node!=null; node=node.left)
			stk.add(node);
		while (!stk.isEmpty()) {
			node = stk.pop();
			list.add(node);
			for (node=node.right; node!=null; node=node.left)
				stk.add(node);
		}
		return list;
	}

	public List<TreeNode<T>> postorderTraversal(TreeNode<T> root) {
		ArrayList<TreeNode<T>> list = new ArrayList<>();
		Stack<TreeNode<T>> stk = new Stack<>();
		TreeNode<T> node, parent;
		for (node=root; node!=null; node=node.left!=null?node.left:node.right)
			stk.add(node);

		while (!stk.isEmpty()) {
			node = stk.pop();
			list.add(node);
			if (!stk.isEmpty() && (parent=stk.peek()).left==node)
				for(node=parent.right; node!=null; node=node.left!=null?node.left:node.right)
					stk.add(node);
		}
		return list;
	}

	public List<TreeNode<T>> levelOrder(TreeNode<T> root) {
		List<TreeNode<T>> L = new ArrayList<>();
		if (root != null)
			L.add(root);
		for (int i = 0; i < L.size(); i++) {
			root = L.get(i);
			if (root.left != null)	L.add(root.left);
			if (root.right != null)	L.add(root.right);
		}
		return L;
	}

    @Override
    public int compareTo(TreeNode<T> o) {
        return value.compareTo(o.value);
    }
}
