# 2. Core Workload Objects

Understanding how to map your application architecture (stateless APIs, stateful databases, background agents) onto the correct Kubernetes primitives is critical.

## 1. The Pod
*   **What it is:** The smallest, most basic deployable object in Kubernetes. You *never* deploy a container directly in K8s; you deploy a Pod. A Pod encapsulates one or more containers, storage resources, and a unique network IP.
*   **The Multi-Container Pod (Sidecar Pattern):** Usually, a Pod has 1 container (e.g., your Spring Boot app). However, you can add "sidecars". For example, a logging agent container running in the exact same Pod as your App container. They share the same `localhost` network and same volume mounts, allowing the logger to read the app's log files natively.
*   **Ephemeral Nature:** Pods are mortal. They are born, and they die. They are *never* resurrected. If a Node dies, the Pods on it die. K8s will create *brand new* Pods on new Nodes to replace them. **Never store permanent state inside a Pod.**

## 2. ReplicaSet (Do not deal with directly)
*   **What it is:** A controller that guarantees a specified number of identical Pods (replicas) are running at any given time.
*   **Usage:** You rarely create these manually. You create Deployments, which manage ReplicaSets for you.

## 3. Deployment (Stateless Applications)
*   **What it is:** The standard way to run stateless applications (like your Java REST APIs or front-end React apps).
*   **Mechanics:** A Deployment acts as a manager over a ReplicaSet. 
*   **The Superpower: Rolling Updates.** If you change the image version in a Deployment (v1 -> v2), the Deployment creates a *new* ReplicaSet for v2. It slowly scales up the v2 ReplicaSet while simultaneously scaling down the v1 ReplicaSet. This guarantees zero-downtime deployments. If v2 crashes immediately, you can simply "rollback" the Deployment to the v1 ReplicaSet.

## 4. StatefulSet (Databases / Message Brokers)
*   **What it is:** Used for applications that require **persistent identity** and **stable storage**. If you try to run PostgreSQL, Kafka, or Elasticsearch using a Deployment, you will destroy your data.
*   **Why Deployments fail here:** In a Deployment, Pod names are random hashing (`api-5fb79`, `api-8a2b1`) and they spin up/down randomly. Databases need stable identities to know who the "Master" node is and who the "Follower" nodes are.
*   **StatefulSet Guarantees:**
    1.  **Stable Network ID:** Pods get sticky, predictable names (`kafka-0`, `kafka-1`, `kafka-2`). If `kafka-1` crashes, K8s restarts it exclusively as `kafka-1`.
    2.  **Ordered Deployment:** `kafka-1` will not start until `kafka-0` is fully up and running.
    3.  **Stable Storage:** If `kafka-1` is deleted and recreated on a completely different Node, K8s ensures the *exact same physical hard drive* (PersistentVolumeClaim) is detached from the old Node and forcefully re-attached to the new Node, preserving the exact data state.

## 5. DaemonSet (Node-Level Agents)
*   **What it is:** Ensures that exactly *one* copy of a specific Pod runs on *every single Node* in the cluster.
*   **Use Case:** You don't want a Datadog monitor or a Fluentd log-aggregator fighting for resources or scaling randomly based on CPU. You simply want one monitor agent per physical server to observe the entire server. If a new Node joins the cluster, K8s automatically stamps a Datadog DaemonSet Pod onto it.

## 6. Job & CronJob (Batch Processing)
*   **Job:** Runs a Pod to perform a specific, finite task and then successfully terminate (e.g., running a database migration script during a CI/CD pipeline). Unlike a Deployment, if the container exits with a `0` (success), K8s does *not* restart it.
*   **CronJob:** A Job, but dictated by a cron schedule (e.g., "run this specific data backup Pod every night at 2 AM").
