package com.test_task.conference.repository;

import com.test_task.conference.model.Conference;
import org.springframework.data.repository.CrudRepository;

public interface ConferenceRepo extends CrudRepository<Conference, Long> {

}
