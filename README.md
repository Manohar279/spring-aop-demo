# Spring AOP Demo (Spring Boot + Maven)

A minimal example of Spring AOP. `BankService` holds plain business logic with no
logging code, and `LoggingAspect` adds logging and timing around every service
method from the outside.

## Requirements

- Java 17 or newer
- Maven 3.6+

## Run

```bash
mvn spring-boot:run
```

Or build a jar and run it:

```bash
mvn clean package
java -jar target/spring-aop-demo-1.0.0.jar
```

## Project layout

```
src/main/java/com/example/aopdemo/
├── AopDemoApplication.java    # main class + CommandLineRunner that calls the service
├── service/BankService.java   # target bean (deposit, getBalance, withdraw)
└── aspect/LoggingAspect.java  # the @Aspect with all five advice types
```

## Advice types used

| Annotation        | When it runs                                         |
|-------------------|------------------------------------------------------|
| `@Before`         | Before the method                                    |
| `@AfterReturning` | After a normal return (can read the return value)    |
| `@AfterThrowing`  | Only when the method throws                          |
| `@After`          | Always after the method, like `finally`              |
| `@Around`         | Wraps the method; you call `proceed()` yourself      |

The pointcut `execution(public * com.example.aopdemo.service.*.*(..))` selects every
public method of every class in the `service` package.

## Expected output (shortened)

```
===== 1. Normal method call =====
@Around (start) -> deposit
@Before         -> deposit args=[ACC-101, 500.0]
    [BankService] Deposited 500.0 into ACC-101
@AfterReturning -> deposit returned=null
@After          -> deposit finished
@Around (end)   -> deposit took 4284 us

===== 3. Method that throws an exception =====
@Around (start) -> withdraw
@Before         -> withdraw args=[ACC-101, 10000.0]
@AfterThrowing  -> withdraw threw IllegalArgumentException: Insufficient funds in ACC-101
@After          -> withdraw finished
@Around (end)   -> withdraw took 1182 us
Main caught: Insufficient funds in ACC-101
```

## Note: self-invocation

`withdraw()` calls `getBalance()` internally, but no advice is printed for that
inner call. Spring AOP works through a proxy, so only calls that come in from
outside the bean (through the proxy) are intercepted. Calls made with `this`
inside the same class skip the proxy.
