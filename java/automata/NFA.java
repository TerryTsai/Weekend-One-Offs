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
        testResource();
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
        System.out.println(nfa.accepts('a','b','c'));
        System.out.println(nfa.accepts('a','b','c','d'));
    }

    public static void testEvenOnes() {
        Node<Character> a = new Node<>();
        Node<Character> b = new Node<>();
        a.add('1', b);
        a.addDefault(a);
        b.add('1', a);
        b.addDefault(b);
        NFA<Character> nfa = new NFA<>(a, a);
        System.out.println(nfa.accepts('0'));
        System.out.println(nfa.accepts('1'));
        System.out.println(nfa.accepts('0','0'));
        System.out.println(nfa.accepts('1','0'));
        System.out.println(nfa.accepts('0','1'));
        System.out.println(nfa.accepts('1','1'));
    }

    public static void testResource() {
        Runnable open = () -> System.out.println("Opening Resource.");
        Runnable close = () -> System.out.println("Closing Resource.");
        Runnable other = () -> System.out.println("Some Other Runnable.");
        Node<Runnable> a = new Node<>();
        Node<Runnable> b = new Node<>();
        Node<Runnable> c = new Node<>();
        a.add(open, b);
        a.add(close, c);
        a.addDefault(a);
        b.add(close, a);
        b.add(open, c);
        b.addDefault(b);
        NFA<Runnable> nfa = new NFA<>(a, a);
        System.out.println(nfa.accepts(open, close));
        System.out.println(nfa.accepts(open, other, close));
        System.out.println(nfa.accepts(open, close, close));
        System.out.println(nfa.accepts(close, open));
    }

}
