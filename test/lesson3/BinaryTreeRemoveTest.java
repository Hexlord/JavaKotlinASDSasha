package lesson3;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BinaryTreeRemoveTest {
    @Test
    public void add() {
        BinaryTree<Integer> tree = new BinaryTree<>();
        Integer root = 10;
        tree.add(root);
        tree.add(5);
        Integer seven = 7;
        tree.add(seven);
        tree.add(10);
        assertEquals(3, tree.size());
        assertTrue(tree.contains(5));
        tree.add(3);
        tree.add(1);
        tree.add(3);
        tree.add(4);
        assertEquals(6, tree.size());
        assertFalse(tree.contains(8));
        tree.add(8);
        tree.add(15);
        tree.add(15);
        tree.add(20);
        assertEquals(9, tree.size());
        assertTrue(tree.contains(8));
        assertTrue(tree.checkInvariant());
        
        tree.remove(root);
        assertEquals(8, tree.size());
        assertFalse(tree.contains(10));
        assertTrue(tree.checkInvariant());
        
        tree.remove(seven);
        assertEquals(7, tree.size());
        assertFalse(tree.contains(7));
        assertTrue(tree.contains(8));
        assertTrue(tree.contains(3));
        assertTrue(tree.contains(4));
        assertTrue(tree.contains(1));
        assertTrue(tree.checkInvariant());
        
        List<Object> list = new ArrayList<>();
        list.add(3);
        list.add(15);
        tree.remove(list);
        assertEquals(5, tree.size());
        assertTrue(tree.contains(20));
        assertFalse(tree.contains(15));
        assertFalse(tree.contains(3));
        assertTrue(tree.contains(4));
        assertTrue(tree.contains(1));
        assertTrue(tree.checkInvariant());
    }
    
}