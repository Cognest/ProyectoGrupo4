package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.CarteraDto;
import com.atm.buenas_practicas_java.entities.Cartera;
import com.atm.buenas_practicas_java.repositories.CarteraRepo;
import com.atm.buenas_practicas_java.services.mapper.CarteraMapper;
import org.springframework.stereotype.Service;

@Service
public class CarteraService extends AbstractBusinessService<Cartera,Integer, CarteraDto,
        CarteraRepo, CarteraMapper>{

    public CarteraService(CarteraRepo repo, CarteraMapper serviceMapper) {
        super(repo, serviceMapper);
    }
}
