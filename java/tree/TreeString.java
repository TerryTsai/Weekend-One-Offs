package tree;

public enum TreeString {

    Service;

    public <T> String preOrder(TreeNode<T> tree) {
        return String.format("( %s %s %s )",
                tree.value.toString(),
                (tree.left == null) ? "-" : preOrder(tree.left),
                (tree.right == null) ? "-" : preOrder(tree.right));
    }

    public <T> String postOrder(TreeNode<T> tree) {
        return String.format("( %s %s %s )",
                (tree.left == null) ? "-" : preOrder(tree.left),
                (tree.right == null) ? "-" : preOrder(tree.right),
                tree.value.toString());
    }

    public <T> String inOrder(TreeNode<T> tree) {
        return String.format("( %s %s %s )",
                (tree.left == null) ? "-" : preOrder(tree.left),
                tree.value.toString(),
                (tree.right == null) ? "-" : preOrder(tree.right));
    }


}
