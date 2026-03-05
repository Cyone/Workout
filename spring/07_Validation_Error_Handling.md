# Handling Validation and Errors Consistently

In professional Spring applications, you want to avoid `try-catch` blocks in every controller. Instead, you use global handling to ensure every error returns a consistent JSON structure.

## 1. Validation (The "Right" Way)

Use the **Bean Validation API (JSR-380)** standards.

1.  **Annotate your DTOs:**
    ```java
    public class UserDto {
        @NotNull
        private String username;
        
        @Email
        private String email;
        
        @Min(18)
        private int age;
    }
    ```

2.  **Enforce in Controller:**
    Use `@Valid` or `@Validated` before the argument.
    ```java
    @PostMapping("/users")
    public ResponseEntity<User> create(@Valid @RequestBody UserDto userDto) {
        // If we get here, the data is valid
    }
    ```

## 2. Global Error Handling (`@ControllerAdvice`)

When validation fails, Spring throws a `MethodArgumentNotValidException`. If you don't catch it, the user gets a generic 400 error.

Use `@RestControllerAdvice` to handle this globally.

### Step 1: Define a Standard Error Response
Create a POJO (Plain Old Java Object) to represent errors consistently.
```java
public record ApiError(
    LocalDateTime timestamp, 
    int status, 
    String error, 
    List<String> details
) {}
```
