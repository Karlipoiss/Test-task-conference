package com.test_task.conference.service;

import com.test_task.conference.model.Participant;
import com.test_task.conference.repository.ParticipantRepo;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class ParticipantService {
    private final ParticipantRepo participantRepo;

    public ParticipantService(ParticipantRepo participantRepo) {
        this.participantRepo = participantRepo;
    }

    public Participant saveParticipant(Participant participant) {
        return participantRepo.save(participant);
    }

    public List<Participant> getParticipants() {
        return (List<Participant>) participantRepo.findAll();
    }

    public Participant getParticipant(Long id) {
        return participantRepo.findById(id).orElseThrow(RuntimeException::new);
    }

    public Participant updateParticipant(Long id, Participant participant) {
        Participant currentParticipant = participantRepo.findById(id).orElseThrow(RuntimeException::new);
        currentParticipant.setName(participant.getName());
        return saveParticipant(currentParticipant);
    }

    public void deleteParticipant(Long id) {
        participantRepo.deleteById(id);
    }
}
