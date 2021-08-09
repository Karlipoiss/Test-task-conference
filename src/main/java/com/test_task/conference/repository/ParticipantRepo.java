package com.test_task.conference.repository;

import com.test_task.conference.model.Participant;
import org.springframework.data.repository.CrudRepository;

public interface ParticipantRepo extends CrudRepository<Participant, Long> {

}
