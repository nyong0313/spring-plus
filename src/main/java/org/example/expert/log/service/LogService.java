package org.example.expert.log.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.log.entity.Log;
import org.example.expert.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Log save(String nickname, String errorMessage) {
        Log log = new Log(nickname, errorMessage);
        return logRepository.save(log);
    }
}
