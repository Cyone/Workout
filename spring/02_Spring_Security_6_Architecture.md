# Spring Security 6 Architecture (Boot 3)

Spring Security 6 (included in Spring Boot 3) removed many deprecated classes, forcing developers to adopt a modern, component-based configuration style.

## 1. The End of `WebSecurityConfigurerAdapter`
In Spring Boot 2, the standard way to configure security was to extend `WebSecurityConfigurerAdapter` and override the `configure(HttpSecurity http)` method.

In Spring Security 6, this class is entirely removed.

### The New Approach: Component-Based Configuration
Instead of extending a base class, you declare a `@Bean` of type `SecurityFilterChain`. You inject `HttpSecurity`, configure it via fluent builder methods, and call `.build()`.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // New Lambda DSL style
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/public").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
}
```

## 2. The Lambda DSL Requirement
Notice the `csrf(csrf -> csrf.disable())` syntax. Spring Security 6 mandates the use of the Lambda DSL for configuration.
*   **Why?** The old chained method style (`http.csrf().disable().authorizeRequests()...`) made it very difficult to understand where one configuration block ended and another began, often leading to misconfiguration. The Lambda DSL uses scoping to make the configuration hierarchy explicitly clear.
*   **Removal of `.antMatchers()`:** The old `.antMatchers()` method is removed. You must now use `.requestMatchers()`.

## 3. The Filter Chain Architecture
Interviewers often ask how Spring Security actually works under the hood. It is fundamentally a chain of Servlet Filters.

1.  **`DelegatingFilterProxy`:** This is a standard Servlet Filter registered with the web server (Tomcat). It doesn't do security itself; it delegates the request to a Spring Bean.
2.  **`FilterChainProxy`:** The Spring Bean that receives the request. It manages one or more `SecurityFilterChain`s.
3.  **The Filters:** The request passes through a sequence of specific filters:
    *   `SecurityContextPersistenceFilter` (or `SecurityContextHolderFilter` in Boot 3): Sets up the context.
    *   `UsernamePasswordAuthenticationFilter`: Handles form login.
    *   `BearerTokenAuthenticationFilter`: Extracts JWTs for OAuth2 resource servers.
    *   `ExceptionTranslationFilter`: Catches `AccessDeniedException` and translates it to HTTP 403, or `AuthenticationException` to HTTP 401.
    *   `AuthorizationFilter` (Replaced `FilterSecurityInterceptor` in Boot 3): The final filter that checks if the authenticated user has the correct roles/authorities to access the specific URL.

## 4. `SecurityContextHolder`
This is where Spring stores the details of who is currently authenticated (`Authentication` object).
*   **ThreadLocal:** By default, it uses a `ThreadLocal` to store the context. This means the authentication state is tied to the specific thread processing the HTTP request.
*   **Async Processing:** If you spawn a new thread (e.g., `@Async` or `CompletableFuture`) inside a controller, the new thread will *not* have the security context, and calls to `SecurityContextHolder.getContext().getAuthentication()` will return null unless you specifically configure `SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)`.
