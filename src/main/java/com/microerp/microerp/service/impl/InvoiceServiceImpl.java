package com.microerp.microerp.service.impl;

import com.microerp.microerp.dto.InvoiceDTO;
import com.microerp.microerp.enums.InvoiceStatusEnum;
import com.microerp.microerp.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final Map<Long, InvoiceDTO> mockInvoices = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public InvoiceServiceImpl() {
        log.info("Initializing InvoiceServiceImpl with mock data...");
        createInvoiceInternal(1L, "Initial Consulting Service", BigDecimal.valueOf(1500.50), LocalDate.now().plusDays(30), 1L);
        createInvoiceInternal(2L, "Software License Renewal", BigDecimal.valueOf(499.99), LocalDate.now().plusDays(15), 1L);
        createInvoiceInternal(3L, "Hardware Maintenance", BigDecimal.valueOf(250.00), LocalDate.now().minusDays(5), 2L);
        log.info("Mock data initialized. Current invoices: {}", mockInvoices.size());
    }

    private InvoiceDTO createInvoiceInternal(Long id, String description, BigDecimal amount, LocalDate dueDate, Long clientId) {
        LocalDateTime now = LocalDateTime.now();
        InvoiceDTO invoice = InvoiceDTO.builder()
                .id(id)
                .clientId(clientId)
                .description(description)
                .amount(amount)
                .issueDate(LocalDate.now())
                .dueDate(dueDate)
                .status(InvoiceStatusEnum.PENDING)
                .paymentDate(null)
                .createdAt(now)
                .updatedAt(now)
                .build();
        mockInvoices.put(id, invoice);
        log.debug("Created and stored mock invoice: {}", invoice);
        return invoice;
    }


    @Override
    public List<InvoiceDTO> getAllInvoices() {
        log.info("Fetching all mock invoices");
        return new ArrayList<>(mockInvoices.values());
    }

    @Override
    public Optional<InvoiceDTO> getInvoiceById(Long id) {
        log.info("Fetching mock invoice by ID: {}", id);
        return Optional.ofNullable(mockInvoices.get(id))
                .map(this::copyInvoiceDTO);
    }

    @Override
    public InvoiceDTO createInvoice(InvoiceDTO newInvoiceDTO) {
        log.info("Creating new mock invoice for client ID: {}", newInvoiceDTO.getClientId());
        long newId = idCounter.incrementAndGet();

        InvoiceDTO createdInvoice = createInvoiceInternal(
                newId,
                newInvoiceDTO.getDescription(),
                newInvoiceDTO.getAmount(),
                newInvoiceDTO.getDueDate(),
                newInvoiceDTO.getClientId()
        );
        log.info("Successfully created mock invoice with ID: {}", newId);
        return copyInvoiceDTO(createdInvoice);
    }

    @Override
    public Optional<InvoiceDTO> updateInvoiceStatus(Long id, InvoiceStatusEnum status) {
        log.info("Attempting to update status of mock invoice ID: {} to {}", id, status);
        InvoiceDTO existingInvoice = mockInvoices.get(id);
        if (existingInvoice != null) {

            InvoiceDTO updatedInvoice = copyInvoiceDTO(existingInvoice); // Start with a copy
            updatedInvoice.setStatus(status);
            updatedInvoice.setUpdatedAt(LocalDateTime.now());

            if (status == InvoiceStatusEnum.PAID && updatedInvoice.getPaymentDate() == null) {
                updatedInvoice.setPaymentDate(LocalDate.now());
                log.info("Invoice {} marked as PAID, setting payment date.", id);
            } else if (status != InvoiceStatusEnum.PAID) {
                updatedInvoice.setPaymentDate(null);
                log.info("Invoice {} status changed to {}, clearing payment date if previously set.", id, status);
            }

            mockInvoices.put(id, updatedInvoice);
            log.info("Successfully updated status for mock invoice ID: {}", id);
            return Optional.of(copyInvoiceDTO(updatedInvoice));
        } else {
            log.warn("Mock invoice with ID: {} not found for status update.", id);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteInvoice(Long id) {
        log.info("Attempting to delete mock invoice with ID: {}", id);
        InvoiceDTO removed = mockInvoices.remove(id);
        if (removed != null) {
            log.info("Successfully deleted mock invoice ID: {}", id);
            return true;
        } else {
            log.warn("Mock invoice with ID: {} not found for deletion.", id);
            return false;
        }
    }

    @Override
    public List<InvoiceDTO> getInvoicesByClientId(Long clientId) {
        log.info("Fetching mock invoices for client ID: {}", clientId);
        return mockInvoices.values().stream()
                .filter(invoice -> invoice.getClientId().equals(clientId))
                .map(this::copyInvoiceDTO) // Return copies
                .collect(Collectors.toList());
    }

    private InvoiceDTO copyInvoiceDTO(InvoiceDTO original) {
        if (original == null) return null;
        return InvoiceDTO.builder()
                .id(original.getId())
                .clientId(original.getClientId())
                .description(original.getDescription())
                .amount(original.getAmount())
                .issueDate(original.getIssueDate())
                .dueDate(original.getDueDate())
                .status(original.getStatus())
                .paymentDate(original.getPaymentDate())
                .createdAt(original.getCreatedAt())
                .updatedAt(original.getUpdatedAt())
                .build();
    }
}