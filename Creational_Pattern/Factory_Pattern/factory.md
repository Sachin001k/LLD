# Factory Pattern - Complete Guide

## What is the Factory Pattern?

The **Factory Pattern** is a creational design pattern that lets you create objects without specifying the exact class to instantiate in your client code. Instead of calling `new SomeClass()` directly everywhere, you delegate object creation to a dedicated factory method.

In simpler terms: It centralizes the "which concrete class do I create?" decision in one place, instead of scattering `if/else` + `new` logic across your codebase.

---

## Problem It Solves

### The Problem Without Factory Pattern

If the client class decides which concrete class to instantiate, it looks like this:

```java
class LogisticsService{
    public void send(String mode){
        if(mode == "air"){
            Logistics logistics = new Air();
            logistics.send();
        } else if(mode == "road"){
            Logistics logistics = new Road();
            logistics.send();
        }
    }
}
```

**Problems with this approach:**
- **Violates Open/Closed Principle**: Every time a new mode (e.g. `Train`, `Sea`) is added, you must go back and modify `LogisticsService` itself
- **Scattered creation logic**: If multiple classes need to create a `Logistics` object, this same `if/else` chain gets duplicated everywhere
- **Tight coupling**: `LogisticsService` is coupled to every concrete class (`Air`, `Road`, ...) instead of just the `Logistics` interface
- **Hard to maintain**: Growing the `if/else` chain makes the class messier over time

### Solution: Factory Pattern

Move the "which concrete class to create" decision into a separate `LogisticsFactory` class. The client (`LogisticsService`) then only asks the factory for a `Logistics` object and uses it — it never decides the concrete type itself.

---

## Code Structure & Explanation

### 1. Logistics Interface - The Product

```java
interface Logistics{
    void send();
}
```

**What it does:**
- Defines the common contract that every shipping mode must implement
- This is the **Product** interface in Factory Pattern terms

**Problem solved:** Lets client code depend on `Logistics` as an abstraction, not on any specific shipping mode class.

---

### 2. Concrete Products - Air, Road, Train

```java
class Air implements Logistics{
    @Override
    public void send() {
        System.out.println("Sending by air");   
    }
}

class Road implements Logistics{
    @Override
    public void send(){
        System.out.println("Road Logistics");
    }
}

class Train implements Logistics{
    @Override
    public void send(){
        System.out.println("Sending by traun");
    }
}
```

**What it does:**
- Each class implements `Logistics` with its own specific behavior for `send()`
- These are the **Concrete Products** — the actual objects that get created

**Problem solved:** Each shipping mode's logic is isolated in its own class (Single Responsibility) rather than being embedded in conditional branches inside the service class.

---

### 3. LogisticsFactory - The Factory

```java
class LogisticsFactory{
    public static Logistics getLogistics(String mode){
        if(mode.equals("Air")){
            return new Air();
        }
        else if(mode.equals("Road")){
            return new Road();
        }
        else{
            return new Train();
        }
        //new changes can be added here
    }   
}
```

**What it does:**
- **Centralizes object creation**: All the "which class to instantiate" logic lives in exactly one place
- **`static` method**: `getLogistics(...)` can be called without creating a `LogisticsFactory` instance — it's a pure creation utility, it holds no state of its own
- **Returns the interface type**: The method's return type is `Logistics`, not `Air`/`Road`/`Train` — callers only ever see the abstraction
- **Single point of extension**: Adding a new mode (e.g. `Sea`) means adding one more `else if` branch here, and nowhere else

**Problem solved:** Isolates the creation decision so it can change (or grow with new modes) without touching any client class that uses `Logistics` objects.

---

### 4. LogisticsService - The Client

```java
class LogisticsService{
    public void send(String mode){
       Logistics logistics = LogisticsFactory.getLogistics(mode);
       logistics.send();
    }
}
```

**What it does:**
- Asks the factory for a `Logistics` object based on `mode`
- Calls `send()` on whatever it gets back, without knowing or caring whether it's an `Air`, `Road`, or `Train` instance

**Problem solved:** `LogisticsService` no longer needs to change when a new shipping mode is introduced — it only ever talks to the `Logistics` interface and the factory. This satisfies the **Open/Closed Principle**: the class is closed for modification but the system is open for extension (via the factory).

---

### 5. Usage - Requesting a Product Through the Factory

```java
public class Main{
    public static void main(String[] args){
        System.out.println("Factory pattern ex");
        LogisticsService logisticsService = new LogisticsService();
        logisticsService.send("Road");
    }
}
```

**What happens step by step:**

1. `new LogisticsService()` → creates the client/service object
2. `logisticsService.send("Road")` → calls `LogisticsService.send("Road")`
3. Inside `send()`, `LogisticsFactory.getLogistics("Road")` is called
4. The factory checks `mode.equals("Road")` → true → returns `new Road()`
5. Back in `LogisticsService`, `logistics.send()` is called on the `Road` instance

**Output:**
```
Factory pattern ex
Road Logistics
```

**Problem solved:** The service picked the correct shipping behavior (`Road`) purely by passing a string — all the `new Road()` vs `new Air()` decision-making stayed inside the factory, completely hidden from `LogisticsService` and `main()`.

---

## Factory Pattern Roles (Mapped to This Code)

| Role | In This Code |
|------|---------------|
| **Product** (common interface) | `Logistics` |
| **Concrete Products** | `Air`, `Road`, `Train` |
| **Factory** (creation logic) | `LogisticsFactory` |
| **Client** (uses products via factory) | `LogisticsService` |

---

## Key Benefits of Factory Pattern

| Benefit | Example from this code |
|---------|-------------------------|
| **Open/Closed Principle** | Adding `Sea` logistics only requires editing `LogisticsFactory`, not `LogisticsService` |
| **Decouples client from concrete classes** | `LogisticsService` never references `Air`, `Road`, or `Train` by name |
| **Centralized creation logic** | One place (`LogisticsFactory.getLogistics`) controls how objects are built |
| **Easier testing/maintenance** | Changing how a `Logistics` object is constructed doesn't ripple through every caller |

---

## Real-World Applications

- **Notification systems**: factory decides whether to create an `SmsNotification`, `WhatsappNotification`, or `EmailNotification`
- **Payment gateways**: factory decides whether to create a `UpiPayment`, `CreditCardPayment`, or `BankTransferPayment`
- **Logging frameworks**: factory returns a `ConsoleLogger`, `FileLogger`, or `CloudLogger` based on configuration
- **Database drivers**: factory returns the right `Connection` implementation based on a config string (MySQL, Postgres, etc.)

---

## When to Use Factory Pattern

✅ **Use it when:**
- A class needs to create objects of a type decided at runtime (e.g. from a string, config, or user input)
- You find yourself writing the same `if/else` + `new` chain in multiple places
- You want client code to depend only on an interface, never on concrete implementations
- New variants of a product are expected to be added over time

❌ **Avoid it when:**
- There's only one concrete implementation and no sign that will change
- Object construction is trivial (a plain constructor call is clearer than a factory)
- You need to create **families** of related objects together — in that case, consider **Abstract Factory** instead
