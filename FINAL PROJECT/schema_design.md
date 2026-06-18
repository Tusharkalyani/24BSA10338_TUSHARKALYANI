# CampusConnect MongoDB Schema Design

This document details the MongoDB schema design for the **CampusConnect** backend system.

---

## 1. Database & Collections Overview

The database is named `campusconnect` and consists of 5 collections. One entity is stored as an embedded document.

| Collection Name | Java Entity Class | Storage Type | Primary Key | Description |
| :--- | :--- | :--- | :--- | :--- |
| `departments` | `Department` | Independent | `ObjectId` (`_id`) | List of academic departments. |
| `students` | `Student` | Independent | `ObjectId` (`_id`) | Student records referencing Departments, LibraryCards, and Books. |
| `teachers` | `Teacher` | Independent | `ObjectId` (`_id`) | Staff records referencing Departments and containing embedded OfficeDesks. |
| `library_cards` | `LibraryCard` | Independent | `ObjectId` (`_id`) | Individual student library cards. |
| `books` | `Book` | Independent | `ObjectId` (`_id`) | Library books referencing Departments. |
| (None - Nested) | `OfficeDesk` | Embedded | N/A | Sub-document stored nested inside the `teachers` collection. |

---

## 2. Document Models

### Department Document (`departments`)
```json
{
  "_id": {"$oid": "603f7e59b2d6a520d43c2c10"},
  "name": "Computer Science and Engineering",
  "code": "CSE",
  "_class": "com.campusconnect.campusconnect.model.Department"
}
```

### LibraryCard Document (`library_cards`)
```json
{
  "_id": {"$oid": "603f7e59b2d6a520d43c2c11"},
  "cardNumber": "LC-8B39F40A",
  "issueDate": "2026-06-17",
  "active": true,
  "_class": "com.campusconnect.campusconnect.model.LibraryCard"
}
```

### Student Document (`students`)
- **One-to-One**: `libraryCard` referenced via `$ref: "library_cards"`
- **One-to-Many**: `department` referenced via `$ref: "departments"`
- **Many-to-Many**: `borrowedBooks` referenced as an array of `$ref: "books"`
```json
{
  "_id": {"$oid": "603f7e59b2d6a520d43c2c12"},
  "name": "John Doe",
  "email": "johndoe@example.com",
  "department": {
    "$ref": "departments",
    "$id": {"$oid": "603f7e59b2d6a520d43c2c10"}
  },
  "libraryCard": {
    "$ref": "library_cards",
    "$id": {"$oid": "603f7e59b2d6a520d43c2c11"}
  },
  "borrowedBooks": [
    {
      "$ref": "books",
      "$id": {"$oid": "603f7e59b2d6a520d43c2c13"}
    }
  ],
  "_class": "com.campusconnect.campusconnect.model.Student"
}
```

### Teacher Document (`teachers`)
- **One-to-One**: `officeDesk` is embedded as a nested document.
- **One-to-Many**: `department` referenced via `$ref: "departments"`
```json
{
  "_id": {"$oid": "603f7e59b2d6a520d43c2c14"},
  "name": "Dr. Sarah Connor",
  "email": "sconnor@example.com",
  "specialization": "Artificial Intelligence",
  "department": {
    "$ref": "departments",
    "$id": {"$oid": "603f7e59b2d6a520d43c2c10"}
  },
  "officeDesk": {
    "deskNumber": "D-402",
    "building": "Engineering Hall",
    "floor": 4
  },
  "_class": "com.campusconnect.campusconnect.model.Teacher"
}
```

### Book Document (`books`)
- **Many-to-Many**: `departments` referenced as an array of `$ref: "departments"`
```json
{
  "_id": {"$oid": "603f7e59b2d6a520d43c2c13"},
  "title": "Introduction to Algorithms",
  "author": "Thomas H. Cormen",
  "isbn": "978-0262033848",
  "departments": [
    {
      "$ref": "departments",
      "$id": {"$oid": "603f7e59b2d6a520d43c2c10"}
    }
  ],
  "_class": "com.campusconnect.campusconnect.model.Book"
}
```

---

## 3. Relationship Rationale

1. **One-to-One**:
   - **Student ↔ LibraryCard**: Modelled using DBRef references. This represents standard decoupling of card operations from primary student entities, meaning card management systems can access cards directly.
   - **Staff ↔ OfficeDesk**: Modelled using Embedding. Desks are tightly bound to the teacher's profile and do not have an independent existence or need direct lookup queries outside the teacher context.
2. **One-to-Many**:
   - **Department ➔ Students / Department ➔ Staff**: Modelled via references on the child side (Student/Teacher has DBRef to Department). This prevents the Department document from growing unbounded since a department can have thousands of students. Query performance is optimized via indices on `department.$id`.
3. **Many-to-Many**:
   - **Students ↔ Books**: Managed using a DBRef array (`borrowedBooks`) in `Student` to represent the borrowing relationship. This makes it trivial to check a student's currently checked out books.
   - **Books ↔ Departments**: Documented with a DBRef array (`departments`) in `Book` to list all departments that use this book.
