# Workout

A comprehensive repository of Software Engineering study materials, coding exercises, and architectural guides. This project serves as a "workout" for technical skills, covering everything from core Java/Kotlin fundamentals to complex System Design and Algorithmic patterns.

## 🚀 Project Overview

This repository is structured to support continuous learning across multiple domains:
- **Algorithms & Data Structures:** Implementation and patterns for common LeetCode problems and core DS.
- **System Design:** In-depth guides on scalability, caching, databases, and microservices.
- **Modern JVM Development:** Advanced topics in Java/Kotlin, Spring Boot 3, and Memory Management.
- **DevOps & Infrastructure:** Cloud-native concepts including AWS, Kubernetes, and Kafka.
- **Software Architecture:** Clean Architecture, SOLID principles, and design patterns.

## 📁 Project Structure

```text
.
├── application_architecture  # Monolith vs Microservices, patterns
├── AWS                       # Cloud infrastructure concepts
├── clean_architecture        # SOLID, component principles, layers
├── data_engineering          # Hadoop, Big Data concepts
├── data_structures           # Core DS implementations (Stacks, Queues, etc.)
├── databases                 # Relational, NoSQL, and Vector DBs
├── java_fundamentals         # Core Java learning path and advanced trivia
├── JVM                       # Memory management, GC, Classloaders, IO/NIO
├── kafka                     # Messaging and event-streaming patterns
├── kotlin                    # Language-specific features and idioms
├── kubernetes                # K8s objects, troubleshooting, and config
├── leet_code                 # Categorized algorithmic problems (01 to 16)
├── messaging                 # General messaging concepts
├── networking                # HTTP, TLS, DNS, API Design, Security (CORS/CSRF)
├── reactive_programming      # Asynchronous and event-driven concepts
├── security                  # Auth (JWT, OAuth2, OIDC), OWASP Top 10
├── spring                    # Spring Boot 3, Security 6, JPA, Cloud patterns
├── src/main/kotlin           # Practice code for algorithms and data structures
└── system_design             # Scalability, CDNs, CQRS, Rate Limiting
```

## 🛠️ Getting Started

### Prerequisites
- **JDK 25** (as specified in the project's toolchain)
- **Gradle** (included via wrapper)

### Building the Project
To compile the Kotlin source files and run the tests:
```bash
./gradlew build
```

### Running Tests
The project uses JUnit Platform for testing:
```bash
./gradlew test
```

## 📖 How to Use This Repo
1. **Theory:** Browse the `.md` files in each directory for deep dives into specific topics.
2. **Practice:** Check `src/main/kotlin` for implementations of common algorithmic problems.
3. **Reference:** Use the categorized folders (e.g., `networking`, `security`) as a quick reference for best practices and interview preparation.

---

*Keep training.*
