package tree;

import java.util.Comparator;

public enum TreeSearch {

    Service;

    public <T> TreeNode<T> binarySearch(TreeNode<T> tree, T value, Comparator<T> comparator) {
        int compare = comparator.compare(value, tree.value);
        if (compare == 0) {
            return tree;
        } else if (compare < 0 && tree.left != null) {
            return binarySearch(tree.left, value, comparator);
        } else if (compare > 0 && tree.right != null) {
            return binarySearch(tree.right, value, comparator);
        } else {
            return null;
        }
    }
}
