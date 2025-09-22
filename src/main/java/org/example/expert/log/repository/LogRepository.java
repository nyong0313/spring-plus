package org.example.expert.log.repository;

import org.example.expert.log.entity.Log;
import org.springframework.data.repository.CrudRepository;

public interface LogRepository extends CrudRepository<Log, Long> {
}
