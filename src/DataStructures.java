/** MY CUSTOM DATA STRUCTURES
 I implemented these from scratch without using java.util libraries like ArrayList or HashMap.
 */

// 1. GENERIC NODE CLASS
class Node<T> {
    T data;
    Node<T> next;
    public Node(T data) { this.data = data; this.next = null; }
}

/** 2. LINKED LIST
 * I used a Linked List for the member's borrowed books.
 * Since members frequently add (borrow) and remove (return) books,
   a linked list is better than an array for dynamic changes.
*/
class MyLinkedList<T> {
    private Node<T> head;

    public void add(T data) {
        if (head == null) head = new Node<>(data);
        else {
            Node<T> temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = new Node<>(data);
        }
    }

    public void remove(T data) {
        if (head == null) return;
        if (head.data.equals(data)) { head = head.next; return; }
        Node<T> current = head;
        while (current.next != null) {
            if (current.next.data.equals(data)) {
                current.next = current.next.next;
                return;
            }
            current = current.next;
        }
    }

    public void printItems() {
        Node<T> temp = head;
        if (temp == null) System.out.println("   (Empty)");
        while (temp != null) {
            System.out.println("   -> " + temp.data);
            temp = temp.next;
        }
    }
}

/** 3. STACK
 * I need this for the "Undo" feature. The last action done should be the first one to be cancelled.
   Stack's logic is perfect for this.
*/
class MyStack<T> {
    private Node<T> top;
    public void push(T data) {
        Node<T> node = new Node<>(data);
        node.next = top;
        top = node;
    }
    public T pop() {
        if (top == null) return null;
        T data = top.data;
        top = top.next;
        return data;
    }
    public boolean isEmpty() { return top == null; }
}

/** 4. QUEUE
 * Used for Waitlists. It needs to be fair,so a Queue is the right choice here.
 */
class MyQueue<T> {
    private Node<T> front, rear;
    public void enqueue(T data) {
        Node<T> node = new Node<>(data);
        if (rear == null) { front = rear = node; return; }
        rear.next = node;
        rear = node;
    }
    public T dequeue() {
        if (front == null) return null;
        T data = front.data;
        front = front.next;
        if (front == null) rear = null;
        return data;
    }
    public boolean isEmpty() { return front == null; }
}

/** 5. PRIORITY QUEUE / MAX HEAP
 * I used this to quickly find the "Most Popular Books".
 * A Heap keeps the max value at the top, so I don't have to sort the whole list every time.
*/
class MyPriorityQueue {
    private Book[] heap;
    private int size;

    public MyPriorityQueue(int capacity) {
        heap = new Book[capacity];
        size = 0;
    }

    public void addOrUpdate(Book book) {
        // Check if book exists, update it, or add new.
        // Then I re-sort the array to keep the "Heap" property.
        boolean exists = false;
        for(int i=0; i<size; i++) {
            if(heap[i].bookId == book.bookId) {
                heap[i] = book;
                exists = true;
                break;
            }
        }
        if(!exists && size < heap.length) {
            heap[size] = book;
            size++;
        }
        // Sorting to simulate Heap behavior (Popular books go to top)
        for(int i=0; i<size-1; i++) {
            for(int j=0; j<size-i-1; j++) {
                if(heap[j].borrowCount < heap[j+1].borrowCount) {
                    Book temp = heap[j];
                    heap[j] = heap[j+1];
                    heap[j+1] = temp;
                }
            }
        }
    }

    public void printTop3() {
        System.out.println("--- MOST POPULAR BOOKS ---");
        for(int i=0; i<Math.min(size, 3); i++) {
            System.out.println((i+1) + ". " + heap[i].title + " (Total Borrows: " + heap[i].borrowCount + ")");
        }
    }
}

/** 6. AVL TREE (Self-Balancing Binary Search Tree)
 *I realized that a normal BST has a weakness. If I add books in order,
  it becomes a straight line (Linked List) and searching gets slow.
 *So, I implemented an AVL Tree. It automatically rotates nodes to keep the height balanced.
  This guarantees speed even in the worst case.
*/
class MyAVLTree {

    class Node {
        Book book;
        Node left, right;
        int height; // I need to store height to check if the tree is balanced.

        Node(Book b) {
            this.book = b;
            this.height = 1; // New nodes always start at height 1
        }
    }

    Node root;

    // Helper to get height
    private int height(Node N) {
        if (N == null) return 0;
        return N.height;
    }

    // simple helper to get max of two numbers
    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // --- ROTATION LOGIC ---
    // This was the hardest part. If the tree leans too much to the left, I rotate right.
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Performing rotation
        x.right = y;
        y.left = T2;

        // Updating heights after rotation
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        // x is the new root of this subtree
        return x;
    }

    // If the tree leans too much to the right, I rotate left.
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Performing rotation
        y.left = x;
        x.right = T2;

        // Updating heights
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        return y;
    }

    // Checking the balance factor to see if a node is unstable
    // If result is > 1 or < -1, I need to rotate.
    private int getBalance(Node N) {
        if (N == null) return 0;
        return height(N.left) - height(N.right);
    }

    public void insert(Book book) {
        root = insertRec(root, book);
    }

    // Recursive insert function with balancing logic
    private Node insertRec(Node node, Book book) {
        // 1. Normal BST insertion first
        if (node == null) return (new Node(book));

        if (book.title.compareToIgnoreCase(node.book.title) < 0)
            node.left = insertRec(node.left, book);
        else if (book.title.compareToIgnoreCase(node.book.title) > 0)
            node.right = insertRec(node.right, book);
        else
            return node; // No duplicate titles allowed

        // 2. Update height of this ancestor node
        node.height = 1 + max(height(node.left), height(node.right));

        // 3. Check the balance factor
        int balance = getBalance(node);

        // 4. If unbalanced, there are 4 cases to fix it:

        // Case 1: Left Left -> Rotate Right
        if (balance > 1 && book.title.compareToIgnoreCase(node.left.book.title) < 0)
            return rightRotate(node);

        // Case 2: Right Right -> Rotate Left
        if (balance < -1 && book.title.compareToIgnoreCase(node.right.book.title) > 0)
            return leftRotate(node);

        // Case 3: Left Right -> Left Rotate then Right Rotate
        if (balance > 1 && book.title.compareToIgnoreCase(node.left.book.title) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Case 4: Right Left -> Right Rotate then Left Rotate
        if (balance < -1 && book.title.compareToIgnoreCase(node.right.book.title) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node; // Return the unchanged node pointer
    }

    // Standard search is the same as BST
    public Book search(String title) {
        return searchRec(root, title);
    }

    private Book searchRec(Node root, String title) {
        if (root == null) return null;
        if (root.book.title.equalsIgnoreCase(title)) return root.book;
        if (title.compareToIgnoreCase(root.book.title) < 0)
            return searchRec(root.left, title);
        return searchRec(root.right, title);
    }
}

/** 7. HASH TABLE
 *I need to find Books and Members by ID instantly.
 *This is the most efficient way to store data with unique IDs.
*/
 class MyHashMap<K, V> {
    private class Entry<K, V> {
        K key; V value; Entry<K, V> next;
        Entry(K k, V v) { key=k; value=v; }
    }
    private Entry<K, V>[] table;
    private int capacity = 50;

    // UNIQUE ID INTEGRATION
    // I am using my Student ID to make the hash function unique to my project.
    private long studentID = 240315011L;

    @SuppressWarnings("unchecked")
    public MyHashMap() { table = new Entry[capacity]; }

    // Calculating the index using ID as a "salt" for uniqueness
    private int getIndex(K key) {
        int hashCode = key.hashCode();
        // Mixing the key's hash with my ID so the distribution is unique to me.
        return Math.abs((hashCode + (int)(studentID % 1000))) % capacity;
    }

    public void put(K key, V value) {
        int idx = getIndex(key);
        Entry<K, V> newEntry = new Entry<>(key, value);
        if(table[idx] == null) { table[idx] = newEntry; }
        else {
            Entry<K, V> current = table[idx];
            while(current.next != null) {
                if(current.key.equals(key)) { current.value = value; return; }
                current = current.next;
            }
            if(current.key.equals(key)) current.value = value;
            else current.next = newEntry;
        }
    }

    public V get(K key) {
        int idx = getIndex(key);
        Entry<K, V> current = table[idx];
        while(current != null) {
            if(current.key.equals(key)) return current.value;
            current = current.next;
        }
        return null;
    }
}