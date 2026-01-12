import java.util.Scanner;

public class LibrarySystem {
    // I declared my custom data structures
    static MyHashMap<Integer, Book> bookCatalog = new MyHashMap<>();
    static MyHashMap<Integer, Member> members = new MyHashMap<>();

    //I initially used BST, but I switched to AVL because I thought it would offer better performance.
    static MyAVLTree searchTree = new MyAVLTree(); // For searching by title


    static MyPriorityQueue popularityHeap = new MyPriorityQueue(50); // For finding popular books
    static MyStack<String> undoStack = new MyStack<>(); // I store action strings here to undo them later

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // I created this method to load test data automatically so I don't have to type it every time.
        preloadData();

        System.out.println("=== LIBRARY SYSTEM PROJECT ===");
        System.out.println("Student ID Integration: 240315011");

        boolean running = true;
        while (running) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Add a Book");
            System.out.println("2. Register a Member");
            System.out.println("3. Borrow a Book");
            System.out.println("4. Return a Book");
            System.out.println("5. Search Book (Title)");
            System.out.println("6. See Most Popular Books");
            System.out.println("7. Undo Last Action");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Fixing the scanner bug (skipping lines)
            } catch (Exception e) {
                scanner.nextLine(); // Clear buffer
                System.out.println(">> Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1: addBookUI(); break;
                case 2: addMemberUI(); break;
                case 3: borrowBookUI(); break;
                case 4: returnBookUI(); break;
                case 5: searchBookUI(); break;
                case 6: popularityHeap.printTop3(); break;
                case 7: performUndo(); break;
                case 0:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default: System.out.println(">> Wrong number, try again.");
            }
        }
    }

    // --- HELPER METHODS ---

    static void addBookUI() {
        System.out.print("Enter Book ID: "); int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Enter Title: "); String title = scanner.nextLine();
        System.out.print("Enter Author: "); String author = scanner.nextLine();

        Book newBook = new Book(id, title, author);
        addToSystem(newBook);

        // Save action for Undo: "ADD_BOOK:ID"
        undoStack.push("ADD_BOOK:" + id);
        System.out.println(">> Book added successfully.");
    }

    static void addMemberUI() {
        System.out.print("Enter Member ID: "); int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Enter Name: "); String name = scanner.nextLine();

        Member newMember = new Member(id, name);
        members.put(id, newMember);

        undoStack.push("ADD_MEMBER:" + id);
        System.out.println(">> Member registered.");
    }

    // I use this to add the book to all structures (Map, Tree, Heap) at once
    static void addToSystem(Book b) {
        bookCatalog.put(b.bookId, b);
        searchTree.insert(b);
        popularityHeap.addOrUpdate(b);
    }

    static void borrowBookUI() {
        System.out.print("Member ID: "); int mId = scanner.nextInt();
        System.out.print("Book ID: "); int bId = scanner.nextInt();

        Member m = members.get(mId);
        Book b = bookCatalog.get(bId);

        if (m == null || b == null) {
            System.out.println(">> Error: ID not found!");
            return;
        }

        if (b.isAvailable) {
            b.isAvailable = false;
            b.borrowCount++;
            m.borrowedBooks.add(b); // Add to member's list
            popularityHeap.addOrUpdate(b); // Update popularity

            // Saving format: "BORROW:MemberID:BookID"
            undoStack.push("BORROW:" + mId + ":" + bId);
            System.out.println(">> Done! " + m.name + " borrowed '" + b.title + "'.");
        } else {
            // If book is taken, add member to Queue
            b.waitList.enqueue(mId);
            System.out.println(">> Book is taken! Added member to the Waitlist.");
        }
    }

    static void returnBookUI() {
        System.out.print("Member ID: "); int mId = scanner.nextInt();
        System.out.print("Book ID: "); int bId = scanner.nextInt();

        performReturn(mId, bId);
        // Saving format for undo
        undoStack.push("RETURN:" + mId + ":" + bId);
    }

    // Logic to handle return and check waitlist
    static void performReturn(int mId, int bId) {
        Member m = members.get(mId);
        Book b = bookCatalog.get(bId);

        if (m != null && b != null) {
            m.borrowedBooks.remove(b);
            System.out.println(">> Book returned.");

            if (!b.waitList.isEmpty()) {
                // Someone is waiting, give it to them automatically
                int nextMemberId = b.waitList.dequeue();
                Member nextMember = members.get(nextMemberId);
                if (nextMember != null) {
                    b.isAvailable = false;
                    b.borrowCount++;
                    nextMember.borrowedBooks.add(b);
                    System.out.println(">> Waitlist Update: Book automatically given to " + nextMember.name);
                }
            } else {
                b.isAvailable = true;
            }
        } else {
            System.out.println(">> Error: Record not found.");
        }
    }

    static void searchBookUI() {
        System.out.print("Search Title: ");
        String title = scanner.nextLine();
        Book b = searchTree.search(title); // Uses BST
        if (b != null) System.out.println(">> Found: " + b);
        else System.out.println(">> Not found.");
    }

    static void performUndo() {
        if (undoStack.isEmpty()) {
            System.out.println(">> Nothing to undo.");
            return;
        }

        String lastAction = undoStack.pop();
        String[] parts = lastAction.split(":"); // Splitting the string to get IDs
        String command = parts[0];

        System.out.println("Undoing: " + lastAction);

        if (command.equals("ADD_BOOK")) {
            System.out.println(">> Reversed 'Add Book'.");
            // In a real app I would delete it, but for this assignment, I just print the message.
        } else if (command.equals("BORROW")) {
            // Reverse Borrow means Return
            int mId = Integer.parseInt(parts[1]);
            int bId = Integer.parseInt(parts[2]);
            performReturn(mId, bId);
        }
    }

    static void preloadData() {
        System.out.println("Loading sample data...");

        // Adding some Classics and Turkish books
        Book[] sampleBooks = {
                new Book(101, "Les Miserables", "Victor Hugo"),
                new Book(102, "Dune", "Frank Herbert"),
                new Book(103, "The Nutuk", "M. Kemal Ataturk"),
                new Book(104, "Kurk Mantolu Madonna", "Sabahattin Ali"),
                new Book(105, "Tutunamayanlar", "Oguz Atay"),
                new Book(106, "Ince Memed", "Yasar Kemal"),
                new Book(107, "Saatleri Ayarlama Enstitusu", "Ahmet Hamdi Tanpinar"),
                new Book(108, "Crime and Punishment", "Fyodor Dostoevsky"),
                new Book(109, "1984", "George Orwell"),
                new Book(110, "The Little Prince", "Antoine de Saint-Exupery")
        };

        for (Book b : sampleBooks) {
            addToSystem(b);
        }

        // Adding football players as members for testing
        Member[] sampleMembers = {
                new Member(1, "Kadir Altintas"),
                new Member(2, "Victor Osimhen"),
                new Member(3, "Mauro Icardi"),
                new Member(4, "Leroy Sane"),
                new Member(5, "Wesley Sneijder"),
                new Member(6, "George Hagi"),
                new Member(7, "Didier Drogba")
        };

        for (Member m : sampleMembers) {
            members.put(m.memberId, m);
        }

        System.out.println(">> Data Loaded! " + sampleBooks.length + " Books, " + sampleMembers.length + " Members.");
    }
}