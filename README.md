# Discrete-Event Ski Resort Simulation

## Overview
A modular, object-oriented discrete-event simulation written in Java that models real-time skier traffic, lift queues, and route conditions across a complex directed graph network of ski lifts and slopes.

## Key Features & Architecture
- **Object-Oriented Design (OOP):** Built using clean polymorphism, encapsulation, and SOLID principles to model autonomous agents (skiers with varying skill levels and preferences), infrastructure nodes (lifts, queues, gates), and edges (slopes).
- **Graph Routing with BFS:** Implements Breadth-First Search (BFS) over a directed graph to compute optimal shortest-path routing dynamically based on slope difficulty and current lift availability.
- **State-Transition Engine:** Manages discrete time-step updates, lift capacity constraints, queue bottlenecks, and dynamic route degradation.

## Tech Stack
- **Language:** Java
- **Paradigms:** Object-Oriented Programming (OOP), Graph Theory, Discrete-Event Simulation

## Build & Run
Compile and run from the `src` directory:
```bash
javac Main.java
java Main
```
