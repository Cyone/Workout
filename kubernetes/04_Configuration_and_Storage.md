# 4. Configuration and Storage in Kubernetes

Application environments (Dev, Staging, Prod) differ. A container image must remain identical across all environments, meaning configuration must be injected remotely at runtime. Furthermore, K8s must manage persistent storage across ephemeral Pod lifetimes.

## 1. Injecting Configuration

### 1. ConfigMaps
*   **What it is:** A key-value store used to store non-confidential data (e.g., specific `SPRING_PROFILES_ACTIVE=prod` settings, or an entire `application.yml` file).
*   **Mechanic:** You create the ConfigMap object in K8s. When your Pod starts, K8s can inject the ConfigMap into the Pod in two ways:
    1.  **Environment Variables:** Inject the keys as OS-level env vars. (Fast, but if the ConfigMap changes, you must restart the Pod to see the changes).
    2.  **Mounted Volumes (Files):** Mount the ConfigMap as an actual file (e.g., `/app/config/application.yml`) inside the running container. (If the ConfigMap updates, K8s silently updates the file on disk without restarting the Pod. Spring Boot can auto-reload this file).

### 2. Secrets
*   **What it is:** Works mechanically identical to a ConfigMap, but intended to hold a small amount of sensitive data such as passwords, OAuth tokens, or SSH keys.
*   **The Crucial Interview Caveat:** **Kubernetes Secrets are NOT inherently secure.** By default, values are merely Base64 encoded, *not* encrypted. Anyone with access to read the `etcd` database or the K8s API can read them in plain text.
*   **Proper Solution:** In production, K8s Secrets are often replaced or augmented by external, dedicated secret managers like **HashiCorp Vault** or **AWS Secrets Manager**, which dynamically inject secrets into the Pod's memory at runtime using sidecars.

---

## 2. Persistent Storage (The PV / PVC Paradigm)

Because Pods are ephemeral, any file written to a local container disk (`/app/data.txt`) disappears forever when the Pod crashes. Storage in Kubernetes is handled by an abstraction layer to decouple the developers from the physical hardware.

### 1. Volumes
*   A directory containing data, accessible to the containers in a Pod.
*   **`emptyDir`:** The simplest volume. It is created when a Pod starts. It is shared between all containers in that Pod. **Crucially, it is deleted when the Pod is deleted.** It is only useful as a temporary scratchpad for sidecars to communicate.

### 2. The PersistentVolume (PV) - The Hardware
*   **What it is:** Represents a piece of actual, physical storage in the cluster (e.g., a 100GB AWS EBS Volume, an NFS share, a local hard drive on a Node).
*   **Management:** Usually provisioned by a Cluster Administrator or dynamically provisioned using a `StorageClass`. It has a life cycle independent of any individual Pod.

### 3. The PersistentVolumeClaim (PVC) - The Request
*   **What it is:** A request for storage by an application developer.
*   **The Paradigm:** A developer doesn't care *where* the storage comes from. They just write a YAML file saying, "My Postgres Database needs 50GB of ReadWriteOnce storage." This request is the PVC.
*   **The Binding:** The K8s Control Plane finds a physical PV that matches the criteria of the PVC (e.g., an available 100GB PV) and **binds** them together tightly. The Pod then mounts the PVC. If the Pod dies, the PVC and PV remain untouched, waiting for a new Pod to claim them.

### Storage Classes (Dynamic Provisioning)
Instead of an administrator manually clicking around AWS to create 50 EBS volumes and writing 50 PV YAML files to match them, K8s uses `StorageClasses`. When a developer asks for a PVC, K8s dynamically talks to AWS behind the scenes to buy and provision the physical EBS disk automatically.
