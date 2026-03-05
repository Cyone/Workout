# 2. Component Principles (Cohesion & Coupling)

While SOLID principles guide us on how to build classes, the Component Principles guide us on how to group those classes into larger, deployable components (like JAR files, DLLs, or distinct modules in a multi-module Gradle project).

## A. Component Cohesion (Which classes belong together?)
These principles help decide which classes go into which component.

### 1. The Reuse/Release Equivalence Principle (REP)
*   **"The granule of reuse is the granule of release."**
*   **Meaning:** If you group classes into a component to be reused by other teams/services, that component must be tracked by a release system (version numbers). You cannot reuse code safely unless you know exactly what version you are depending on.

### 2. The Common Closure Principle (CCP)
*   **"Gather into components those classes that change for the same reasons and at the same times."**
*   **Meaning:** This is SRP applied at the component level. If a business requirement changes, you want that change to be isolated to a single component, rather than requiring you to modify and redeploy five different components.

### 3. The Common Reuse Principle (CRP)
*   **"Don't force users of a component to depend on things they don't need."**
*   **Meaning:** This is ISP applied at the component level. If class A depends on class B, and you put class B in a massive "Utils" component containing 100 other classes, class A now implicitly depends on all 100 classes.

*   *The Tension Triangle:* You cannot perfectly satisfy all three. REP and CCP tend to make components larger. CRP tends to make them smaller. Architecture is the art of balancing these forces based on the project's current lifecycle stage (early stages favor CCP to localized changes; mature libraries favor REP/CRP).

---

## B. Component Coupling (How components interact?)
These principles guide how the arrows between components should be drawn.

### 1. The Acyclic Dependencies Principle (ADP)
*   **"Allow no cycles in the component dependency graph."**
*   **The Problem:** If Component A depends on B, B depends on C, and C depends on A, you have a cycle. You can no longer build or test these components independently. They effectively become one massive, tangled component (the "Morning After Syndrome").
*   **The Solution:** Break the cycle using the **Dependency Inversion Principle (DIP)**. If B depends on A, extract the interface B needs into a new component, and have both A and B depend on that new component.

### 2. The Stable Dependencies Principle (SDP)
*   **"Depend in the direction of stability."**
*   **Meaning:** Some components are designed to be volatile and change often (like the UI or Web Controllers). Some are designed to be extremely stable (like core Business Rules).
*   *Rule:* A component that is hard to change (has many things depending on it) must not depend on a component that is easy to change. If your core Domain depends on your Spring Controllers, your Domain becomes volatile.

### 3. The Stable Abstractions Principle (SAP)
*   **"A component should be as abstract as it is stable."**
*   **Meaning:** 
    *   Highly stable components (Core Domain) should consist mostly of interfaces and abstract classes so they can be extended without modification (OCP).
    *   Highly unstable components (Database Adapters, UI) should consist mostly of concrete classes, as their internal details will change frequently.
