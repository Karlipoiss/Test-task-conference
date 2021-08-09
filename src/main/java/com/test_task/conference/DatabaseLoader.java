package com.test_task.conference;

import com.test_task.conference.model.Conference;
import com.test_task.conference.repository.ConferenceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class DatabaseLoader implements CommandLineRunner {

    private final ConferenceRepo repository;

    @Autowired
    public DatabaseLoader(ConferenceRepo repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws Exception {
        repository.save(new Conference("Test123", new Date(0L), 50L, null));
    }

}