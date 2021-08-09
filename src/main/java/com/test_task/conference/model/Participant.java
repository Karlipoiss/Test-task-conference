package com.test_task.conference.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
public class Participant {
    private @Id @GeneratedValue Long id;

    @NotEmpty(message = "Participant must have a name")
    private String name;

    @ManyToMany(mappedBy = "participantList")
    private List<Conference> conferenceList;

    public Participant() {}

    public Participant(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
