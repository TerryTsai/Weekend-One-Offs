package automata;

import java.util.*;

@SuppressWarnings({"unused", "unchecked", "FieldCanBeLocal", "WeakerAccess", "Convert2streamapi"})
public class NFA<T> {

    // Destructive Composition

    public static <S> NFA<S> repeat(NFA<S> nfa1) {
        for (Node<S> endNode : nfa1.endNodes)
            endNode.addEpsilon(nfa1.startNode);
        return new NFA<>(nfa1.startNode, nfa1.endNodes);
    }

    public static <S> NFA<S> concat(NFA<S> nfa1, NFA<S> nfa2) {
        for (Node<S> endNode : nfa1.endNodes)
            endNode.addEpsilon(nfa2.startNode);
        return new NFA<>(nfa1.startNode, nfa2.endNodes);
    }

    public static <S> NFA<S> either(NFA<S> nfa1, NFA<S> nfa2) {
        Node<S> startNode = new Node<>();
        startNode.addEpsilon(nfa1.startNode);
        startNode.addEpsilon(nfa2.startNode);

        List<Node<S>> endNodes = new ArrayList<>();
        endNodes.addAll(nfa1.endNodes);
        endNodes.addAll(nfa2.endNodes);

        return new NFA<>(startNode, endNodes);
    }

    // Implementation

    private Node<T> startNode;

    private Set<Node<T>> endNodes;

    public NFA(Node<T> startNode, Node<T> ... endNodes) {
        this(startNode, Arrays.asList(endNodes));
    }

    public NFA(Node<T> startNode, Collection<Node<T>> endNodes) {
        if (startNode == null)
            throw new NullPointerException();
        if (endNodes == null)
            throw new NullPointerException();
        if (endNodes.size() < 1)
            throw new RuntimeException("At least one endNodes node must be specified.");
        this.startNode = startNode;
        this.endNodes = new HashSet<>(endNodes);
    }

    public boolean accepts(T ... ts) {
        System.out.print(Arrays.toString(ts) + " - ");
        Set<Node<T>> currNodes = new HashSet<>();
        Set<Node<T>> nextNodes = new HashSet<>();

        currNodes.add(startNode);
        resolveEpsilons(currNodes, nextNodes);
        for (T t : ts) {
            resolveTransitions(currNodes, nextNodes, t);
            if (currNodes.isEmpty()) break;
            resolveEpsilons(currNodes, nextNodes);
        }

        for (Node<T> currNode : currNodes)
            if (endNodes.contains(currNode))
                return true;

        return false;
    }

    private void resolveTransitions(Set<Node<T>> currNodes, Set<Node<T>> nextNodes, T t) {
        if (t == null)
            throw new NullPointerException();
        if (currNodes == null)
            throw new NullPointerException();
        if (nextNodes == null)
            throw new NullPointerException();
        if (!nextNodes.isEmpty())
            throw new RuntimeException("Next set must be empty.");

        for (Node<T> currNode : currNodes)
            for (Node<T> nextNode : currNode.get(t))
                if (!nextNodes.contains(nextNode))
                    nextNodes.add(nextNode);

        if (nextNodes.isEmpty()) {
            for (Node<T> currNode : currNodes)
                for (Node<T> nextNode : currNode.getDefaults())
                    if (!nextNodes.contains(nextNode))
                        nextNodes.add(nextNode);
        }

        currNodes.clear();
        currNodes.addAll(nextNodes);
        nextNodes.clear();
    }

    private void resolveEpsilons(Set<Node<T>> currNodes, Set<Node<T>> nextNodes) {
        if (currNodes == null)
            throw new NullPointerException();
        if (nextNodes == null)
            throw new NullPointerException();
        if (!nextNodes.isEmpty())
            throw new RuntimeException("Next set must be empty.");

        do {
            currNodes.addAll(nextNodes);
            nextNodes.clear();
            for (Node<T> currNode : currNodes)
                for (Node<T> nextNode : currNode.getEpsilons())
                    if (!currNodes.contains(nextNode))
                        nextNodes.add(nextNode);
        } while (!nextNodes.isEmpty());
    }

    // Testing

    public static void main(String[] args) {
        testRepeat();
    }

    public static void testRepeat() {
        Node<Character> a = new Node<>();
        Node<Character> b = new Node<>();
        Node<Character> c = new Node<>();
        Node<Character> d = new Node<>();
        a.add('a', b);
        b.add('b', c);
        c.add('c', d);
        NFA<Character> nfa = NFA.repeat(new NFA<>(a, d));
        test(nfa, "ab", "abc", "abcab", "abcabc", "abcabcabcabc");
    }

    public static void testEvenOnes() {
        Node<Character> a = new Node<>();
        Node<Character> b = new Node<>();

        a.add('1', b);
        a.addDefault(a);
        b.add('1', a);
        b.addDefault(b);

        NFA<Character> nfa = new NFA<>(a, a);
        test(nfa, "kjew1flkdsajlk14");
        test(nfa, "0");
        test(nfa, "1");
        test(nfa, "00");
        test(nfa, "01");
        test(nfa, "10");
        test(nfa, "000");
        test(nfa, "001");
        test(nfa, "010");
        test(nfa, "011");
        test(nfa, "100");
        test(nfa, "101");
        test(nfa, "110");
        test(nfa, "111");
    }

    private static void test(NFA<Character> nfa, String ... tests) {
        for (String test : tests)
            test(nfa, test);
    }

    private static void test(NFA<Character> nfa, String test) {
        System.out.println(nfa.accepts(toArray(test)));
    }

    private static Character[] toArray(String str) {
        return str.chars().mapToObj(c -> (char) c).toArray(Character[]::new);
    }

}
