package com.test_task.conference.model.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConferenceMaxSeatsValidator.class)
@Target({ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConferenceMaxSeatsConstraint {
    String message() default "Conference can't have more participants then number of max seats";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}