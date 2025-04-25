package com.microerp.microerp.service;

import com.microerp.microerp.dto.InvoiceDTO;
import com.microerp.microerp.enums.InvoiceStatusEnum;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing Invoices.
 */
public interface InvoiceService {

    List<InvoiceDTO> getAllInvoices();
    Optional<InvoiceDTO> getInvoiceById(Long id);
    InvoiceDTO createInvoice(InvoiceDTO invoiceDTO);
    Optional<InvoiceDTO> updateInvoiceStatus(Long id, InvoiceStatusEnum status);
    boolean deleteInvoice(Long id);
    List<InvoiceDTO> getInvoicesByClientId(Long clientId);

}