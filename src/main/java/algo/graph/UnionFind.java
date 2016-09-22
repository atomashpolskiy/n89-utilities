package algo.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class UnionFind<T> {

    public static <T> Builder<T> empty() {
        return new Builder<>();
    }

    public static <T> Builder<T> elements(Set<T> elements) {
        return new Builder<>(elements);
    }

    private final boolean allowFindReturnNull;

    protected final Map<T, T> parents;
    protected final Map<T, Integer> ranks;

    protected UnionFind(Set<T> elements, boolean allowFindReturnNull) {

        Map<T, T> parents = new HashMap<>();
        Map<T, Integer> ranks = new HashMap<>();
        if (!elements.isEmpty()) {
            elements.forEach(element -> addElement(parents, ranks, element));
        }
        this.parents = parents;
        this.ranks = ranks;

        this.allowFindReturnNull = allowFindReturnNull;
    }

    public boolean contains(T element) {
        return parents.containsKey(element);
    }

    public void add(T element) {
        if (parents.containsKey(element)) {
            return;
        }
        addElement(parents, ranks, element);
    }

    private void addElement(Map<T, T> parents, Map<T, Integer> ranks, T element) {
        Objects.requireNonNull(element);
        parents.put(element, element);
        ranks.put(element, 0);
    }

    /**
     * @return Element's parent or null if element is not in this set and allowFindReturnNull is set to true
     * @throws IllegalArgumentException if element is not in this set and allowFindReturnNull is set to false
     */
    public T find(T element) {

        T parent = parents.get(Objects.requireNonNull(element));
        if (parent == null) {
            if (allowFindReturnNull) {
                return null;
            } else {
                throw new IllegalArgumentException("Element does not belong to this set");
            }
        }

        return (element == parent) ? element : find(parent);
    }

    /**
     * If the ranks of parents of two elements are equal,
     * then parent of the first element is set as the parent of the second element.
     */
    public void union(T first, T second) {

        if (!parents.containsKey(Objects.requireNonNull(first))) {
            throw new IllegalArgumentException("First element does not belong to this set");
        }
        else if (!parents.containsKey(Objects.requireNonNull(second))) {
            throw new IllegalArgumentException("Second element does not belong to this set");
        }

        if (first == second) {
            return;
        }

        T parentFirst = find(first), parentSecond = find(second);
        if (parentFirst == parentSecond) {
            // elements are already in the same set
            return;
        }

        Integer rankFirst = ranks.get(parentFirst), rankSecond = ranks.get(parentSecond);
        if (rankFirst < rankSecond) {
            parents.put(parentFirst, parentSecond);
        }
        else if (rankFirst > rankSecond) {
            parents.put(parentSecond, parentFirst);
        }
        else {
            parents.put(parentSecond, parentFirst);
            ranks.put(parentFirst, rankFirst + 1);
        }
    }

    public static class Builder<T> {

        private Set<T> elements;
        private boolean allowFindReturnNull;

        public Builder() {
            this.elements = Collections.emptySet();
        }

        public Builder(Set<T> elements) {
            this.elements = Objects.requireNonNull(elements);
        }

        public Builder allowFindReturnNull() {
            this.allowFindReturnNull = true;
            return this;
        }

        public UnionFind<T> build() {
            return new UnionFind<>(elements, allowFindReturnNull);
        }
    }
}
