# 4. Kotlin Implementation Example: Register User Feature

This document provides a concrete Kotlin implementation of Robert C. Martin's Clean Architecture applied to a simple "Register User" feature, suitable for discussing Spring Boot separation of concerns in a Senior Backend interview.

## The Package Structure (Screaming Architecture)
Your top-level packages should not be `controllers/`, `services/`, `repositories/` (which screams "I am a Spring Web App!"). They should scream the business domain: `user/`, `order/`, `billing/`.

Within the `user/` domain, the structure follows the concentric circles:

```text
src/main/kotlin/com/workout/user/
├── domain/                      (Entities)
│   ├── User.kt
│   └── exception/UserValidationException.kt
├── application/                 (Use Cases)
│   ├── RegisterUserUseCase.kt
│   └── port/
│       ├── in/RegisterUserCommand.kt          (Input Boundary)
│       └── out/UserRepositoryPort.kt          (Output Boundary)
└── infrastructure/              (Adapters & Frameworks)
    ├── web/
    │   ├── UserController.kt                  (Primary/Driving Adapter)
    │   └── dto/RegisterUserRequest.kt
    └── persistence/
        ├── PostgresUserRepositoryAdapter.kt   (Secondary/Driven Adapter)
        └── entity/UserJpaEntity.kt            (Spring Data Entity)
```

---

## 1. Domain Layer (Entities)
Pure Kotlin. Zero framework dependencies. The core business rules.

```kotlin
// /domain/User.kt
package com.workout.user.domain

class User(
    val id: String?,
    val email: String,
    val isVerified: Boolean = false
) {
    init {
        // Enterprise Business Rule: Emails must contain an '@'
        require(email.contains("@")) { "Invalid email format" }
    }

    // Enterprise Business Rule: Verification process
    fun verify(): User {
        return User(id = this.id, email = this.email, isVerified = true)
    }
}
```

---

## 2. Application Layer (Use Cases & Ports)
Application-specific rules. It orchestrates the flow. Depends ONLY on the Domain.

### Output Port (Dependency Inversion for the DB)
```kotlin
// /application/port/out/UserRepositoryPort.kt
package com.workout.user.application.port.out

import com.workout.user.domain.User

interface UserRepositoryPort {
    fun save(user: User): User
    fun existsByEmail(email: String): Boolean
}
```

### The Use Case (The Interactor)
```kotlin
// /application/RegisterUserUseCase.kt
package com.workout.user.application

import com.workout.user.domain.User
import com.workout.user.application.port.out.UserRepositoryPort

// Notice: No @Service or Spring annotations here if we are strict!
class RegisterUserUseCase(
    private val userRepository: UserRepositoryPort
) {
    // Application Rule: Don't register duplicate emails
    fun execute(command: RegisterUserCommand): User {
        if (userRepository.existsByEmail(command.email)) {
            throw IllegalArgumentException("User already exists")
        }
        
        val newUser = User(id = null, email = command.email)
        return userRepository.save(newUser)
    }
}

// DTO acting as the Input boundary to the Use Case
data class RegisterUserCommand(val email: String)
```

---

## 3. Infrastructure Layer (Adapters & Frameworks)
The messy outer edge. This is the only place Spring, JPA, or web annotations exist.

### Primary Adapter (The Web Controller)
Takes HTTP requests, converts them to Commands, and calls the Use Case.

```kotlin
// /infrastructure/web/UserController.kt
package com.workout.user.infrastructure.web

import org.springframework.web.bind.annotation.*
import com.workout.user.application.RegisterUserUseCase
import com.workout.user.application.RegisterUserCommand

@RestController
@RequestMapping("/api/users")
class UserController(
    private val registerUserUseCase: RegisterUserUseCase
) {
    @PostMapping
    fun register(@RequestBody request: RegisterUserRequest): String {
        // Translate Driver format (JSON Request) to Application format (Command)
        val command = RegisterUserCommand(email = request.email)
        
        // Execute the boundary
        val savedUser = registerUserUseCase.execute(command)
        
        return savedUser.id!!
    }
}

data class RegisterUserRequest(val email: String)
```

### Secondary Adapter (The Database Adapter)
Implements the Use Case's Port, translating pure Domain Objects into framework-specific JPA Entities.

```kotlin
// /infrastructure/persistence/PostgresUserRepositoryAdapter.kt
package com.workout.user.infrastructure.persistence

import org.springframework.stereotype.Component
import com.workout.user.application.port.out.UserRepositoryPort
import com.workout.user.domain.User

@Component
class PostgresUserRepositoryAdapter(
    private val springDataRepo: SpringDataUserRepository
) : UserRepositoryPort {

    override fun save(user: User): User {
        // 1. Map Domain User -> Framework JpaEntity
        val entity = UserJpaEntity(id = user.id?.toLong(), email = user.email)
        
        // 2. Perform DB operation via Framework Dependency
        val savedEntity = springDataRepo.save(entity)
        
        // 3. Map Framework JpaEntity -> Domain User
        return User(id = savedEntity.id.toString(), email = savedEntity.email)
    }

    override fun existsByEmail(email: String): Boolean {
        return springDataRepo.existsByEmail(email)
    }
}

// The actual Spring Data Repository (Hidden from the Application core)
interface SpringDataUserRepository : org.springframework.data.jpa.repository.JpaRepository<UserJpaEntity, Long> {
    fun existsByEmail(email: String): Boolean
}
```

## Conclusion for Interviews
By demonstrating this structure to an interviewer, you prove:
1.  **Framework Agnostic Core:** You can swap Spring Web for gRPC, or PostgreSQL for MongoDB, without touching a single line of code in the `domain` or `application` layers.
2.  **Testability:** You can unit test the `RegisterUserUseCase` in milliseconds by passing a simple InMemory mock of the `UserRepositoryPort`, completely avoiding the heavy Spring Context.
