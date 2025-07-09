package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.LikeDto;
import com.atm.buenas_practicas_java.entities.Like;
import com.atm.buenas_practicas_java.repositories.LikeRepo;
import com.atm.buenas_practicas_java.services.mapper.LikeMapper;
import org.springframework.stereotype.Service;

@Service
public class LikeService extends AbstractBusinessService<Like, Integer, LikeDto,
        LikeRepo, LikeMapper> {

    public LikeService(LikeRepo repo, LikeMapper mapper) {
        super(repo, mapper);
    }
}

