package lesson3;

import org.junit.Test;

import static org.junit.Assert.*;

public class BinaryTreeRemoveTest {
    @Test
    public void add() {
        BinaryTree<Integer> tree = new BinaryTree<>();
        tree.add(10);
        tree.add(5);
        tree.add(7);
        tree.add(10);
        assertEquals(3, tree.size());
        assertTrue(tree.contains(5));
        
        tree.add(3);
        tree.add(4);
        tree.add(1);
        tree.add(7);
        tree.add(9);
        tree.add(8);
        
        assertEquals(8, tree.size());
        
        tree.remove(7);
        
        assertEquals(5, tree.size());
        assertFalse(tree.contains(9)); // fall apart
        assertFalse(tree.contains(8));
    }
    
}