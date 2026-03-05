# 16_JVM_Flags_And_Tuning.md

## Essential JVM Flags Cheat Sheet

### 1. Heap Sizing
| Flag | Meaning | Example |
|------|---------|---------|
| `-Xms<size>` | Initial heap size | `-Xms512m` |
| `-Xmx<size>` | Maximum heap size | `-Xmx4g` |
| `-XX:MetaspaceSize=<size>` | Initial Metaspace size | `-XX:MetaspaceSize=128m` |
| `-XX:MaxMetaspaceSize=<size>` | Cap Metaspace growth | `-XX:MaxMetaspaceSize=512m` |
| `-Xss<size>` | Thread stack size | `-Xss512k` |

**Rule:** Start with `-Xms` = `-Xmx` in production to avoid heap resizing pauses.

### 2. Garbage Collector Selection
| Flag | Collector | When to use |
|------|-----------|-------------|
| `-XX:+UseSerialGC` | Serial | Single-core, tiny apps |
| `-XX:+UseParallelGC` | Parallel | High throughput, batch |
| `-XX:+UseG1GC` | G1 | Default (Java 9+). Balanced throughput + latency |
| `-XX:+UseZGC` | ZGC | Ultra-low latency (<10ms pauses), Java 15+ GA |
| `-XX:+UseShenandoahGC` | Shenandoah | Ultra-low latency (OpenJDK alternative) |

### 3. GC Tuning — G1 Specific
| Flag | Purpose |
|------|---------|
| `-XX:MaxGCPauseMillis=200` | Target max pause time (soft goal) |
| `-XX:G1HeapRegionSize=n` | Region size (1MB–32MB, power of 2) |
| `-XX:G1NewSizePercent=5` | Min % of heap for young gen |
| `-XX:G1MaxNewSizePercent=60` | Max % of heap for young gen |
| `-XX:InitiatingHeapOccupancyPercent=45` | IHOP: starts concurrent marking at 45% heap usage |

### 4. JIT Compilation Flags
| Flag | Purpose |
|------|---------|
| `-XX:+TieredCompilation` | Enable C1→C2 tiered JIT (on by default) |
| `-XX:CompileThreshold=10000` | Method invocation count before JIT compilation |
| `-XX:+PrintCompilation` | Log all JIT-compiled methods |
| `-Xcomp` | Force immediate compilation (slower startup, good benchmark control) |
| `-Xint` | Interpreter only (no JIT — for debugging) |

### 5. Diagnostics & Logging
| Flag | Purpose |
|------|---------|
| `-Xlog:gc*:file=gc.log:time,uptime` | Unified GC logging (Java 9+) |
| `-verbose:gc` | Basic GC logging (legacy) |
| `-XX:+HeapDumpOnOutOfMemoryError` | Auto-dump heap on OOM |
| `-XX:HeapDumpPath=/tmp/heap.hprof` | Specify dump path |
| `-XX:+PrintGCDateStamps` | Add timestamps to GC logs (pre-Java 9) |
| `-XX:+UseGCLogFileRotation` | Rotate GC log files |

### 6. Native Memory & Serialization
| Flag | Purpose |
|------|---------|
| `-XX:+UseCompressedOops` | Compress 64-bit pointers to 32-bit (enabled by default ≤32GB heap) |
| `-XX:MaxDirectMemorySize=1g` | Limit `ByteBuffer.allocateDirect()` off-heap |
| `-Djava.io.tmpdir=/fast-disk` | Redirect temp files |
| `-Dfile.encoding=UTF-8` | Force UTF-8 encoding |

### 7. Application-Level System Properties
```bash
# Template command for a backend service
java -server \
     -Xms2g -Xmx2g \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -XX:+HeapDumpOnOutOfMemoryError \
     -XX:HeapDumpPath=/var/log/app/heap.hprof \
     -Xlog:gc*:file=/var/log/app/gc.log:time,uptime \
     -jar myapp.jar
```

### 8. Analyzing Behavior Without Restarting
Use `jcmd` to query running JVM flags dynamically:
```bash
jcmd <pid> VM.flags            # All active flags
jcmd <pid> VM.system_properties # System properties
jcmd <pid> GC.heap_info        # Heap regions summary
jcmd <pid> Thread.print        # Thread dump (same as jstack)
jcmd <pid> JFR.start duration=60s filename=/tmp/flight.jfr
```

### Interview Pro-Tip
**Question:** "Your service is running slow after deployment. No code changed. What do you check?"
**Answer:**
1. `jcmd <pid> GC.heap_info` → Is the heap being over-committed? Old Gen full?
2. `jstat -gc <pid> 1000` → Is GC running very frequently (minor GC every few seconds)?
3. `jstack <pid>` → Are threads blocked/waiting on a lock?
4. Check the GC log for long STW pauses.
5. Compare the active JVM flags (`jcmd VM.flags`) to the expected configuration — a flag may have been lost in the deployment script.
