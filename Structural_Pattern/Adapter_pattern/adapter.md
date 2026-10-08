# Adapter Pattern - Complete Guide

## What is the Adapter Pattern?

The **Adapter Pattern** is a structural design pattern that lets objects with incompatible interfaces work together. It acts as a bridge/translator between a class your system expects and a class that already exists but has a different method signature.

Definition: structuring smaller, incompatible pieces in such a way that they become usable as part of a larger system — without modifying the existing (often third-party) code.

---

## Problem It Solves

Your system has a `paymentGateway` interface with a `pay(orderId, amount)` method that your checkout logic depends on.

A third-party library, `RazorpayApi`, already exists and does the same job (processing payment) — but its method has a **different signature**:

```java
class RazorpayApi{
    public void makePayment(String invoiceId, double amountInRupees){
        System.out.println("Paid ₹" +amountInRupees +" using Razorpay for invoiceId: "+invoiceId);
    }
}
```

**The problem:**
- `RazorpayApi.makePayment(...)` doesn't match `paymentGateway.pay(...)`
- You can't directly pass a `RazorpayApi` object wherever a `paymentGateway` is expected (e.g. into `checkOutService`)
- You usually **can't modify third-party code** to make it match your interface (it's an external library/API)
- Without an adapter, you'd be forced to write special-case logic in `checkOutService` to handle Razorpay differently from every other gateway — breaking the whole point of coding to an interface

**Solution:** Create an **Adapter class** that implements your expected interface (`paymentGateway`) and internally translates calls to the third-party API's actual method (`makePayment`).

---

## Code Structure & Explanation

### 1. paymentGateway Interface - The Target

```java
interface paymentGateway{
    void pay(String orderId, double amount);
}
```

**What it does:**
- Defines the interface that `checkOutService` (the client) expects every payment gateway to follow
- This is the **Target** interface in Adapter Pattern terms — the interface the client code is written against

**Problem solved:** Lets `checkOutService` depend on an abstraction (`paymentGateway`) instead of any concrete payment provider.

---

### 2. payUgateway - A Native Implementation

```java
class payUgateway implements paymentGateway{
    @Override
    public void pay(String orderId, double amount){
        System.out.println("Paid ₹" +amount +" using PayU for orderId: "+orderId);
    }
}
```

**What it does:**
- Implements `paymentGateway` directly because PayU's own method signature already matches what you need
- No adapter needed here — this is a normal, compatible implementation

**Problem solved:** Shows the "happy path" case, as a contrast to Razorpay's incompatible case below.

---

### 3. RazorpayApi - The Adaptee

```java
class RazorpayApi{
    public void makePayment(String invoiceId, double amountInRupees){
        System.out.println("Paid ₹" +amountInRupees +" using Razorpay for invoiceId: "+invoiceId);
    }
}
```

**What it does:**
- Represents the existing third-party class (the **Adaptee**) with incompatible method naming: `makePayment(invoiceId, amountInRupees)` instead of `pay(orderId, amount)`
- This class is treated as "can't be changed" — exactly like a real external SDK/API you install as a dependency

**Problem solved:** Nothing yet — this IS the problem. It represents the incompatible interface that needs adapting.

---

### 4. Razorpay - The Adapter

```java
class Razorpay implements paymentGateway{
    private RazorpayApi razorpayApi;
    
    public Razorpay(){
        this.razorpayApi = new RazorpayApi();
    }
    @Override
    public void pay(String orderId, double amount){
        razorpayApi.makePayment(orderId, amount);
        System.out.println("Paid ₹ " + amount + " using Razorpay for orderId: "+orderId);
    }
}
```

**What it does:**
- **Implements `paymentGateway`**: Makes `Razorpay` usable anywhere a `paymentGateway` is expected (this is the key trick — the adapter wears the "face" of the interface the client wants)
- **Holds a `RazorpayApi` instance**: `private RazorpayApi razorpayApi` — composition, not inheritance. The adapter wraps the incompatible object internally
- **Constructor creates the adaptee**: `new RazorpayApi()` — the adapter is responsible for owning/creating the object it's adapting
- **Translates the call**: Inside `pay(orderId, amount)`, it calls `razorpayApi.makePayment(orderId, amount)` — converting the expected method call into the actual method call the third-party class understands

**How it works:**
1. Client code calls `pay("12345", 1000.0)` on what it thinks is a `paymentGateway`
2. Internally, `Razorpay.pay()` forwards that call to `razorpayApi.makePayment("12345", 1000.0)`
3. The parameter names differ conceptually (`orderId`→`invoiceId`, `amount`→`amountInRupees`) but the adapter maps them positionally — this is exactly the translation job an adapter does

**Problem solved:** Makes the incompatible `RazorpayApi` usable as a `paymentGateway`, without changing a single line of `RazorpayApi` or `checkOutService`.

---

### 5. checkOutService - The Client

```java
class checkOutService{
    private paymentGateway paymentGateway;

    public checkOutService(paymentGateway paymentGateway){
        this.paymentGateway = paymentGateway;
    }

    public void checkout(String orderId, double totalAmount){
        paymentGateway.pay(orderId, totalAmount);
    }
}
```

**What it does:**
- **Depends only on the `paymentGateway` interface**, never on a concrete class like `Razorpay` or `payUgateway`
- Receives whichever gateway implementation it should use via constructor injection
- Calls `pay(...)` without caring whether it's talking to PayU directly or to Razorpay through an adapter

**Design principle at play:**
- This is the **Dependency Inversion Principle** — high-level code (`checkOutService`) depends on an abstraction (`paymentGateway`), not on low-level concrete implementations
- Because of this, `checkOutService` never needs to know an adapter even exists — it's completely transparent to the client

**Problem solved:** Keeps checkout logic decoupled from any specific payment provider, so new gateways (adapted or native) can be swapped in without touching `checkOutService`.

---

### 6. Usage - Plugging the Adapter In

```java
public static void main(String[] args){
    System.out.println("Adapter Pattern");

    paymentGateway payU = new payUgateway();
    System.out.println("Using PayU gateway");

    paymentGateway razorpay = new Razorpay();
    System.out.println("Using Razorpay gateway");

    checkOutService checkOutService = new checkOutService(razorpay);
    checkOutService.checkout("12345", 1000.0);
}
```

**What happens step by step:**

1. `new payUgateway()` → a `paymentGateway` reference backed directly by PayU (no adapter needed)
2. `new Razorpay()` → a `paymentGateway` reference, but secretly backed by the incompatible `RazorpayApi` underneath, through the adapter
3. `new checkOutService(razorpay)` → the checkout service is handed the Razorpay adapter, but only sees it as a plain `paymentGateway`
4. `checkOutService.checkout("12345", 1000.0)` → calls `paymentGateway.pay("12345", 1000.0)`, which is actually `Razorpay.pay(...)`, which internally calls `razorpayApi.makePayment("12345", 1000.0)`

**Output:**
```
Adapter Pattern
Using PayU gateway
Using Razorpay gateway
Paid ₹1000.0 using Razorpay for invoiceId: 12345
Paid ₹ 1000.0 using Razorpay for orderId: 12345
```

**Problem solved:** `checkOutService` successfully processes a payment through Razorpay's incompatible API, using the exact same code path it would use for PayU or any other gateway.

---

## Adapter Pattern Roles (Mapped to This Code)

| Role | In This Code |
|------|---------------|
| **Target** (expected interface) | `paymentGateway` |
| **Client** (uses the Target interface) | `checkOutService` |
| **Adaptee** (existing incompatible class) | `RazorpayApi` |
| **Adapter** (bridges Target and Adaptee) | `Razorpay` |

---

## Key Benefits of Adapter Pattern

| Benefit | Example from this code |
|---------|-------------------------|
| **Reuse existing/third-party code** | `RazorpayApi` is used as-is, with zero modification |
| **Decouples client from implementation details** | `checkOutService` never knows `Razorpay` wraps a differently-shaped API |
| **Open/Closed Principle** | Adding Razorpay support didn't require changing `paymentGateway`, `checkOutService`, or `RazorpayApi` |
| **Consistent interface across providers** | `payU` and `razorpay` can be used completely interchangeably by the client |

---

## Real-World Applications

- **Payment gateways**: exactly this example — wrapping Stripe/Razorpay/PayPal SDKs behind one common interface
- **Legacy system integration**: adapting an old system's API to a new system's expected interface
- **Third-party library integration**: wrapping a library whose method names/signatures don't match your app's conventions
- **Data format conversion**: adapting XML-based APIs to work with a JSON-based interface

---

## When to Use Adapter Pattern

✅ **Use it when:**
- You need to use an existing class, but its interface doesn't match what your code expects
- You can't (or shouldn't) modify the existing/third-party class's source code
- You want to integrate multiple providers (e.g. payment gateways) behind one common interface

❌ **Avoid it when:**
- You control both interfaces and can simply make them match directly
- Only one implementation will ever exist — an adapter adds indirection with no real benefit
- The mismatch is trivial enough that a 1-line wrapper method would do (sometimes you don't need a whole pattern)
