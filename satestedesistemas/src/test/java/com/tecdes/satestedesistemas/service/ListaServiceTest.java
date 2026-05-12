package com.tecdes.satestedesistemas.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tecdes.satestedesistemas.repository.ListaRepository;

@ExtendWith(MockitoExtension.class)
public class ListaServiceTest {
    @Mock
    public ListaRepository listaRepository;

    @InjectMocks
    public ListaService listaService;
}
