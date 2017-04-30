package tree;

public class TreeTest {

    public static void main(String[] args) {

        TreeNode<String> tree =
                TreeBuild.Service.preOrder((o) -> o.endsWith("."),
                        "a", "b.", "c", "d.", "e", "f.", "g", "h.", "i.");
        System.out.println(TreeString.Service.preOrder(tree));

    }
}
