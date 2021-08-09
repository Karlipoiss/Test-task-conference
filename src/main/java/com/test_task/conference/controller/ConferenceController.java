package com.test_task.conference.controller;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import com.test_task.conference.model.Conference;
import com.test_task.conference.model.Participant;
import com.test_task.conference.repository.ConferenceRepo;
import com.test_task.conference.repository.ParticipantRepo;
import com.test_task.conference.service.ConferenceService;
import com.test_task.conference.service.ParticipantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/conferences")
public class ConferenceController {

    private final ConferenceService conferenceService;
    private final ParticipantService participantService;

    public ConferenceController(ConferenceRepo conferenceRepo, ParticipantRepo participantRepo) {
        this.conferenceService = new ConferenceService(conferenceRepo, participantRepo);
        this.participantService = new ParticipantService(participantRepo);
    }

    @GetMapping
    public List<Conference> getConferences() {
        return conferenceService.getConferences();
    }

    @GetMapping("/{id}")
    public Conference getConference(@PathVariable Long id) {
        return conferenceService.getConference(id);
    }

    @GetMapping("/{id}/available_seats")
    public Long getConferenceAvailableSeats(@PathVariable Long id) {
        return conferenceService.getConferenceAvailableSeats(id);
    }

    @PostMapping
    public ResponseEntity createConference(@Valid @RequestBody Conference conference) throws URISyntaxException {
        Conference savedConference = conferenceService.saveConference(conference);
        return ResponseEntity.created(new URI("/conferences/" + savedConference.getId())).body(savedConference);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateConference(@PathVariable Long id, @Valid @RequestBody Conference conference) {
        Conference currentConference = conferenceService.updateConference(id, conference);
        return ResponseEntity.ok(currentConference);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteConference(@PathVariable Long id) {
        conferenceService.deleteConference(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/participant")
    public ResponseEntity addNewParticipant(@PathVariable Long id, @Valid @RequestBody Participant participant) throws Exception {
        participant = participantService.saveParticipant(participant);
        Conference currentConference = conferenceService.addParticipant(id, participant.getId());
        return ResponseEntity.ok(currentConference);
    }

    @PutMapping("/{id}/participant/{participantId}")
    public ResponseEntity addExistingParticipant(@PathVariable Long id,  @PathVariable Long participantId) throws Exception {
        Conference currentConference = conferenceService.addParticipant(id, participantId);
        return ResponseEntity.ok(currentConference);
    }

    @DeleteMapping("/{id}/participant/{participantId}")
    public ResponseEntity removeParticipant(@PathVariable Long id, @PathVariable Long participantId) {
        Conference currentConference = conferenceService.removeParticipant(id, participantId);
        return ResponseEntity.ok(currentConference);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, List<String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        List<String> errorMessages = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errorMessages.add(error.getDefaultMessage());
        });
        Collections.sort(errorMessages);
        errors.put("error", errorMessages);
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, List<String>> handleConstraintExceptions(ConstraintViolationException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        List<String> errorMessages = new ArrayList<>();
        ex.getConstraintViolations().forEach((error) -> {
            errorMessages.add(error.getMessage());
        });
        Collections.sort(errorMessages);
        errors.put("error", errorMessages);
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Map<String, List<String>> handleLogicExceptions(Exception ex) {
        return Collections.singletonMap("error", List.of(ex.getMessage()));
    }
}