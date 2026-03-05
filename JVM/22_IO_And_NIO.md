# 22_IO_And_NIO.md

## Java I/O API & NIO File System — OCP 1Z0-830 Deep Dive

---

### 1. Classic I/O vs NIO — When to Use Which

| | `java.io` | `java.nio.file` |
|---|---|---|
| Abstractions | `File`, `InputStream`, `OutputStream` | `Path`, `Files`, `FileSystem` |
| Style | Stream-based, synchronous, blocking | Method-oriented utility class |
| Preferred? | Legacy / simple use-cases | **Preferred in modern Java & on the exam** |

**Rule:** On the 1Z0-830 exam, `java.nio.file` (`Path` + `Files`) is the expected API. Know `File` only for its shortcomings.

---

### 2. `Path` — Representing a File Location

`Path` is an **interface** (not a class). You never construct it directly.

```java
// Three equivalent ways
Path p1 = Path.of("/home/user/data.txt");          // Java 11+ factory (preferred)
Path p2 = Paths.get("/home/user/data.txt");         // Legacy factory (pre-11)
Path p3 = FileSystems.getDefault().getPath("/home/user/data.txt");

// Combining paths
Path base = Path.of("/home/user");
Path full = base.resolve("docs/report.pdf");        // /home/user/docs/report.pdf
Path rel  = base.relativize(full);                  // docs/report.pdf
```

#### Key `Path` Methods (Exam Favourites)

```java
Path p = Path.of("/home/user/docs/report.pdf");

p.getFileName()      // report.pdf
p.getParent()        // /home/user/docs
p.getRoot()          // /
p.getNameCount()     // 4  (home, user, docs, report.pdf)
p.getName(0)         // home
p.subpath(1, 3)      // user/docs   (from index 1, exclusive 3)
p.isAbsolute()       // true
p.normalize()        // removes . and ..
p.toAbsolutePath()   // makes relative paths absolute using CWD
```

#### `resolve()` vs `relativize()` — Exam Gotchas

```java
Path a = Path.of("/a/b");
Path b = Path.of("c/d");

a.resolve(b)          // /a/b/c/d  (appends b to a)
a.resolve(Path.of("/c/d"))  // /c/d  (absolute path REPLACES a entirely!)

a.relativize(Path.of("/a/b/c/d"))  // c/d
Path.of("/a/b/c/d").relativize(a)  // ../..
```

> **Tip:** `resolve()` with an absolute argument ignores the receiver — this is a classic trap question.

---

### 3. `Files` — The Utility Class for Everything

#### Read / Write

```java
// Read all content
String content = Files.readString(path);                    // Java 11+
List<String> lines = Files.readAllLines(path);              // all lines into List
byte[] bytes = Files.readAllBytes(path);

// Write content
Files.writeString(path, "hello");                           // Java 11+
Files.write(path, bytes);
Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.APPEND);

// Streaming lines (lazy — good for large files)
try (Stream<String> stream = Files.lines(path)) {
    stream.filter(s -> s.startsWith("ERROR")).forEach(System.out::println);
}
```

#### Copy / Move / Delete

```java
Files.copy(src, dst);                                       // fails if dst exists
Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
Files.copy(src, dst, StandardCopyOption.COPY_ATTRIBUTES);  // preserve metadata
Files.move(src, dst, StandardCopyOption.ATOMIC_MOVE);      // OS-level atomic move

Files.delete(path);           // throws NoSuchFileException if missing
Files.deleteIfExists(path);   // silent if missing — preferred
```

#### Create Directories / Files

```java
Files.createFile(path);                // creates file; throws if already exists
Files.createDirectory(path);           // one level only
Files.createDirectories(path);         // creates entire chain, like mkdir -p

Files.createTempFile("prefix", ".tmp");
Files.createTempDirectory("tmpdir");
```

#### Metadata & Existence Checks

```java
Files.exists(path)                     // true/false
Files.notExists(path)                  // NOT the inverse of exists! (symbolic links)
Files.isDirectory(path)
Files.isRegularFile(path)
Files.isReadable(path)
Files.isWritable(path)
Files.isHidden(path)                   // throws IOException
Files.size(path)                       // bytes
Files.getLastModifiedTime(path)        // FileTime object

// Bulk attributes
BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
attrs.creationTime();
attrs.isSymbolicLink();
```

---

### 4. Walking the File Tree

```java
// Simple walk (depth-first, lazily populated Stream)
try (Stream<Path> walk = Files.walk(startPath)) {
    walk.filter(Files::isRegularFile)
        .filter(p -> p.toString().endsWith(".java"))
        .forEach(System.out::println);
}

// Walk with max depth
Files.walk(startPath, 2);    // only 2 levels deep

// Find with built-in matcher
try (Stream<Path> found = Files.find(startPath, 10,
        (path, attrs) -> attrs.isRegularFile() && attrs.size() > 1024)) {
    found.forEach(System.out::println);
}

// FileVisitor pattern (fine-grained control)
Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        System.out.println("File: " + file);
        return FileVisitResult.CONTINUE;     // or SKIP_SIBLINGS, SKIP_SUBTREE, TERMINATE
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.println("Failed: " + file + " - " + exc.getMessage());
        return FileVisitResult.CONTINUE;
    }
});
```

---

### 5. Stream-Based I/O (`InputStream` / `OutputStream` / `Reader` / `Writer`)

```java
// Buffered reading (classic but still valid)
try (BufferedReader br = Files.newBufferedReader(path)) {
    String line;
    while ((line = br.readLine()) != null) { ... }
}

// Buffered writing
try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
    bw.write("New line");
    bw.newLine();
}

// InputStream → byte level
try (InputStream is = Files.newInputStream(path)) {
    byte[] buf = new byte[8192];
    int read;
    while ((read = is.read(buf)) != -1) { ... }
}
```

#### `StandardOpenOption` Values (Exam)

| Option | Behaviour |
|--------|-----------|
| `READ` | Open for reading (default for read operations) |
| `WRITE` | Open for writing |
| `CREATE` | Create if not exists; open if exists |
| `CREATE_NEW` | Create; **throw** if already exists |
| `APPEND` | Write to end of file |
| `TRUNCATE_EXISTING` | Truncate to zero length on open |
| `DELETE_ON_CLOSE` | Delete file when stream closes |

---

### 6. Serialization & Deserialization

```java
// Serialize
try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream("data.ser"))) {
    oos.writeObject(myObject);
}

// Deserialize — cast required
try (ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream("data.ser"))) {
    MyClass obj = (MyClass) ois.readObject();  // throws ClassNotFoundException
}
```

#### Exam Rules for Serialization

- Class must implement `java.io.Serializable` (marker interface — no methods).
- `static` and `transient` fields are **NOT** serialized.
- `serialVersionUID` — if not declared, JVM auto-generates one. Changing the class without updating this UID causes `InvalidClassException` on deserialization.
- If a superclass is **not** `Serializable`, its fields are lost; its no-arg constructor is called during deserialization instead.

```java
class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private transient String password;    // NOT saved
    private static int count;            // NOT saved
}
```

---

### 7. Console I/O

```java
// System.out / System.err — PrintStream
System.out.println("stdout");
System.err.println("stderr");

// Console — only available when attached to a real terminal (not IDE)
Console console = System.console();   // null in IDE!
if (console != null) {
    String user = console.readLine("Username: ");
    char[] pass = console.readPassword("Password: ");  // masked, returns char[]
    Arrays.fill(pass, ' ');                            // security: clear after use
}
```

---

### 8. Common Exam Traps

```java
// 1. Path.of() does NOT check if file exists — these never throw:
Path p = Path.of("/does/not/exist/at/all");
p.getFileName(); // OK — just string manipulation

// 2. Files.exists() vs Files.notExists() — NOT complementary for sym links:
// if the target of a sym link doesn't exist:
//   exists() → false, notExists() → false (unknown/inaccessible)

// 3. Files.delete() throws NoSuchFileException; deleteIfExists() does not

// 4. relativize() requires both paths to be same type (both absolute / both relative)
// Path.of("/a").relativize(Path.of("b"))  → IllegalArgumentException!

// 5. Files.copy() does NOT copy directories recursively — only the dir itself, not its contents

// 6. Stream returned by Files.lines() / Files.walk() holds an open file handle
//    — MUST be closed with try-with-resources!
```

---

### Interview / Exam Quick-Fire

| Question | Answer |
|----------|--------|
| `Path` is a class or interface? | **Interface** |
| How to read all lines lazily? | `Files.lines(path)` returns `Stream<String>` |
| How to copy preserving metadata? | `Files.copy(src, dst, StandardCopyOption.COPY_ATTRIBUTES)` |
| `getNameCount()` on `Path.of("/")` | Returns **0** |
| What happens if you `resolve()` an absolute path? | The absolute argument **replaces** the receiver |
| Is serialization transitive? | Yes — all fields must also be `Serializable` unless `transient` |
