import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

class Book {
    private int id;
    private String title;
    private String author;
    private int quantity;
    private int borrowedQuantity;

    public Book(int id, String title, String author, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.quantity = quantity;

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getBorrowedQuantity() {
        return borrowedQuantity;
    }

    public void borrowBook(int quantity) {
        this.quantity -= quantity;
        this.borrowedQuantity += quantity;
    }

    public void returnBook(int quantity) {
        this.quantity += quantity;
        this.borrowedQuantity -= quantity;
    }
}

class Library {
    private Map<String, Book> books;
    private static final String BOOKS_FILE_PATH = "books.txt";

    public Library() {
        this.books = new HashMap<>();
        loadBooksFromFile();
    }

    // save in file or database for now we will just store in file

    private void loadBooksFromFile() {
    try {
        File file = new File(BOOKS_FILE_PATH);

        // if file does not exist, create file
        if (!file.exists()) {
            boolean fileCreated = file.createNewFile();

            if (!fileCreated) {
                printUserConsole("Error creating books file.", "error");
                return;
            }
        }

        // read books from file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookData = line.split(",");
                if (bookData.length == 4) {
                    int id = Integer.parseInt(bookData[0]);
                    String title = bookData[1];
                    String author = bookData[2];
                    int quantity = Integer.parseInt(bookData[3]);

                    Book book = new Book(id, title, author, quantity);
                    books.put(title, book);
                }
            }
        } catch (IOException | NumberFormatException e) {
            printUserConsole("Error loading books from file.", "error");
        }
    } catch (IOException e) {
        printUserConsole("Error checking or creating books file.", "error");
    }
}


    // save in file or database for now we will just store in file
    private void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE_PATH))) {
            for (Book book : books.values()) {
                // save book id, title, author, quantity with comma separated
                writer.write(book.getId() + "," + book.getTitle() + "," + book.getAuthor() + "," + book.getQuantity());
                writer.newLine();
            }
        } catch (IOException e) {
            printUserConsole("Error saving books to file.", "error");
        }
    }


    public void addBook(Book book) {
        // check if book already exists
        if (books.containsKey(book.getTitle())) {
            // if book exists, add quantity
            Book existingBook = books.get(book.getTitle());
            existingBook.setQuantity(existingBook.getQuantity() + book.getQuantity());
            printUserConsole("Book added successfully.", "success");
        } else {
            // if book does not exist, add book
            Book newBook = new Book(books.size() + 1, book.getTitle(), book.getAuthor(), book.getQuantity());

            books.put(newBook.getTitle(), newBook);

            printUserConsole("Book added successfully.", "success");
        }

        saveBooksToFile();
    }

    public void removeBook(Book book) {
    }

    // borrow book meaning it will be removed from the library
    public void borrowBook(Book book) {

        // guard clause 1
        if (!books.containsKey(book.getTitle())) {
            printUserConsole("Book is not available.", "error");
            return;
        }

        Book existingBook = books.get(book.getTitle());

        // guard clause 2
        if (existingBook.getQuantity() == 0) {
            printUserConsole("Book is out of stock.", "error");
            return;
        }

        if (existingBook.getQuantity() < book.getQuantity()) {
            printUserConsole("Book quantity is not enough.", "error");
            return;
        }

        existingBook.borrowBook(book.getQuantity());
        saveBooksToFile();
        printUserConsole("Book borrowed successfully. Left: " + existingBook.getQuantity(), "success");


    }

    // return book meaning it will be added to the library
    public void returnBook(Book book) {
        // guard clause 1
        if (!books.containsKey(book.getTitle())) {
            printUserConsole("There is no such book in the library.", "error");
            return;
        }

        Book existingBook = books.get(book.getTitle());
        existingBook.returnBook(book.getQuantity());
        // we need to add status to available if we want to store on a database or file

        saveBooksToFile();
        printUserConsole("Book returned successfully. Total: " + existingBook.getQuantity(), "success");
    }

    public void printAvailableBooks() {
        if (books.isEmpty()) {
            printUserConsole("There are no books available.", "error");
            return;
        }

        for (Book book : books.values()) {
            printUserConsole("Title: " + book.getTitle() + " | Author: " + book.getAuthor() + " | Quantity: "
                    + book.getQuantity());
        }
    }

    // helper method to print to console
    public void printUserConsole(String consoleMessage, String... type) {
        String message;
        if (type.length > 0 && type[0].equals("error")) {
            message = "\u001B[31m" + consoleMessage + "\u001B[0m \n";
        } else if (type.length > 0 && type[0].equals("success")) {
            message = "\u001B[32m" + consoleMessage + "\u001B[0m \n";
        } else {
            message = "\n" + consoleMessage;
        }

        System.out.print(message);

    }

    // helper method to check if an array contains a string
    private boolean containsAttr(String[] attrs, String attr) {
        for (String a : attrs) {
            if (a.equals(attr)) {
                return true;
            }
        }
        return false;
    }

    // user input for book
    public Book getBook(Scanner scanner, String... skipAttrs) {
        Hashtable<String, Object> book = new Hashtable<>();
        try {
            // if book title is not in skipAttrs, get book title

            if (skipAttrs.length == 0 || !containsAttr(skipAttrs, "title")) {
                book.put("title", getBookTitle(scanner));
            }

            // if book author is not in skipAttrs, get book author
            if (skipAttrs.length == 0 || !containsAttr(skipAttrs, "author")) {
                book.put("author", getBookAuthor(scanner));
            }

            // if book quantity is not in skipAttrs, get book quantity
            if (skipAttrs.length == 0 || !containsAttr(skipAttrs, "quantity")) {
                book.put("quantity", getBookQuantity(scanner));
            }

        } catch (Exception e) {
            printUserConsole(e.getMessage(), "error");
        }

        // temp id, it is not stored in the database or file
        return new Book(0, (String) book.get("title"), (String) book.get("author"), (int) book.get("quantity"));
    }

    // user input for book title
    public String getBookTitle(Scanner scanner) {
        do {
            printUserConsole("Enter book title: ");
            String title = scanner.nextLine();
            if (title.isEmpty()) {
                printUserConsole("Book title cannot be empty.", "error");
                continue;
            }

            if (title.length() > 100) {
                printUserConsole("Book title cannot be more than 100 characters.", "error");
                continue;
            }

            if (title.length() < 2) {
                printUserConsole("Book title cannot be less than 2 characters.", "error");
                continue;
            }

            return title;
        } while (true);
    }

    // user input for book author
    public String getBookAuthor(Scanner scanner) {
        do {
            printUserConsole("Enter book author: ");
            String author = scanner.nextLine();
            if (author.isEmpty()) {
                printUserConsole("Book author cannot be empty.", "error");
                continue;
            }

            if (author.length() > 100) {
                printUserConsole("Book author cannot be more than 100 characters.", "error");
                continue;
            }

            if (author.length() < 2) {
                printUserConsole("Book author cannot be less than 2 characters.", "error");
                continue;
            }

            return author;
        } while (true);
    }

    // user input for book quantity
    public int getBookQuantity(Scanner scanner) {
        do {
            printUserConsole("Enter book quantity: ");
            String quantity = scanner.nextLine();
            try {
                int quantityInt = Integer.parseInt(quantity);
                if (quantityInt < 1) {
                    printUserConsole("Book quantity cannot be less than 1.", "error");
                    continue;
                }

                return quantityInt;
            } catch (NumberFormatException e) {
                printUserConsole("Book quantity must be a number.", "error");
            }
        } while (true);
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();

        Scanner scanner = new Scanner(System.in);
        printUserConsole("\nWelcome to the Library Management System! \n", "success");

        do {
            printUserConsole("\n1. Add book \n2. Borrow book \n3. Return book \n4. Print available books \n5. Exit \n");
            String userAnswer = scanner.nextLine();

            switch (userAnswer) {
                case "1":
                    library.addBook(library.getBook(scanner));
                    break;
                case "2":
                    library.borrowBook(library.getBook(scanner, "author"));
                    break;
                case "3":
                    library.returnBook(library.getBook(scanner, "author"));
                    break;
                case "4":
                    library.printAvailableBooks();
                    break;
                case "5":
                    printUserConsole("Thank you for using the Library Management System!", "success");
                    System.exit(0);
                    break;
                default:
                    printUserConsole("Invalid input. Please enter 1, 2, 3, 4, or 5.", "error");
            }

        } while (true);
    }

    public static void printUserConsole(String consoleMessage, String... type) {

        String message;
        if (type.length > 0 && type[0].equals("error")) {
            message = "\u001B[31m" + consoleMessage + "\u001B[0m \n";
        } else if (type.length > 0 && type[0].equals("success")) {
            message = "\u001B[32m" + consoleMessage + "\u001B[0m \n";
        } else {
            message = consoleMessage;
        }

        System.out.print(message);

    }
}
