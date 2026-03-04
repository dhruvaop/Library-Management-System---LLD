# Library Management System

Hey! This is a console-based Library Management System I built in Java. It was part of a backend assignment where I had to apply SOLID principles, OOP, and a couple of design patterns. Everything runs in the terminal and the data lives in memory (no database needed to run it).

I tried my best to keep it clean and organized. Happy to get feedback on it.

---

## How to Run

Make sure you have Java 17 and Maven installed.

```bash
cd library-management-system
mvn clean package
java -jar target/library-management-system-1.0.0.jar
```

You can also just run it directly with Maven if you do not want to build the jar:

```bash
mvn compile exec:java -Dexec.mainClass="com.library.Main"
```

Once it runs, the `Main.java` file goes through a full demo — it creates branches, adds books, registers patrons, checks out and returns books, fires off a reservation alert, and shows recommendations. All output goes to the console and also to a `library.log` file.

---

## Project Structure

```
library-management-system/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/com/library/
        │   ├── Main.java
        │   ├── model/
        │   │   ├── Book.java
        │   │   ├── BookStatus.java
        │   │   ├── Patron.java
        │   │   ├── Branch.java
        │   │   ├── LendingRecord.java
        │   │   ├── LendingStatus.java
        │   │   ├── Reservation.java
        │   │   └── ReservationStatus.java
        │   ├── repository/
        │   │   ├── BookRepository.java
        │   │   ├── PatronRepository.java
        │   │   ├── LendingRepository.java
        │   │   ├── ReservationRepository.java
        │   │   ├── BranchRepository.java
        │   │   └── impl/
        │   │       ├── InMemoryBookRepository.java
        │   │       ├── InMemoryPatronRepository.java
        │   │       ├── InMemoryLendingRepository.java
        │   │       ├── InMemoryReservationRepository.java
        │   │       └── InMemoryBranchRepository.java
        │   ├── service/
        │   │   ├── BookService.java
        │   │   ├── BookServiceImpl.java
        │   │   ├── PatronService.java
        │   │   ├── PatronServiceImpl.java
        │   │   ├── LendingService.java
        │   │   ├── LendingServiceImpl.java
        │   │   ├── ReservationService.java
        │   │   ├── ReservationServiceImpl.java
        │   │   ├── RecommendationService.java
        │   │   ├── RecommendationServiceImpl.java
        │   │   ├── BranchService.java
        │   │   └── BranchServiceImpl.java
        │   ├── notification/
        │   │   ├── BookReturnedEvent.java
        │   │   ├── BookReturnedListener.java
        │   │   ├── NotificationService.java
        │   │   └── ReservationAlertListener.java
        │   ├── recommendation/
        │   │   ├── RecommendationStrategy.java
        │   │   ├── BasedOnBorrowingHistory.java
        │   │   └── BasedOnPopularity.java
        │   └── util/
        │       └── IdGenerator.java
        └── resources/
            └── logback.xml
```

---

## What I Built

- **Book management** — you can add, remove, update, and search for books by title, author, or ISBN
- **Patron management** — register new patrons, update their info, and view their borrowing history
- **Checkout and return** — full lending flow with records stored in memory
- **Reservations** — patrons can reserve a book that is already checked out
- **Notifications** — when a book is returned, anyone who reserved it gets alerted on the console
- **Recommendations** — two ways to recommend books, one based on what you have read before and one based on what is popular overall
- **Multi-branch** — the system supports multiple library branches and you can transfer books between them

---

## SOLID Principles

Here is how I applied SOLID in this project:

**Single Responsibility**
I tried to make sure each class does only one thing. For example, `LendingServiceImpl` only handles checkout and return logic. `ReservationAlertListener` only deals with sending alerts when a book becomes available. `IdGenerator` just generates IDs.

**Open/Closed**
The services all depend on interfaces like `BookRepository` or `PatronRepository`. If I wanted to swap the in-memory storage for a real database later, I could just write a new implementation without touching any of the service classes.

**Liskov Substitution**
Any of the `InMemory*Repository` classes can be passed wherever the interface is expected and everything still works the same way.

**Interface Segregation**
Each interface is small and focused. `BookService` only has book operations. `PatronService` only has patron operations. None of them is bloated with methods that do not belong.

**Dependency Inversion**
All the service classes depend on interfaces, not on concrete implementations. In `Main.java` you can see how everything is wired up through constructors. The services do not create their own dependencies, they receive them from outside.

---

## Design Patterns

### Observer (used for notifications)

When a book gets returned, `LendingServiceImpl` fires a `BookReturnedEvent` through `NotificationService`. The `ReservationAlertListener` is listening and will print an alert for any patron who has a pending reservation on that book.

I named the classes based on what they actually do (`notification` package) rather than naming the folder after the pattern. The Observer pattern is still there, it just does not have "observer" in its name.

```
LendingServiceImpl
    └── notificationService.notifyBookReturned(event)
            └── ReservationAlertListener.onBookReturned(event)
                    └── prints alert for patron with pending reservation
```

### Strategy (used for recommendations)

The `RecommendationStrategy` interface has two implementations:
- `BasedOnBorrowingHistory` — recommends books by authors and genres the patron has already read
- `BasedOnPopularity` — recommends the most borrowed books in the system

`RecommendationServiceImpl` uses both and you can swap either one without changing any service code. I put these in a `recommendation` package since that is what they are about.

---

## Class Diagram

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                             com.library.model                                 │
│                                                                               │
│  ┌──────────────┐   ┌──────────────┐   ┌─────────────┐   ┌───────────────┐  │
│  │     Book     │   │    Patron    │   │   Branch    │   │ LendingRecord │  │
│  │────────────  │   │──────────── │   │─────────────│   │───────────────│  │
│  │ isbn         │   │ patronId    │   │ branchId    │   │ recordId      │  │
│  │ title        │   │ name        │   │ name        │   │ isbn          │  │
│  │ author       │   │ email       │   │ address     │   │ patronId      │  │
│  │ publicYear   │   │ phone       │   └─────────────┘   │ branchId      │  │
│  │ genre        │   │ history     │                      │ checkoutDate  │  │
│  │ status       │   └──────────── │   ┌─────────────┐   │ returnDate    │  │
│  └──────────────┘                 │   │ Reservation │   │ status        │  │
│                                   │   │─────────────│   └───────────────┘  │
│  BookStatus: AVAILABLE, BORROWED  │   │reservationId│                       │
│  LendingStatus: ACTIVE, RETURNED  │   │ isbn        │                       │
│  ReservationStatus: PENDING, ...  │   │ patronId    │                       │
│                                   │   │ branchId    │                       │
│                                   │   │ status      │                       │
│                                   │   └─────────────┘                       │
└──────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│                           com.library.repository                              │
│                                                                               │
│  <<interface>>      <<interface>>      <<interface>>      <<interface>>       │
│  BookRepository     PatronRepository   LendingRepository  ReservationRepo    │
│       ▲                   ▲                  ▲                  ▲             │
│       │                   │                  │                  │             │
│  InMemoryBook       InMemoryPatron      InMemoryLending   InMemoryReserv.    │
│  Repository         Repository          Repository         Repository         │
└──────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│                             com.library.service                               │
│                                                                               │
│  <<interface>>   <<interface>>   <<interface>>   <<interface>>                │
│  BookService     PatronService   LendingService  ReservationService           │
│       ▲               ▲               ▲                ▲                     │
│  BookServiceImpl PatronSvcImpl  LendingSvcImpl  ReservationSvcImpl            │
│                                                                               │
│  <<interface>>              <<interface>>                                     │
│  RecommendationService      BranchService                                    │
│       ▲                          ▲                                            │
│  RecommendationSvcImpl      BranchServiceImpl                                │
└──────────────────────────────────────────────────────────────────────────────┘

┌────────────────────────────────────────────┐
│         com.library.notification            │
│                                             │
│  <<interface>>                              │
│  BookReturnedListener                       │
│         ▲                                   │
│  ReservationAlertListener                   │
│                                             │
│  NotificationService                        │
│   addListener(BookReturnedListener)         │
│   notifyBookReturned(BookReturnedEvent)     │
│         │                                   │
│         └──► listener.onBookReturned()      │
└────────────────────────────────────────────┘

┌────────────────────────────────────────────┐
│        com.library.recommendation           │
│                                             │
│  <<interface>>                              │
│  RecommendationStrategy                     │
│  recommend(patron, books) → List<Book>      │
│         ▲              ▲                    │
│  BasedOnBorrowing  BasedOnPopularity        │
│  History                                    │
└────────────────────────────────────────────┘
```

---

## OOP Concepts

**Encapsulation** — all fields in the model classes are private and accessed through getters and setters. When returning a list like borrowing history, I return a copy so nothing outside can mess with the internal state.

**Abstraction** — the repository and service layers are all interfaces. Code outside of those layers only ever deals with the interface, not the concrete class.

**Inheritance** — all the `Impl` classes implement their respective interfaces. Enums like `BookStatus` and `LendingStatus` are used to keep state values clear and type-safe.

**Polymorphism** — you can see this most clearly in the recommendation package. `RecommendationServiceImpl` holds a `RecommendationStrategy` reference and calls `recommend()` on it without caring which implementation it is.

---

## Tech Stack

- Java 17
- Maven
- SLF4J + Logback for logging
- Java Collections (HashMap, ArrayList, List, Map, Set, Optional)
- No frameworks, just plain Java
