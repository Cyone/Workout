# TroubleshootingAndTools.md

## Essential Production Tools
A strong Java engineer must know how to diagnose production issues using standard CLI tools.

### 1. `jps` (Java Process Status)
*   **What it does:** Lists all running Java processes on the machine.
*   **Use Case:** `jps -l` quickly shows you the PID of your running app.
*   **Example Output:** `12345 com.myapp.Server`

### 2. `jstat` (Java Virtual Machine Statistics Monitoring Tool)
*   **What it does:** Provides real-time statistics on garbage collection and class loading.
*   **Use Case:** Identifying memory leaks or excessive GC activity.
*   **Example:** `jstat -gc <pid> 1000` (Prints GC stats every 1 second).

### 3. `jstack` (Stack Trace Utility)
*   **What it does:** Prints the stack traces of all active threads for a given PID.
*   **Use Case:** Diagnosing deadlocks or stuck threads.
*   **Look For:** "Blocked" or "Waiting on monitor entry".
*   **Tip:** `jstack -l <pid> > thread_dump.txt`

### 4. `jmap` (Memory Map)
*   **What it does:** Prints shared object memory maps or heap memory details.
*   **Use Case:** Generating a heap dump for offline analysis.
*   **Example:** `jmap -dump:format=b,file=heap_dump.hprof <pid>`
*   **Risk:** Running a full heap dump on a large, busy heap can pause the application for several seconds or even minutes ("Stop The World").

### 5. `jcmd` (The Swiss Army Knife)
*   **What it does:** A unified command-line tool that can do almost everything `jps`, `jstack`, `jmap`, etc., can do.
*   **Use Case:** Triggering GC, enabling JFR (Java Flight Recorder), or checking system properties.
*   **Example:** `jcmd <pid> GC.run`

### 6. Profiling & Monitoring (Advanced)
*   **Java Flight Recorder (JFR):** A low-overhead (typically <1%) profiling tool built into the JVM. It records events like GC pauses, method execution times, and lock contention.
*   **VisualVM / JConsole:** GUI tools to visualize heap usage, threads, and CPU consumption in real-time. Good for development, risky for production due to overhead.

### Interview Pro-Tip
**Question:** "How do you analyze a high CPU usage issue in production?"
**Answer:**
1.  Run `top -H -p <pid>` to find the specific thread ID consuming CPU.
2.  Convert the decimal thread ID to hex (e.g., `printf "%x
" <thread_id>`).
3.  Run `jstack <pid> | grep <hex_thread_id>` to pinpoint exactly what line of code that thread is executing right now.
