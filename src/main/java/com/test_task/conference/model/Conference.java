package com.test_task.conference.model;

import com.test_task.conference.model.constraints.ConferenceMaxSeatsConstraint;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@ConferenceMaxSeatsConstraint
public class Conference {
    private @Id @GeneratedValue Long id;

    @NotEmpty(message = "Conference must have a name")
    private String name;

    @NotNull(message = "Conference must have a start time")
    private Date startTime;

    @NotNull(message = "Conference must have max seats")
    @Min(value = 1, message = "Conference must have atleast one seat")
    private Long maxSeats;

    @ManyToMany
    @JoinTable(
            name = "conference_participants",
            joinColumns = @JoinColumn(name = "conference_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id"))
    private List<Participant> participantList;

    public Conference() {
    }

    public Conference(String name, Date startTime, Long maxSeats, List<Participant> participantList) {
        this.name = name;
        this.startTime = startTime;
        this.maxSeats = maxSeats;
        this.participantList = Objects.requireNonNullElseGet(participantList, ArrayList::new);
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Long getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(Long maxSeats) {
        this.maxSeats = maxSeats;
    }

    public List<Participant> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(List<Participant> participantList) {
        this.participantList = participantList;
    }
}