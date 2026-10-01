/*
 * PRACTICAL 08: TRANSACTIONAL MICROSERVICE
 *
 * Spring Boot + JPA + PostgreSQL
 *
 * This single Java source demonstrates the required entity model,
 * validation, repository-style operations, service logic, and REST
 * endpoints in one file.
 *
 * For a standard Spring Boot project, place this file in a project
 * containing Spring Web, Spring Data JPA, PostgreSQL Driver and
 * Validation dependencies.
 */

package com.bookflow.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Optional;

@SpringBootApplication
public class Practical_08_Transactional_Microservice {
    public static void main(String[] args) {
        SpringApplication.run(
            Practical_08_Transactional_Microservice.class, args
        );
    }
}

@Entity
@Table(name = "inventory")
class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long bookId;

    @Min(0)
    @Column(nullable = false)
    private int totalStock;

    @Min(0)
    @Column(nullable = false)
    private int availableStock;

    public Inventory() {}

    public Inventory(Long bookId, int totalStock, int availableStock) {
        this.bookId = bookId;
        this.totalStock = totalStock;
        this.availableStock = availableStock;
    }

    public Long getId() {
        return id;
    }

    public Long getBookId() {
        return bookId;
    }

    public int getTotalStock() {
        return totalStock;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }
}

@Entity
@Table(name = "orders")
class BookOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private String status;

    public BookOrder() {}

    public BookOrder(Long userId, Long bookId, String status) {
        this.userId = userId;
        this.bookId = bookId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getStatus() {
        return status;
    }
}

interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByBookId(Long bookId);
}

interface OrderRepository extends JpaRepository<BookOrder, Long> {
}

class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }
}

@RestController
@RequestMapping("/api")
class InventoryController {

    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;

    InventoryController(
        InventoryRepository inventoryRepository,
        OrderRepository orderRepository
    ) {
        this.inventoryRepository = inventoryRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/inventory/{bookId}")
    public ResponseEntity<?> getInventory(@PathVariable Long bookId) {
        return inventoryRepository.findByBookId(bookId)
            .map(ResponseEntity::ok)
            .orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book inventory not found")
            );
    }

    @PostMapping("/orders")
    @Transactional
    public ResponseEntity<?> borrowBook(
        @Valid @RequestBody OrderRequest request
    ) {
        Inventory inventory = inventoryRepository
            .findByBookId(request.bookId())
            .orElseThrow(() ->
                new OutOfStockException("Book inventory not found")
            );

        if (inventory.getAvailableStock() <= 0) {
            throw new OutOfStockException("Book is out of stock");
        }

        inventory.setAvailableStock(
            inventory.getAvailableStock() - 1
        );

        inventoryRepository.save(inventory);

        BookOrder order = new BookOrder(
            request.userId(),
            request.bookId(),
            "BORROWED"
        );

        orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}

record OrderRequest(
    @Min(1) Long userId,
    @Min(1) Long bookId
) {}

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<String> handleOutOfStock(
        OutOfStockException exception
    ) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(exception.getMessage());
    }
}
