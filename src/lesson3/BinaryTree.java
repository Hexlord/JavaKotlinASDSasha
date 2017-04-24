package lesson3;

import org.jetbrains.annotations.NotNull;
import org.omg.CORBA.IntHolder;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

// Attention: comparable supported but comparator is not
@SuppressWarnings("WeakerAccess")
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> {
    
    private static class Node<T> {
        final T value;
        
        Node<T> left = null;
        
        Node<T> right = null;
        
        Node(T value) {
            this.value = value;
        }
    }
    
    private Node<T> root = null;
    
    private int size = 0;
    
    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        } else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }
    
    boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }
    
    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        
        
        if (o instanceof List<?>) {
            boolean isRemoved = false;
            
            for (Object i : ((List<?>) o)) {
                if (remove(i)) {
                    isRemoved = true;
                }
            }
            
            return isRemoved;
        } else if (o instanceof Comparable) {
            
            return remove((T) o);
            
        }
        
        return false;
    }
    
    private boolean remove(T t) {
        if (root != null && t != null) {
            
            int comparison = t.compareTo(root.value);
            
            if (comparison == 0) { // removing root
                
                // special case of removing root itself involves direct root pointer issue ( can not
                // use removeChildNode method due to lack of root's parent )
                if (root.left == null) { // no root.left, then just place root.right instead of root
                    root = root.right;
                } else {
                    Node<T> right = root.right; // can be null, lower than any node in left branch
                    root = root.left; // not null
                    Node<T> lowest = findLowestInBranch(root); // lowest node in root.left
                    lowest.right = right; // connect to lowest node of bigger than base root branch
                    
                }
                size--; // always success
                return true;
            } else { // search for node parent and perform removeChildNode
                if (removeNodeSearch(root, t, comparison)) {
                    return true;
                }
            }
            
            
        }
        
        return false;
    }
    
    // always move to Node.right to get the lowest one
    private Node<T> findLowestInBranch(Node<T> start) {
        Node<T> r = start;
        while (r.right != null) {
            r = r.right;
        }
        return r;
    }
    
    // way = 1 => remove right
    // way = -1 => remove left
    private boolean removeChildNode(Node<T> parent, int way) {
        
        // look for comments inside remove(..) method
        if (way == 1) {
            if (parent.right.left == null) {
                parent.right = parent.right.right;
            } else {
                Node<T> right = parent.right.right;
                parent.right = parent.right.left;
                Node<T> lowest = findLowestInBranch(parent.right);
                lowest.right = right;
                
            }
            
            size--;
            return true;
        } else if (way == -1) {
            if (parent.left.left == null) {
                parent.left = parent.left.right;
            } else {
                Node<T> right = parent.left.right;
                parent.left = parent.left.left;
                Node<T> lowest = findLowestInBranch(parent.left);
                lowest.right = right;
                
            }
            
            size--;
            return true;
        }
        
        return false;
    }
    
    private boolean removeNodeSearch(Node<T> start, T value, int comparison) {
        
        if (comparison < 0) {
            
            if (start.left != null) {
                
                int comparisonLeft = value.compareTo(start.left.value);
                if (comparisonLeft == 0) {
                    return removeChildNode(start, -1);
                } else if (removeNodeSearch(start.left, value, comparisonLeft)) { // keep searching
                    return true;
                }
                
            }
        } else if (comparison > 0) {
            
            if (start.right != null) {
                
                int comparisonRight = value.compareTo(start.right.value);
                if (comparisonRight == 0) {
                    return removeChildNode(start, 1);
                } else if (removeNodeSearch(start.right, value, comparisonRight)) { // keep searching
                    return true;
                }
                
            }
        }
        
        return false;
    }
    
    // unused
    private int countChild(Node<T> start) {
        IntHolder counter = new IntHolder(0);
        
        if (start != null) {
            
            countChildIterate(start, counter);
            
        }
        
        return counter.value;
    }
    
    // unused
    private void countChildIterate(Node<T> start, IntHolder counter) {
        
        if (start.left != null) {
            counter.value++;
            countChildIterate(start.left, counter);
        }
        
        if (start.right != null) {
            counter.value++;
            countChildIterate(start.right, counter);
        }
        
        
    }
    
    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }
    
    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }
    
    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }
    
    public class BinaryTreeIterator implements Iterator<T> {
        
        private Node<T> current = null;
        
        private BinaryTreeIterator() {
        }
        
        private Node<T> findNext() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean hasNext() {
            return findNext() != null;
        }
        
        @Override
        public T next() {
            current = findNext();
            if (current == null) throw new NoSuchElementException();
            return current.value;
        }
    }
    
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }
    
    @Override
    public int size() {
        return size;
    }
}
