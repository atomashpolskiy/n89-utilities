package algo.graph;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class UnionFindTest {

    @Test
    public void testCreate_Empty() {
        UnionFindHelper<?> S = new UnionFindHelper<>();
        assertEquals(0, S.getParents().size());
        assertEquals(0, S.getRanks().size());
    }

    @Test
    public void testCreate_WithElements() {

        Object object1 = new Object(), object2 = new Object();
        Set<Object> elements = new HashSet<>(Arrays.asList(object1, object2));

        UnionFindHelper<Object> S = new UnionFindHelper<>(elements);

        elements.forEach(element -> assertTrue(S.contains(element)));

        assertEquals(elements.size(), S.getParents().size());
        assertEquals(elements.size(), S.getRanks().size());

        S.getParents().forEach(Assert::assertSame);
        S.getRanks().forEach((element, rank) -> assertEquals(Integer.valueOf(0), rank));
    }

    @Test
    public void testAdd_One() {

        UnionFindHelper<Object> S = new UnionFindHelper<>();

        Object object = new Object();
        S.add(object);

        assertTrue(S.contains(object));

        assertEquals(1, S.getParents().size());
        assertEquals(1, S.getRanks().size());

        assertEquals(object, S.getParents().get(object));
        assertEquals(Integer.valueOf(0), S.getRanks().get(object));
    }

    @Test
    public void testUnion_ChildAndParent() {

        Object child = new Object(), parent = new Object();
        Set<Object> elements = new HashSet<>(Arrays.asList(child, parent));

        UnionFindHelper<Object> S = new UnionFindHelper<>(elements);
        S.union(parent, child);

        assertSame(parent, S.getParents().get(child));
        assertSame(parent, S.getParents().get(parent));

        assertEquals(Integer.valueOf(0), S.getRanks().get(child));
        assertEquals(Integer.valueOf(1), S.getRanks().get(parent));

        assertSame(parent, S.find(child));
    }

    @Test
    public void testUnion_MergeSets() {

        Object child1 = new Object(), parent1 = new Object(), child2 = new Object(), parent2 = new Object();
        Set<Object> elements = new HashSet<>(Arrays.asList(child1, parent1, child2, parent2));

        UnionFindHelper<Object> S = new UnionFindHelper<>(elements);

        S.union(parent1, child1);
        S.union(parent2, child2);
        assertNotSame(S.find(child1), S.find(child2));

        S.union(child1, child2);

        assertSame(parent1, S.find(child2));
        assertSame(parent1, S.find(parent2));
        assertEquals(Integer.valueOf(2), S.getRanks().get(parent1));
    }

    @Test
    public void testUnion_SwapParents() {

        Object child1 = new Object(), parent1 = new Object(), child2 = new Object(), parent2 = new Object();
        Set<Object> elements = new HashSet<>(Arrays.asList(child1, parent1, child2, parent2));

        UnionFindHelper<Object> S = new UnionFindHelper<>(elements);

        S.union(parent1, child1);
        S.union(parent2, child2);
        S.union(child1, child2);

        // try swapping parents
        S.union(parent2, parent1);

        // do not expect anything to change
        assertSame(parent1, S.find(parent1));
        assertSame(parent1, S.find(child2));
        assertSame(parent1, S.find(parent2));
        assertEquals(Integer.valueOf(2), S.getRanks().get(parent1));
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_NullElement() {
        Set<Object> elements = new HashSet<>(Arrays.asList(new Object(), null));
        new UnionFindHelper<>(elements);
    }

    @Test(expected = NullPointerException.class)
    public void testFind_NullElement() {
        UnionFindHelper<Object> S = new UnionFindHelper<>();
        S.find(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFind_NullParent() {
        UnionFindHelper<Object> S = new UnionFindHelper<>();
        S.find(new Object());
    }

    @Test
    public void testFind_NullParentAllowed() {
        UnionFindHelper<Object> S = new UnionFindHelper<>(true);
        Object parent = S.find(new Object());
        assertNull(parent);
    }

    @Test
    public void testContains_NullElement() {
        UnionFindHelper<Object> S = new UnionFindHelper<>();
        assertFalse(S.contains(null));
    }

    @Test(expected = NullPointerException.class)
    public void testAdd_NullElement() {
        UnionFindHelper<Object> S = new UnionFindHelper<>();
        S.add(null);
    }

    @Test(expected = NullPointerException.class)
    public void testUnion_NullElement() {
        Object object1 = new Object();
        UnionFindHelper<Object> S = new UnionFindHelper<>(Collections.singleton(object1));
        S.union(null, object1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnion_NullParent() {
        Object object1 = new Object(), object2 = new Object();
        UnionFindHelper<Object> S = new UnionFindHelper<>(Collections.singleton(object1));
        S.union(object1, object2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnion_NullParentAllowed() {
        Object object1 = new Object(), object2 = new Object();
        UnionFindHelper<Object> S = new UnionFindHelper<>(Collections.singleton(object1), true);
        S.union(object1, object2);
    }

    private static class UnionFindHelper<T> extends UnionFind<T> {

        UnionFindHelper() {
            this(Collections.emptySet(), false);
        }

        UnionFindHelper(boolean allowFindReturnNull) {
            this(Collections.emptySet(), allowFindReturnNull);
        }

        UnionFindHelper(Set<T> elements) {
            this(elements, false);
        }

        UnionFindHelper(Set<T> elements, boolean allowFindReturnNull) {
            super(Objects.requireNonNull(elements), allowFindReturnNull);
        }

        Map<T, T> getParents() {
            return parents;
        }

        Map<T, Integer> getRanks() {
            return ranks;
        }
    }
}
