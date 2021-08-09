package com.test_task.conference.model.constraints;

import com.test_task.conference.model.Conference;
import com.test_task.conference.model.Participant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ConferenceMaxSeatsValidator implements ConstraintValidator<ConferenceMaxSeatsConstraint, Conference> {
    @Override
    public void initialize(ConferenceMaxSeatsConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Conference criteria, ConstraintValidatorContext constraintValidatorContext) {
        List<Participant> participantList = criteria.getParticipantList();
        if (participantList == null) {
            participantList = new ArrayList<>();
        }
        return criteria.getMaxSeats() >= participantList.size();
    }
}