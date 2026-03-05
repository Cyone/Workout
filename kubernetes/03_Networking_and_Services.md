# 3. Networking and Services in K8s

If Pods are constantly dying and being reborn with brand new IP addresses on completely different physical servers, how does an API Gateway know where to route REST traffic? The answer is the `Service` object.

## 1. The Service (Stable Networking)
A Service is an abstract way to expose an application running on a set of Pods as a single network service.
*   **Mechanic:** A Service gets a static, permanent IP address and a permanent DNS name inside the cluster.
*   **Routing (Label Selectors):** How does the Service know which Pods to send traffic to? Labels. If the Service is selecting `app: my-backend`, it constantly queries the API server for all healthy Pods holding that label. It then acts as an internal Load Balancer, distributing traffic round-robin to whatever Pods currently exist.

---

## 2. The Four Types of Services

### 1. ClusterIP (The Default - Internal Only)
*   **What it does:** Exposes the Service on an internal IP in the cluster.
*   **Visibility:** The service is **only reachable from within the cluster**. A user on the public internet cannot hit a ClusterIP service.
*   **Use Case:** Microservice-to-Microservice communication. The API Gateway calls `http://user-service:8080/`. The K8s internal DNS resolves `user-service` to the ClusterIP, which routes to a user-service Pod.

### 2. NodePort (Opening a Physical Port)
*   **What it does:** Exposes the Service on each physical Worker Node's IP at a static port (between 30000-32767).
*   **Visibility:** Reachable from the outside world by hitting `<NodeIP>:<NodePort>`.
*   **Use Case:** Legacy hardware or direct manual debugging. It is highly discouraged for production because you have to manage those random high-number ports, and if the physical Node IP changes, your DNS records break.

### 3. LoadBalancer (Cloud Integration)
*   **What it does:** Exposes the Service externally using the Cloud Provider's actual load balancer (e.g., AWS ALBs, GCP Load Balancers).
*   **Mechanic:** When you create this Service, K8s makes an API call to AWS saying "Spin up a physical Application Load Balancer and point it at my Nodes."
*   **Use Case:** Exposing your application directly to the internet.
*   **The Problem:** If you have 50 microservices and you make them all type `LoadBalancer`, AWS will spin up 50 physical ALBs for you. You will be billed thousands of dollars a month just for the Load Balancers themselves.

### 4. ExternalName (The Alias)
*   **What it does:** Maps a Service to a DNS name outside the cluster.
*   **Use Case:** Your Kubernetes app needs to talk to a managed database hosted outside K8s (e.g., AWS RDS). Instead of hardcoding the ugly AWS RDS URL in your Spring Boot `application.yml`, you create an `ExternalName` service called `db`. Your app connects to `http://db`, and K8s silently proxies it to the external RDS domain.

---

## 3. Ingress (The API Gateway / Router)

Because creating 50 Cloud `LoadBalancer` services is financially ruinous, Kubernetes introduced `Ingress`.

*   **What it is:** Ingress is *not* a Service type. It is a completely separate K8s object that manages external access to the services in a cluster, typically HTTP/HTTPS.
*   **Mechanic:** You deploy exactly ONE Cloud `LoadBalancer` Service. That LoadBalancer routes all internet traffic to the **Ingress Controller** (a specialized Pod running software like Nginx, Traefik, or HAProxy).
*   **The Ingress Rules:** You define Ingress rules that act like an API Gateway:
    *   Traffic to `api.myapp.com/users` -> routes to the internal `user-service` (ClusterIP).
    *   Traffic to `api.myapp.com/billing` -> routes to the internal `billing-service` (ClusterIP).
*   **Why it's essential:** It provides a single point of entry, SSL/HTTPS termination, and path-based routing, drastically cutting down cloud infrastructure costs.
