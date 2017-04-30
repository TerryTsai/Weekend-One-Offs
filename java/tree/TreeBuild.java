package tree;

import java.util.Stack;
import java.util.function.Predicate;

public enum TreeBuild {

    Service;

    public <T> TreeNode<T> preOrder(Predicate<T> isLeaf, T... values) {
        TreeNode<T> root = new TreeNode<T>(null);
        Stack<TreeNode<T>> stack = new Stack<>();
        stack.push(root);
        for (T value : values) {
            TreeNode<T> curr = new TreeNode<T>(value);
            TreeNode<T> prev = stack.peek();
            if (prev.left == null) {
                prev.left = curr;
            } else if (prev.right == null) {
                prev.right = curr;
                stack.pop();
            }
            if (!isLeaf.test(curr.value))
                stack.push(curr);
        }
        return root.left;
    }

}
