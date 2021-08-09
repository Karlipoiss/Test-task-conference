package com.test_task.conference.service;

import com.test_task.conference.model.Conference;
import com.test_task.conference.model.Participant;
import com.test_task.conference.model.constraints.ConferenceMaxSeatsConstraint;
import com.test_task.conference.model.constraints.ConferenceMaxSeatsValidator;
import com.test_task.conference.repository.ConferenceRepo;
import com.test_task.conference.repository.ParticipantRepo;

import javax.transaction.Transactional;
import javax.validation.*;
import java.util.List;

@Transactional
public class ConferenceService {

    private final ConferenceRepo conferenceRepo;
    private final ParticipantRepo participantRepo;

    public ConferenceService(ConferenceRepo conferenceRepo, ParticipantRepo participantRepo) {
        this.conferenceRepo = conferenceRepo;
        this.participantRepo = participantRepo;
    }

    public List<Conference> getConferences() {
        return (List<Conference>) conferenceRepo.findAll();
    }

    public Conference getConference(Long id) {
        return conferenceRepo.findById(id).orElseThrow(RuntimeException::new);
    }

    public Long getConferenceAvailableSeats(Long id) {
        Conference conference = conferenceRepo.findById(id).orElseThrow(RuntimeException::new);
        return conference.getMaxSeats() - conference.getParticipantList().size();
    }

    public Conference saveConference(Conference conference) {
        return conferenceRepo.save(conference);
    }

    public Conference updateConference(Long id, Conference conference) {
        Conference currentConference = conferenceRepo.findById(id).orElseThrow(RuntimeException::new);
        currentConference.setName(conference.getName());
        currentConference.setStartTime(conference.getStartTime());
        currentConference.setMaxSeats(conference.getMaxSeats());

        return saveConference(conference);
    }

    public void deleteConference(Long id) {
        conferenceRepo.deleteById(id);
    }

    public Conference addParticipant(Long conferenceId, Long participantId) throws Exception {
        Participant participant = participantRepo.findById(participantId).orElseThrow(RuntimeException::new);
        Conference currentConference = conferenceRepo.findById(conferenceId).orElseThrow(RuntimeException::new);
        List<Participant> participantList = currentConference.getParticipantList();
        if (getConferenceAvailableSeats(conferenceId) <= 0) {
            throw new Exception("Conference can't have more participants then number of max seats");
        }
        participantList.add(participant);
        currentConference.setParticipantList(participantList);
        return saveConference(currentConference);
    }

    public Conference removeParticipant(Long conferenceId, Long participantId) {
        Participant participant = participantRepo.findById(participantId).orElseThrow(RuntimeException::new);
        Conference currentConference = conferenceRepo.findById(conferenceId).orElseThrow(RuntimeException::new);
        List<Participant> participantList = currentConference.getParticipantList();
        participantList.remove(participant);
        currentConference.setParticipantList(participantList);

        return saveConference(currentConference);
    }
}
