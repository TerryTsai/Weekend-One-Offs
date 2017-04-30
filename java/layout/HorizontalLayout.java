package layout;

/**
 * Computing nested horizontal layouts that
 * can shrink and grow within parent layout.
 */
class HorizontalLayout {

    // Shrink takes precedence over grow.
    private static final int FIXED = 0;
    public static final int SHRINK = -1;
    public static final int GROW = -2;

    private int width, height;
    private boolean horizontal;
    private HorizontalLayout[] children;

    // Root view must be fixed size.
    HorizontalLayout(int width, int height, boolean horizontal, HorizontalLayout... children) {
        this.width = width;
        this.height = height;
        this.horizontal = horizontal;
        this.children = children;
    }

    void compute() {
        computeHorizontal(width, height);
    }

    void computeHorizontal(int maxWidth, int maxHeight) {
        if (width > maxWidth || height > maxHeight)
            throw new RuntimeException("Fixed constraints exceeds max dimension.");

        int computedHeight = 0;
        int remainingChildren = children.length;
        int remainingWidth = (width >= FIXED) ? width : maxWidth;

        int fixedWidth = 0;
        for (HorizontalLayout child : children) {
            if (child.width >= FIXED) {
                child.computeHorizontal(child.width, maxHeight);
                computedHeight = Math.max(computedHeight, child.height);
                fixedWidth += child.width;
                remainingChildren--;
            }
        }
        remainingWidth -= fixedWidth;

        int shrinkWidth = 0;
        for (HorizontalLayout child : children) {
            if (child.width == SHRINK) {
                child.computeHorizontal(remainingWidth, maxHeight);
                computedHeight = Math.max(computedHeight, child.height);
                shrinkWidth += child.width;
                remainingChildren--;
            }
        }
        remainingWidth -= shrinkWidth;

        int growWidth = 0;
        for (HorizontalLayout child : children) {
            if (child.width == GROW) {
                child.computeHorizontal(remainingWidth / remainingChildren, maxHeight);
                computedHeight = Math.max(computedHeight, child.height);
                growWidth += child.width;
            }
        }
        remainingWidth -= growWidth;

        if (remainingWidth < 0 || computedHeight > maxHeight)
            throw new RuntimeException("Total child constraints exceed max dimensions.");
        if (width == SHRINK)
            width = maxWidth - remainingWidth;
        if (width == GROW)
            width = maxWidth;
        if (height == SHRINK)
            height = computedHeight;
        if (height == GROW)
            height = maxHeight;
    }

    void print() {
        print("", "   ");
    }

    void print(String acc, String pad) {
        System.out.println(acc + pad + "[ " + width + " , " + height + " ]");
        for (HorizontalLayout child : children) {
            child.print(acc + pad, pad);
        }
    }

    public static void main(String[] args) {
        right();
        split();
        left();
        test();
    }

    private static void test() {
        HorizontalLayout x = new HorizontalLayout(GROW, 10, false);
        HorizontalLayout y = new HorizontalLayout(2, 10, false);
        HorizontalLayout z = new HorizontalLayout(GROW, 10, false);

        HorizontalLayout a = new HorizontalLayout(10, SHRINK, false, x, y, z);
        HorizontalLayout b = new HorizontalLayout(GROW, GROW, false);
        HorizontalLayout c = new HorizontalLayout(20, GROW, false);
        HorizontalLayout d = new HorizontalLayout(GROW, GROW, false);
        HorizontalLayout e = new HorizontalLayout(10, GROW, false);
        HorizontalLayout container = new HorizontalLayout(100, 100, false, a, b, c, d, e);
        container.compute();
        container.print();
    }

    private static void right() {
        HorizontalLayout sidebar = new HorizontalLayout(10, GROW, false);
        HorizontalLayout content = new HorizontalLayout(GROW, GROW, false);
        HorizontalLayout container = new HorizontalLayout(100, 100, false, content, sidebar);
        container.compute();
        container.print("", "  ");
    }

    private static void left() {
        HorizontalLayout sidebar = new HorizontalLayout(10, GROW, false);
        HorizontalLayout content = new HorizontalLayout(GROW, GROW, false);
        HorizontalLayout container = new HorizontalLayout(200, 100, false, sidebar, content);
        container.compute();
        container.print("", "  ");
    }

    private static void split() {
        HorizontalLayout g1 = new HorizontalLayout(GROW, GROW, false);
        HorizontalLayout g2 = new HorizontalLayout(GROW, GROW, false);

        HorizontalLayout view = new HorizontalLayout(100, 100, false, g1, g2);
        view.compute();
        view.print("", "  ");
    }
}
