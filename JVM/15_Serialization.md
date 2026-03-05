# 15_Serialization.md

## Persisting Object State

### 1. What Is Serialization?
**Serialization** converts an object's state (its fields) into a byte stream.
**Deserialization** reconstructs the object from that byte stream.
Used for: network transmission (RMI, messaging), caching (Redis), file persistence.

### 2. Basic `Serializable`
```java
import java.io.*;

public class User implements Serializable {
    private static final long serialVersionUID = 1L; // Version stamp
    private String name;
    private transient String password; // NOT serialized
}

// Serialize to file
try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("user.ser"))) {
    out.writeObject(user);
}

// Deserialize
try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("user.ser"))) {
    User user = (User) in.readObject();
}
```

### 3. `serialVersionUID` — The Version Contract
*   Acts as a **compatibility check**. If you deserialize an object whose `serialVersionUID` differs from the current class definition, you get `InvalidClassException`.
*   If you don't declare it, the JVM generates one from the class structure — any field/method change regenerates it and breaks old serialized data.
*   **Rule:** Always declare it explicitly in classes you intend to serialize.

### 4. `transient` Keyword
Fields marked `transient` are **excluded from serialization**:
*   Passwords, session tokens (security)
*   Calculated/derived fields (can be recomputed on deserialization)
*   Non-serializable dependencies (thread references, streams)

After deserialization, `transient` fields have their **default values** (`null`, `0`, `false`).

### 5. `Externalizable` — Full Manual Control
```java
public class User implements Externalizable {
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(name); // Manually write each field
    }
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.name = in.readUTF(); // Manually read each field
    }
}
```
*   **Faster** than `Serializable` (no reflection, compact custom format).
*   **Requires a public no-args constructor** (used during deserialization).
*   Full control — you decide what's in the byte stream.

### 6. Security Risks
Java serialization is a major attack vector:
*   **Deserialization Gadget Chains** — malicious byte streams can trigger arbitrary code execution. (See Apache Commons Collections / Log4Shell ecosystem.)
*   **Rules:**
    *   Never deserialize data from untrusted sources with native Java serialization.
    *   Use safer alternatives: **JSON** (Jackson/Gson), **Protobuf**, **Avro**, **Kryo**.
    *   If you must use `ObjectInputStream`, override `resolveClass()` to whitelist allowed classes.

### 7. `readObject` / `writeObject` Hooks
```java
private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject(); // Serialize normal fields
    out.writeInt(encryptedPassword); // Add custom encrypted data
}
private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject(); // Restore normal fields
    this.password = decrypt(in.readInt()); // Restore custom data
}
```

### Interview Pro-Tip
**Question:** "If a parent class does not implement `Serializable` but a child class does, what happens to the parent's fields?"
**Answer:** "The parent's fields are **not serialized**. On deserialization, the JVM calls the parent's **no-args constructor** to initialize the parent's state. If the parent has no accessible no-args constructor, deserialization throws an `InvalidClassException`. This is why mixing serializable and non-serializable in a hierarchy requires careful design."
