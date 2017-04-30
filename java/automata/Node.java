package automata;

import java.util.*;

@SuppressWarnings({"WeakerAccess", "unused"})
class Node<S> {

    private Set<Node<S>> defaults;
    private Set<Node<S>> epsilons;
    private Map<S, Set<Node<S>>> explicits;

    public Node() {
        this.defaults = new HashSet<>();
        this.epsilons = new HashSet<>();
        this.explicits = new HashMap<>();
    }

    public void add(S s, Node<S> node) {
        if (s == null)
            throw new NullPointerException();
        if (node == null)
            throw new NullPointerException();

        Set<Node<S>> nodes = explicits
                .computeIfAbsent(s, k -> new HashSet<>());
        nodes.add(node);
    }

    public void addDefault(Node<S> node) {
        defaults.add(node);
    }

    public void addEpsilon(Node<S> node) {
        epsilons.add(node);
    }

    public Set<Node<S>> get(S s) {
        if (s == null)
            throw new NullPointerException();

        Set<Node<S>> explicit = explicits.get(s);
        return (explicit == null) ? Collections.EMPTY_SET : explicit;
    }

    public Set<Node<S>> getDefaults() {
        return defaults;
    }

    public Set<Node<S>> getEpsilons() {
        return epsilons;
    }

}