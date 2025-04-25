package com.microerp.microerp.service.impl;

import com.microerp.microerp.dto.ClientDTO;
import com.microerp.microerp.enums.ClientTypeEnum;
import com.microerp.microerp.service.ClientService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ClientServiceImpl implements ClientService {

    private final Map<Long, ClientDTO> mockDb = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();

    public ClientServiceImpl() {

        ClientDTO mockClient = ClientDTO.builder()
                .id(counter.incrementAndGet())
                .fullName("John Doe")
                .documentNumber("12345678900")
                .type(ClientTypeEnum.INDIVIDUAL)
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("john@example.com")
                .phone("123456789")
                .city("Sample City")
                .state("Sample State")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        mockDb.put(mockClient.getId(), mockClient);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return new ArrayList<>(mockDb.values());
    }

    @Override
    public ClientDTO getClientById(Long id) {
        return mockDb.get(id);
    }

    @Override
    public ClientDTO createClient(ClientDTO dto) {
        long id = counter.incrementAndGet();
        dto = dto.toBuilder()
                .id(id)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        mockDb.put(id, dto);
        return dto;
    }

    @Override
    public ClientDTO updateClient(Long id, ClientDTO dto) {
        dto = dto.toBuilder()
                .id(id)
                .updatedAt(LocalDateTime.now())
                .build();

        mockDb.put(id, dto);
        return dto;
    }

    @Override
    public void deleteClient(Long id) {
        mockDb.remove(id);
    }
}
