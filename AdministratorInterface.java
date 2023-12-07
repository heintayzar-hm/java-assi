// ## Usage

// You have to run the project depending on the editor you use.

// - All the students will be written inside the students.txt file and you might need to give write access depending on your os.
// - You can see the view:

// - as we can see, you can add, edit, and view students.
// - We have predefined all the necessary validations like name length, and age. But you can also change them inside the class of the file, "ValidationUtils", you can see some variables and they are self-explanatory.
// - If your terminal supports ASCII, you will be able to see color depending on your type.


// ## Code Usage

// - We have four classes in this project

// - Student Model class, responsible for a model of a student, you can define static classes for getting a student information or finding a student
// - ValidationUtils class, responsible for type checking and user validation inputs, the static methods inside responsibility is type checking or validation, if fails, the methods will return static variable Error code.
// - StudentManagement class, responsible for file loading and saving data updating data, or viewing data that will link with the main class, AdministratorInterface. It will load the file when starting the application using static block and save the data after every data change like update, or add.
// - AdministratorInterface class, responsible for main user interface, in some ways middleware and route combination in MVC pattern. It will print the user console with available options, and routes. after the user chooses it will link to static methods inside the class. The methods are responsible for getting necessary data and validating them using validation class, a middleware, and linking to the correct method in StudentManagement, a controller.

// So you can think like this in the MVC pattern,
// AdministratorInterface class: middleware and routing
// StudentManagement class: controller
// Student Model class: model
// ValidationUtils class: a helper middleware class


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/*
 * Student class
 * name: String
 * id: int
 * age: int
 * grade: String
 *
 * static methods:
 * getStudent(id: int): Optional<Student>
 * printStudent(student: Student): void
 */
class Student {
    private String name;
    private int id;
    private int age;
    private String grade;

    public Student(String name, int id, int age, String grade) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getGrade() {
        return grade;
    }

    // static methods
    public static Optional<Student> getStudent(int id) {
        for (Student student : StudentManagement.students) {
            if (student.getId() == id) {
                return Optional.of(student);
            }
        }
        return Optional.empty();
    }

    public static void printStudent(Student student) {

        StudentManagement.printUserConsole("Student information:", "success");

        StudentManagement.printUserConsole("ID: " + student.getId() + " | Name: " + student.getName() +
                " | Age: " + student.getAge() + " | Grade: " + student.getGrade(), "success");
    }
}

/*
 * ValidationUtils class
 * static methods:
 * validateAge(input: String, args: int...): String
 * validateName(name: String, args: int...): String
 * validateGrade(grade: String, args: String...): String
 * validateExistId(input: String): String
 *
 * static variables:
 * ERROR_CODE: String (default: "error")
 * and other constants
 */
class ValidationUtils {
    private static final int MIN_AGE = 10;
    private static final int MAX_AGE = 100;
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 50;
    private static final String[] GRADES = { "A", "B", "C", "D", "E", "F" };
    static final String ERROR_CODE = "error";

    /*
     * Validate grade
     * params: input, args(minAge, maxAge)
     */
    public static String validateAge(String input, int... args) {
        int minAge = args.length > 0 ? args[0] : MIN_AGE;
        int maxAge = args.length > 1 ? args[1] : MAX_AGE;
        try {
            int age = Integer.parseInt(input);

            if (age < minAge || age > maxAge) {
                throw new IllegalArgumentException("Age must be between " + minAge + " and " + maxAge + ".");
            }

            return input;
        } catch (IllegalArgumentException e) {
            // throw new IllegalArgumentException("Invalid age. " + e.getMessage());
            printUserConsole("Invalid age. " + e.getMessage(), "error");
            return ERROR_CODE;
        }
    }

    public static String validateName(String name, int... args) {
        int minLength = args.length > 0 ? args[0] : MIN_NAME_LENGTH;
        int maxLength = args.length > 1 ? args[1] : MAX_NAME_LENGTH;

        try {
            if (name.length() < minLength || name.length() > maxLength) {
                throw new IllegalArgumentException("Name must be between " + minLength + " and " + maxLength + " characters.");
            }

            return name;
        } catch (IllegalArgumentException e) {
            // throw new IllegalArgumentException("Invalid name length. " + e.getMessage());
            printUserConsole("Invalid name length. " + e.getMessage(), "error");
            return ERROR_CODE;
        }
    }

    public static String validateGrade(String grade, String... args) {
        String[] grades = args.length > 0 ? args : GRADES;

        String errorMessage = "Grade must be one of " + String.join(", ", grades) + "or a number between 0 and 100.";

        // grade can either be A, B, C, D, E, F or a number between 0 and 100

        try {
        int gradeNumber = Integer.parseInt(grade);

        if (gradeNumber < 0 || gradeNumber > 100) {
            throw new IllegalArgumentException(errorMessage);
        }

        return grade;
    } catch (IllegalArgumentException e) {
        for (String validGrade : grades) {
            if (grade.equals(validGrade)) {
                return grade;
            }
        }

        printUserConsole("Invalid grade. " + e.getMessage(), "error");

        return ERROR_CODE;
    }

    }

    public static String validateExistId(String input) {
        try {
            int id = Integer.parseInt(input);

            // validate id by finding student with id from StudentManagement
            for (Student student : StudentManagement.students) {
                if (student.getId() == id) {
                    return input;
                }
            }

            throw new IllegalArgumentException("Student not found with ID: " + id);
        } catch (IllegalArgumentException e) {
            // throw new IllegalArgumentException("Invalid age. " + e.getMessage());
            printUserConsole("Invalid id. " + e.getMessage(), "error");
            return ERROR_CODE;
        }
    }

    private static void printUserConsole(String consoleMessage, String... type) {
        StudentManagement.printUserConsole(consoleMessage, type);
    }
}


/*
 * StudentManagement class
 * static variables:
 * students: List<Student>
 * totalStudents: int
 * STUDENTS_FILE_PATH: String
 *
 * static methods:
 * addStudent(student: Student): void
 * updateStudent(id: int, newName: String, newAge: int, newGrade: String): void
 * viewStudentDetails(): void
 * getTotalStudents(): int
 * printUserConsole(consoleMessage: String, type: String...): void
 */

class StudentManagement {
    static List<Student> students = new ArrayList<>();
    private static int totalStudents = 0;
    private static final String STUDENTS_FILE_PATH = "students.txt";

    // load students from file
    static {
        loadStudentsFromFile();
    }

    private static void loadStudentsFromFile() {
        try {
            File file = new File(STUDENTS_FILE_PATH);

            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] studentData = line.split(",");
                        if (studentData.length == 4) {
                            int id = Integer.parseInt(studentData[0]);
                            String name = studentData[1];
                            int age = Integer.parseInt(studentData[2]);
                            String grade = studentData[3];

                            Student student = new Student(name, id, age, grade);
                            students.add(student);
                            totalStudents++;
                        }
                    }
                } catch (IOException | NumberFormatException e) {
                    printUserConsole("Error loading students from file.", "error");
                }
            }
        } catch (Exception e) {
            printUserConsole("Error checking or creating students file.", "error");
        }
    }

    private static void saveStudentsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STUDENTS_FILE_PATH))) {
            for (Student student : students) {
                // save student id, name, age, grade with comma separated
                writer.write(
                        student.getId() + "," + student.getName() + "," + student.getAge() + "," + student.getGrade());
                writer.newLine();
            }
        } catch (IOException e) {
            printUserConsole("Error saving students to file.", "error");
        }
    }

    public static void addStudent(Student student) {
        students.add(student);
        totalStudents++;
        saveStudentsToFile();
        printUserConsole("Student added successfully.", "success");

        Student.printStudent(student);
    }

    public static void updateStudent(int id, String newName, int newAge, String newGrade) {
        for (Student student : students) {
            if (student.getId() == id) {
                student = new Student(newName, id, newAge, newGrade);

                // replace old student with new student
                students.set(id - 1, student);


                saveStudentsToFile();
                printUserConsole("Student information updated successfully.", "success");

                Student.printStudent(student);
                return;
            }
        }
        printUserConsole("Student not found with ID: " + id, "error");
    }

    public static void viewStudentDetails() {
        if (students.isEmpty()) {
            printUserConsole("No students available.", "error");
            return;
        }

        for (Student student : students) {
            printUserConsole("ID: " + student.getId() + " | Name: " + student.getName() +
                    " | Age: " + student.getAge() + " | Grade: " + student.getGrade());
        }
    }

    public static int getTotalStudents() {
        return totalStudents;
    }

    public static void printUserConsole(String consoleMessage, String... type) {
        String message;
        if (type.length > 0 && type[0].equals("error")) {
            message = "\u001B[31m" + consoleMessage + "\u001B[0m \n";
        } else if (type.length > 0 && type[0].equals("success")) {
            message = "\u001B[32m" + consoleMessage + "\u001B[0m \n";
        } else {
            message = "\n" + consoleMessage + "\n";
        }

        System.out.print(message);
    }
}

/*
 * AdministratorInterface class
 * static methods:
 * main(args: String[]): void
 * getInput(scanner: Scanner, message: String, args: String...): String
 * addStudentMenu(scanner: Scanner): void
 * updateStudentMenu(scanner: Scanner): void
 * printUserConsole(consoleMessage: String, type: String...): void
 */

class AdministratorInterface {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("\n1. Add student\n2. Update student information\n3. View student details\n4. View a student details\n5. Exit");
            System.out.print("Enter your choice: ");

            // works only for Windows and Linux(ctr + D) for exit
            // not thoroughly tested
            if (!scanner.hasNextLine()) {
                // No more input, indicating EOF (Ctrl + Z or Ctrl + D)
                printUserConsole("Exiting the program. Goodbye!", "success");
                System.exit(0);
            }



            String userChoice = scanner.nextLine();

            switch (userChoice) {
                case "1":
                    addStudentMenu(scanner);
                    break;
                case "2":
                    updateStudentMenu(scanner);
                    break;
                case "3":
                    StudentManagement.viewStudentDetails();
                    break;
                case "4":
                    viewAStudentDetails(scanner);
                    break;
                case "5":
                    printUserConsole("Exiting the program. Goodbye!", "success");
                    System.exit(0);
                    break;
                default:
                    printUserConsole("Invalid input. Please enter 1, 2, 3, or 4.", "error");
            }

        } while (true);
    }

    /*
     * Get input from user
     * params: scanner, message, args(name or age or grade, "allowEmpty" for empty input)
     * return: input
     */
    private static String getInput(Scanner scanner, String message, String... args) {
        String input;

        boolean allowEmpty = false;

        for (String arg : args) {
            if (arg.equals("allowEmpty")) {
                allowEmpty = true;
            }
        }


        do {
            System.out.print(message);
            input = scanner.nextLine();

            if (input.isEmpty() && allowEmpty) {
                return input;
            }

            if (args.length > 0 && args[0].equals("name")) {
                input = ValidationUtils.validateName(input);
            } else if (args.length > 0 && args[0].equals("age")) {
                input = ValidationUtils.validateAge(input);
            } else if (args.length > 0 && args[0].equals("grade")) {
                input = ValidationUtils.validateGrade(input);
            } else if (args.length > 0 && args[0].equals("id")) {
                input = ValidationUtils.validateExistId(input);
            }
        } while (input.equals(ValidationUtils.ERROR_CODE));

        return input;
    }


    /*
     * Add student menu
     * params: scanner
     */
    private static void addStudentMenu(Scanner scanner) {
        String name = getInput(scanner, "Enter student name: ", "name");
        int age = Integer.parseInt(getInput(scanner, "Enter student age: ", "age"));
        String grade = getInput(scanner, "Enter student grade: ", "grade");

        int id = StudentManagement.getTotalStudents() + 1;
        Student newStudent = new Student(name, id, age, grade);
        StudentManagement.addStudent(newStudent);
    }

    /*
     * Update student menu
     * params: scanner
     */
    private static void updateStudentMenu(Scanner scanner) {

        int id = Integer.parseInt(getInput(scanner, "Enter student ID: ", "id"));

        // find student with id from StudentManagement
        Student student = Student.getStudent(id).orElse(null);

        // not found
        if (student == null) {
            printUserConsole("Student not found with ID: " + id, "error");
            return;
        }

        // print student information
        Student.printStudent(student);

        String newName = getInput(scanner, "Enter new student name(empty for not updating) : ", "name", "allowEmpty");
        String age = (getInput(scanner, "Enter new student age(empty for not updating) : ", "age", "allowEmpty"));
        int newAge = age.isEmpty() ? 0 : Integer.parseInt(age);
        String newGrade = getInput(scanner, "Enter new student grade (empty for not updating) : ", "grade",
                "allowEmpty");

        if (newName.isEmpty() && newAge == 0 && newGrade.isEmpty()) {
            printUserConsole("No information updated.", "error");
            return;
        }

        // if user input is empty, use the old value
        if (newName.isEmpty()) {
            newName = student.getName();
        }

        // if user input is empty, use the old value
        if (newAge == 0) {
            newAge = student.getAge();
        }

        // if user input is empty, use the old value
        if (newGrade.isEmpty()) {
            newGrade = student.getGrade();
        }

        // update student information
        StudentManagement.updateStudent(id, newName, newAge, newGrade);
    }


    private static void viewAStudentDetails(Scanner scanner) {
        int id = Integer.parseInt(getInput(scanner, "Enter student ID: ", "id"));

        // find student with id from StudentManagement
        Student student = Student.getStudent(id).orElse(null);

        // not found
        if (student == null) {
            printUserConsole("Student not found with ID: " + id, "error");
            return;
        }

        // print student information
        Student.printStudent(student);
    }

    private static void printUserConsole(String consoleMessage, String... type) {
        StudentManagement.printUserConsole(consoleMessage, type);
    }
}
