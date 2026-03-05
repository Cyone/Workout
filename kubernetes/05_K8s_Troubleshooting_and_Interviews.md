# 5. K8s Troubleshooting & Interview Guide

Interviews aggressively test your K8s debugging skills. If you deploy an application and the API returns 503 Service Unavailable, you must know exactly where to look.

## 1. The Probes (Liveness vs. Readiness vs. Startup)
Kubernetes needs to know exactly when your Spring Boot app is actually ready to receive traffic and when it is terminally broken.

### 1. The `livenessProbe`
*   **Question:** *"Is the application in a terminal, unrecoverable state?"* (e.g., deadlocked thread).
*   **Action:** If this probe fails repeatedly, K8s will brutally **kill** the Container and attempt to restart it.

### 2. The `readinessProbe`
*   **Question:** *"Is the application ready to handle live HTTP traffic *right now*?"*
*   **Action:** If your app takes 30 seconds to cache data, this probe fails for 30 seconds. K8s does *not* kill the pod. Instead, K8s **removes the Pod's IP address from all Services/LoadBalancers**. Traffic stops routing to it. When the probe finally succeeds, traffic resumes. If a DB goes down causing the probe to fail, you stop traffic gracefully instead of restarting the server infinitely.

### 3. The `startupProbe` (For Legacy/Slow Apps)
*   **Question:** *"Has the application initially booted yet?"*
*   **Action:** The JVM can take 60 seconds to boot on a slow server. If your `livenessProbe` checks every 5 seconds, K8s will constantly kill your app before it ever finishes booting. The `startupProbe` disables the liveness/readiness probes until it succeeds entirely.

---

## 2. Common Pod States & Debugging
The first command you run is `kubectl get pods`. The resulting `STATUS` column tells the whole story.

### 1. `Pending`
*   **Meaning:** The API Server accepted the Pod request, but the Scheduler cannot find a Node to run it on.
*   **Cause:** Insufficient CPU/Memory resources in the entire cluster. Or, restrictive `nodeSelector`/Taint rules holding it back.
*   **Fix:** Add more physical Nodes, or lower the Pod's CPU request.

### 2. `ImagePullBackOff` / `ErrImagePull`
*   **Meaning:** The `kubelet` on the Node cannot download the Docker image.
*   **Cause:** The image tag doesn't exist (`v2.1beta` instead of `v2.1-beta`), the repository name is misspelled, or the Node lacks the authentication credentials (`imagePullSecrets`) to access your private Docker registry.

### 3. `CrashLoopBackOff`
*   **Meaning:** The container successfully started, but the main Java process immediately crashed (exited with a non-zero code). K8s restarted it, it threw an exception again, crashed again, and K8s is now backing off on restarting it to save CPU.
*   **Cause:** A bug in your code. A missing environment variable. A failed database connection string on boot.
*   **Fix:** `kubectl logs <pod-name> --previous` to view the Java stack trace from the crash *before* the restart.

### 4. `Evicted`
*   **Meaning:** A node ran entirely out of Memory or Disk space. The `kubelet` panicked and brutally killed your Pod in order to save the physical machine from a kernel panic.
*   **Fix:** You failed to set proper Resource Limits. Ensure all Deployments define strict CPU/Memory `requests` and `limits`.

---

## 3. The Golden Interview Debugging Flow
When asked, *"Your pod is CrashLooping, walk me through the steps to fix it"*:

1.  **Describe:** `kubectl describe pod <pod-name>`. I'd check the "Events" section at the very bottom. This tells me if it's scheduling failures, memory limits (OOMKilled), or health probe failures.
2.  **Logs:** `kubectl logs <pod-name>` (or `kubectl logs <pod-name> -c <container-name>` if there are sidecars) to check the application's actual STDOUT logs for NullPointerExceptions or Spring Boot crash reports.
3.  **Shell:** If the pod is running but behaving weirdly, I'd exec into it: `kubectl exec -it <pod-name> -- /bin/bash`. From inside, I'd run `curl` to test if the database DNS is resolvable, checking `env` for missing properties.
