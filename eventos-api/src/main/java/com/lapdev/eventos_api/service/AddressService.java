package com.lapdev.eventos_api.service;

import com.lapdev.eventos_api.domain.address.Address;
import com.lapdev.eventos_api.domain.events.Event;
import com.lapdev.eventos_api.domain.events.EventRequestDTO;
import com.lapdev.eventos_api.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address createAddress(EventRequestDTO data, Event event){
        Address address = new Address();
        address.setCity(data.city());
        address.setUf(data.uf());
        address.setEvent(event);

        return addressRepository.save(address);
    }
}
