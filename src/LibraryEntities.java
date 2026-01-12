
/** 1. BOOK CLASS
 * This class simply holds the information for a single book.
 */
class Book {
    int bookId;
    String title;
    String author;
    boolean isAvailable;
    int borrowCount; // I need this counter to find the "Most Popular" book later.

    // I used my custom Queue here because the waitlist must be First-Come-First-Served.
    MyQueue<Integer> waitList;

    public Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
        this.borrowCount = 0;
        this.waitList = new MyQueue<>();
    }

    @Override
    public String toString() {
        // Formatting the output nicely for the console
        return String.format("[ID: %d] %s - %s | Status: %s | Borrows: %d",
                bookId, title, author, (isAvailable ? "Available" : "Checked Out"), borrowCount);
    }
}

/** 2. MEMBER CLASS
 * Stores member details and the books they currently have.
 */
class Member {
    int memberId;
    String name;

    // I chose Linked List here because members borrow/return books often.
    // Adding/removing from a list is easier than an array.
    MyLinkedList<Book> borrowedBooks;

    public Member(int memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.borrowedBooks = new MyLinkedList<>();
    }

    @Override
    public String toString() {
        return "[Member ID: " + memberId + "] " + name;
    }
}