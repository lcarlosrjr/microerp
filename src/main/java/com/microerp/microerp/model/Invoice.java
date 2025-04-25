package com.microerp.microerp.model;

import com.microerp.microerp.enums.InvoiceStatusEnum; // We'll create this enum next
import jakarta.persistence.*; // Ready for JPA
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal; // Using BigDecimal for monetary values is safer
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data // Lombok: generates getters, setters, toString, equals, hashCode
@Builder // Lombok: Builder pattern
@NoArgsConstructor // Lombok: No-args constructor
@AllArgsConstructor // Lombok: All-args constructor
@Entity // JPA: Marks as an entity
@Table(name = "invoices") // JPA: Table name in the database
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA: Auto-incremented ID
    private Long id;

    @Column(nullable = false) // JPA: Cannot be null in the database
    private Long clientId; // Reference to the client's ID (we can map the @ManyToOne relationship later)

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2) // JPA: Defines precision and scale for decimal values
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING) // JPA: Stores the enum name (PENDING, PAID, etc.) in the database
    @Column(nullable = false, length = 20)
    private InvoiceStatusEnum status;

    private LocalDate paymentDate; // Can be null if not yet paid

    @Column(nullable = false, updatable = false) // Not updatable after creation
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // JPA Callbacks to set dates automatically
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.issueDate == null) {
            this.issueDate = LocalDate.now(); // Set issue date if not provided
        }
        if (this.status == null) {
            this.status = InvoiceStatusEnum.PENDING; // Default status
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // We could add the relationship with Client here:
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "clientId", insertable = false, updatable = false)
    // private Client client;
}