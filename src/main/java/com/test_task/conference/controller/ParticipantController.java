package com.test_task.conference.controller;

import com.test_task.conference.model.Participant;
import com.test_task.conference.repository.ParticipantRepo;
import com.test_task.conference.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    @Autowired
    public ParticipantController(ParticipantRepo participantRepo) {
        this.participantService = new ParticipantService(participantRepo);
    }

    @GetMapping
    public List<Participant> getParticipants() {
        return participantService.getParticipants();
    }

    @GetMapping("/{id}")
    public Participant getParticipant(@PathVariable Long id) {
        return participantService.getParticipant(id);
    }

    @PostMapping
    public ResponseEntity createParticipant(@Valid @RequestBody Participant participant) throws URISyntaxException {
        Participant savedParticipant = participantService.saveParticipant(participant);
        return ResponseEntity.created(new URI("/participants/" + savedParticipant.getId())).body(savedParticipant);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateParticipant(@PathVariable Long id, @Valid @RequestBody Participant participant) {
        Participant currentParticipant = participantService.updateParticipant(id, participant);
        return ResponseEntity.ok(currentParticipant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteParticipant(@PathVariable Long id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.ok().build();
    }

}
