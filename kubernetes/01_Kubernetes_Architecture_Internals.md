# 1. Kubernetes Architecture & Internals

Kubernetes (K8s) is an open-source container orchestration platform originally designed by Google (borg). To pass a Senior Backend interview, you must understand *how* it manages state, not just how to deploy a pod.

## 1. The Declarative Paradigm (The Reconciliation Loop)
The most fundamental concept in K8s is that it is **declarative**, not imperative.
*   **Imperative (Docker Compose / Bash):** "Start exactly 3 containers of my app, right now."
*   **Declarative (Kubernetes):** "The *Desired State* of the cluster should always have 3 replicas of my app running."
*   **The Control Loop:** Kubernetes runs continuous "Reconciliation Loops". It constantly compares the *Current State* of the cluster to the *Desired State*. If they don't match (e.g., a Node crashed entirely, bringing the Current State down to 2 replicas), K8s automatically takes action to reach the Desired State (by scheduling a new Pod on a healthy Node to reach 3).

---

## 2. The Control Plane (The Brain)
The Control Plane manages the worker nodes and the Pods in the cluster. In managed services (EKS on AWS, GKE on Google), the cloud provider manages the Control Plane for you.

It consists of 4 primary components:

### 1. `kube-apiserver` (The Front Door)
*   **Role:** The core component. It exposes the Kubernetes HTTP API. 
*   **Mechanic:** Every single action (from your `kubectl apply` commands to internal node communication) goes through the API Server. It validates requests, updates state in `etcd`, and triggers the other controllers. It is strictly stateless.

### 2. `etcd` (The Brain's Memory storing State)
*   **Role:** A highly available, consistent, distributed key-value store.
*   **Mechanic:** This is the *only* place Kubernetes stores cluster state. It holds the source of truth for the "Desired State" you declared. If you lose `etcd` data, you lose your entire cluster configuration. It uses the Raft consensus algorithm, requiring a majority quorum to write data (like Kafka/Zookeeper).

### 3. `kube-scheduler` (The Dispatcher)
*   **Role:** Watches for newly created Pods that have no assigned Node.
*   **Mechanic:** It evaluates the resource requirements of the Pod (e.g., "I need 2 CPUs and 4GB RAM") and scans the cluster for a Node with enough available capacity, satisfying any rules you defined (like Node Affinity or Taints/Tolerations). It assigns the Pod to that Node.

### 4. `kube-controller-manager` (The Enforcer)
*   **Role:** Runs the core controller processes (the Reconciliation Loops).
*   **Mechanic:** Examples include the *Node Controller* (noticing when a Node goes down) and the *ReplicaSet Controller* (noticing when your Desired State is 3 but Current State is 2, creating a new Pod specification and sending it to the API Server).

---

## 3. The Worker Nodes (The Brawn)
The physical or virtual machines where your application containers actually run.

### 1. `kubelet` (The Captain on the Ship)
*   **Role:** The primary agent running on *every* Node.
*   **Mechanic:** It constantly talks to the API Server. The API Server says, "I assigned these 3 Pods to your Node." The `kubelet` ensures the containers described in those Pods are actually running and healthy on its machine, reporting their status back to the API Server.

### 2. `kube-proxy` (The Network Postman)
*   **Role:** A network proxy running on each node implementing Kubernetes "Services".
*   **Mechanic:** It maintains network rules (using iptables or IPVS on Linux) allowing network communication to your Pods from network sessions inside or outside of your cluster. It handles the internal load balancing across Pod replicas.

### 3. Container Runtime
*   **Role:** The software responsible for actually pulling the image and running the container. (e.g., containerd, CRI-O. Note: Docker itself was deprecated as a direct runtime in K8s v1.20+ in favor of runtimes adhering strictly to the Container Runtime Interface (CRI)).
