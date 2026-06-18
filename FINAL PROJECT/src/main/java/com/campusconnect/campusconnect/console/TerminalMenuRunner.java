package com.campusconnect.campusconnect.console;

import com.campusconnect.campusconnect.model.*;
import com.campusconnect.campusconnect.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
public class TerminalMenuRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TerminalMenuRunner.class);

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final BookRepository bookRepository;
    private final DepartmentRepository departmentRepository;
    private final LibraryCardRepository libraryCardRepository;

    @Value("${campusconnect.terminal.menu.enabled:true}")
    private boolean menuEnabled;

    public TerminalMenuRunner(StudentRepository studentRepository,
                              TeacherRepository teacherRepository,
                              BookRepository bookRepository,
                              DepartmentRepository departmentRepository,
                              LibraryCardRepository libraryCardRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.bookRepository = bookRepository;
        this.departmentRepository = departmentRepository;
        this.libraryCardRepository = libraryCardRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!menuEnabled) {
            log.info("Terminal menu interface is disabled.");
            return;
        }

        Thread menuThread = new Thread(this::showMenu);
        menuThread.setDaemon(true);
        menuThread.start();
        log.info("Terminal menu interface started in background daemon thread.");
    }

    private void showMenu() {
        Scanner scanner = new Scanner(System.in);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {}

        while (true) {
            System.out.println("\n==================================================");
            System.out.println("            CAMPUSCONNECT TERMINAL MENU           ");
            System.out.println("==================================================");
            System.out.println("1. View Counts of All Collections");
            System.out.println("2. Create a Department");
            System.out.println("3. Create a Student");
            System.out.println("4. Create a Teacher (with Office Desk)");
            System.out.println("5. Create & Link Library Card to a Student");
            System.out.println("6. List all Students in a Department");
            System.out.println("7. Run Relationship Demo (1-1, 1:N, N:M)");
            System.out.println("8. Exit Menu");
            System.out.println("==================================================");
            System.out.print("Enter your choice (1-8): ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        viewCounts();
                        break;
                    case "2":
                        createDepartment(scanner);
                        break;
                    case "3":
                        createStudent(scanner);
                        break;
                    case "4":
                        createTeacher(scanner);
                        break;
                    case "5":
                        linkLibraryCard(scanner);
                        break;
                    case "6":
                        listStudentsInDepartment(scanner);
                        break;
                    case "7":
                        runRelationshipsDemo();
                        break;
                    case "8":
                        System.out.println("Exiting terminal menu. Server remains running...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                }
            } catch (Exception e) {
                System.out.println("Error executing command: " + e.getMessage());
            }
        }
    }

    private void viewCounts() {
        System.out.println("\n--- Collection Counts ---");
        System.out.println("Departments: " + departmentRepository.count());
        System.out.println("Students: " + studentRepository.count());
        System.out.println("Teachers: " + teacherRepository.count());
        System.out.println("Books: " + bookRepository.count());
        System.out.println("Library Cards: " + libraryCardRepository.count());
    }

    private void createDepartment(Scanner scanner) {
        System.out.print("Enter Department Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Department Code: ");
        String code = scanner.nextLine();

        Department dept = Department.builder()
                .name(name)
                .code(code)
                .build();
        Department saved = departmentRepository.save(dept);
        System.out.println("Created Department: " + saved);
    }

    private void createStudent(Scanner scanner) {
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Student Email: ");
        String email = scanner.nextLine();

        System.out.println("Available Departments:");
        departmentRepository.findAll().forEach(d -> System.out.println("ID: " + d.getId() + " | Code: " + d.getCode() + " | Name: " + d.getName()));
        System.out.print("Enter Department ID (or press Enter to skip): ");
        String deptId = scanner.nextLine().trim();

        Department dept = null;
        if (!deptId.isEmpty()) {
            dept = departmentRepository.findById(deptId).orElse(null);
        }

        Student student = Student.builder()
                .name(name)
                .email(email)
                .department(dept)
                .borrowedBooks(new ArrayList<>())
                .build();
        Student saved = studentRepository.save(student);
        System.out.println("Created Student: " + saved);
    }

    private void createTeacher(Scanner scanner) {
        System.out.print("Enter Teacher Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Teacher Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Specialization: ");
        String specialization = scanner.nextLine();

        System.out.print("Enter Desk Number: ");
        String deskNum = scanner.nextLine();
        System.out.print("Enter Building Name: ");
        String building = scanner.nextLine();
        System.out.print("Enter Floor (integer): ");
        int floor = Integer.parseInt(scanner.nextLine().trim());

        OfficeDesk desk = OfficeDesk.builder()
                .deskNumber(deskNum)
                .building(building)
                .floor(floor)
                .build();

        System.out.println("Available Departments:");
        departmentRepository.findAll().forEach(d -> System.out.println("ID: " + d.getId() + " | Code: " + d.getCode() + " | Name: " + d.getName()));
        System.out.print("Enter Department ID (or press Enter to skip): ");
        String deptId = scanner.nextLine().trim();

        Department dept = null;
        if (!deptId.isEmpty()) {
            dept = departmentRepository.findById(deptId).orElse(null);
        }

        Teacher teacher = Teacher.builder()
                .name(name)
                .email(email)
                .specialization(specialization)
                .department(dept)
                .officeDesk(desk)
                .build();
        Teacher saved = teacherRepository.save(teacher);
        System.out.println("Created Teacher: " + saved);
    }

    private void linkLibraryCard(Scanner scanner) {
        System.out.println("Select Student:");
        studentRepository.findAll().forEach(s -> System.out.println("ID: " + s.getId() + " | Name: " + s.getName()));
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        String cardNum = "LC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        LibraryCard card = LibraryCard.builder()
                .cardNumber(cardNum)
                .issueDate(LocalDate.now())
                .active(true)
                .build();
        LibraryCard savedCard = libraryCardRepository.save(card);

        student.setLibraryCard(savedCard);
        studentRepository.save(student);

        System.out.println("Created Library Card: " + savedCard);
        System.out.println("Linked to Student: " + student.getName());
    }

    private void listStudentsInDepartment(Scanner scanner) {
        System.out.println("Available Departments:");
        departmentRepository.findAll().forEach(d -> System.out.println("ID: " + d.getId() + " | Name: " + d.getName()));
        System.out.print("Enter Department ID: ");
        String deptId = scanner.nextLine().trim();

        System.out.println("\n--- Students in Department ---");
        studentRepository.findByDepartmentId(deptId).forEach(s -> 
            System.out.println("Name: " + s.getName() + " | Email: " + s.getEmail())
        );
    }

    private void runRelationshipsDemo() {
        System.out.println("\n==================================================");
        System.out.println("          STARTING RELATIONSHIP MAPPING DEMO      ");
        System.out.println("==================================================");

        // 1. Clear Database
        System.out.println("\n[Step 1] Clearing existing data to ensure clean logs...");
        departmentRepository.deleteAll();
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
        bookRepository.deleteAll();
        libraryCardRepository.deleteAll();
        System.out.println("Database cleared.");

        try {
            // 2. Create Department
            System.out.println("\n[Step 2] Creating Department (1:N Parent)...");
            Department csDept = Department.builder()
                    .name("Computer Science and Engineering")
                    .code("CSE")
                    .build();
            csDept = departmentRepository.save(csDept);
            System.out.println("Created Department: " + csDept.getName() + " (ID: " + csDept.getId() + ")");
            printJson("Department JSON representation in MongoDB:", csDept);

            // 3. One-to-One: Staff (Teacher) ↔ OfficeDesk (EMBEDDED)
            System.out.println("\n[Step 3] Demonstrating 1:1 Embedded Relationship (Staff ↔ OfficeDesk)...");
            OfficeDesk desk = OfficeDesk.builder()
                    .deskNumber("D-401")
                    .building("Engineering Hall")
                    .floor(4)
                    .build();
            
            Teacher teacher = Teacher.builder()
                    .name("Dr. Alan Turing")
                    .email("turing@campus.edu")
                    .specialization("Cryptography")
                    .department(csDept) // 1:N link
                    .officeDesk(desk) // 1:1 Embedded
                    .build();
            
            teacher = teacherRepository.save(teacher);
            System.out.println("Created Teacher: " + teacher.getName() + " (ID: " + teacher.getId() + ")");
            printJson("Teacher JSON (Notice 'officeDesk' is EMBEDDED directly inside the teacher document, and 'department' is a DBRef reference):", teacher);

            // 4. One-to-One: Student ↔ LibraryCard (DBREF REFERENCE)
            System.out.println("\n[Step 4] Demonstrating 1:1 Referenced Relationship (Student ↔ LibraryCard)...");
            LibraryCard card = LibraryCard.builder()
                    .cardNumber("LC-987654321")
                    .issueDate(LocalDate.now())
                    .active(true)
                    .build();
            card = libraryCardRepository.save(card);
            System.out.println("Created Library Card: " + card.getCardNumber() + " (ID: " + card.getId() + ")");

            Student student = Student.builder()
                    .name("Ada Lovelace")
                    .email("ada@campus.edu")
                    .department(csDept) // 1:N link
                    .libraryCard(card) // 1:1 Referenced DBRef
                    .borrowedBooks(new ArrayList<>())
                    .build();
            student = studentRepository.save(student);
            System.out.println("Created Student: " + student.getName() + " (ID: " + student.getId() + ")");
            printJson("Student JSON (Notice 'libraryCard' is referenced via DBRef, and 'department' is also referenced via DBRef):", student);

            // 5. One-to-Many: Department ➔ Students & Staff
            System.out.println("\n[Step 5] Demonstrating 1:N Relationships (Department ➔ Students, Department ➔ Staff)...");
            System.out.println("Querying database for Students in Department ID: " + csDept.getId());
            List<Student> studentsInCs = studentRepository.findByDepartmentId(csDept.getId());
            System.out.println("Students found: " + studentsInCs.size());
            for (Student s : studentsInCs) {
                System.out.println(" - Student Name: " + s.getName() + " | Email: " + s.getEmail());
            }

            System.out.println("Querying database for Staff (Teachers) in Department ID: " + csDept.getId());
            List<Teacher> teachersInCs = teacherRepository.findByDepartmentId(csDept.getId());
            System.out.println("Staff found: " + teachersInCs.size());
            for (Teacher t : teachersInCs) {
                System.out.println(" - Teacher Name: " + t.getName() + " | Office: Building " + t.getOfficeDesk().getBuilding() + ", Room " + t.getOfficeDesk().getDeskNumber());
            }

            // 6. Many-to-Many: Books ↔ Departments
            System.out.println("\n[Step 6] Demonstrating N:M Relationship (Books ↔ Departments)...");
            Book book1 = Book.builder()
                    .title("Introduction to Algorithms")
                    .author("Cormen et al.")
                    .isbn("978-0262033848")
                    .departments(new ArrayList<>(Collections.singletonList(csDept)))
                    .build();
            book1 = bookRepository.save(book1);

            Book book2 = Book.builder()
                    .title("Clean Code")
                    .author("Robert C. Martin")
                    .isbn("978-0132350884")
                    .departments(new ArrayList<>(Collections.singletonList(csDept)))
                    .build();
            book2 = bookRepository.save(book2);

            System.out.println("Created Books: '" + book1.getTitle() + "' and '" + book2.getTitle() + "'");
            printJson("Book JSON representation (Notice 'departments' holds a list of referenced DBRefs):", book1);

            // 7. Many-to-Many: Students ↔ Books (Borrowing System)
            System.out.println("\n[Step 7] Demonstrating N:M Relationship (Students ↔ Books)...");
            System.out.println("Ada Lovelace is borrowing both books...");
            student.getBorrowedBooks().add(book1);
            student.getBorrowedBooks().add(book2);
            student = studentRepository.save(student);

            printJson("Student JSON after borrowing books (Notice 'borrowedBooks' array is populated with DBRefs):", student);

            System.out.println("\n==================================================");
            System.out.println("      DEMO COMPLETED SUCCESSFULLY!                ");
            System.out.println("==================================================");

        } catch (Exception e) {
            System.out.println("Error running demo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void printJson(String message, Object obj) {
        System.out.println("\n" + message);
        try {
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .enable(SerializationFeature.INDENT_OUTPUT);
            System.out.println(mapper.writeValueAsString(obj));
        } catch (Exception e) {
            System.out.println("Failed to render JSON: " + e.getMessage());
        }
    }
}
