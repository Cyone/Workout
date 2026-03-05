# 05_Pass_By_Value.md

## The Golden Rule
**Java is strictly Pass-By-Value.** There is no "Pass-By-Reference" in Java.

### How it works
When you pass a variable to a method, you are passing a **copy** of the bits inside that variable.

1.  **Primitives:**
    *   If you pass `int x = 10`, the method gets a copy of the value `10`. Changing it inside the method does not affect the original `x`.

2.  **Objects (The tricky part):**
    *   If you pass `Dog d`, the variable `d` holds the **address** (reference) of the object.
    *   The method gets a **copy of the address**.
    *   **Scenario A:** If the method uses the address to modify the object (e.g., `d.setName("Fido")`), the original object **is modified** (because both copies point to the same house).
        *   **Scenario B:** If the method reassigns the variable (e.g., `d = new Dog("Rex")`), it changes the **copy** of the address to point to a new dog. The original `d` outside the method still points to the old dog.
    
    ### 3. JIT Optimization: Escape Analysis
    While Java is strictly pass-by-value for references, the Just-In-Time (JIT) compiler uses **Escape Analysis** to see if an object allocated in a method "escapes" the method's scope.
    *   **Scalar Replacement:** If an object doesn't escape, the JVM might not create the object on the heap at all. It "deconstructs" the object and stores its fields directly in the stack frame or CPU registers. This makes the code run much faster by avoiding heap allocation and GC pressure.
    
    ### 4. The Future: Project Valhalla
    Currently, objects always have reference identity and header overhead. **Project Valhalla** aims to introduce "Value Objects" (or Primitive Objects). These will be true "Pass-By-Value" objects that behave like primitives (no identity, no header overhead), enabling high-performance layouts without the indirection of references.
    
    ### Interview Pro-Tip
    **Question:**
    ```java
    public void change(Point p) {
        p.x = 2;       // Line 1
        p = new Point(5, 5); // Line 2
    }
    ```
    **Answer:** "After calling `change(myPoint)`, the original `myPoint.x` will be 2. Line 1 modified the object that both the original and copy reference point to. Line 2 only changed the local copy of the reference to point elsewhere, leaving the original reference untouched."
    