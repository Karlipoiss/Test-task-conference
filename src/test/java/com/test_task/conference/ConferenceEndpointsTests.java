package com.test_task.conference;

import com.test_task.conference.model.Conference;
import com.test_task.conference.model.Participant;
import com.test_task.conference.repository.ConferenceRepo;
import com.test_task.conference.repository.ParticipantRepo;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConferenceEndpointsTests {

	@LocalServerPort
	private Integer port;

	@Autowired
	private ConferenceRepo conferenceRepo;
	@Autowired
	private ParticipantRepo participantRepo;

	private Long initialConferenceId;
	private Long initialParticipantId;

	@BeforeEach
	void setUp() {
		conferenceRepo.deleteAll();
		participantRepo.deleteAll();

		Participant participant = participantRepo.save(new Participant("Artur"));
		initialParticipantId = participant.getId();
		Conference conference = conferenceRepo.save(new Conference("Initial", new Date(0L), 2L, List.of(participant)));
		initialConferenceId = conference.getId();
		RestAssured.port = port;
	}

	@Test
	void shouldGetInitialConference() {
		RestAssured.
			given()
				.filter(new RequestLoggingFilter())
				.contentType("application/json")
			.when()
				.get("http://localhost:" + port + "/conferences")
			.then()
				.statusCode(200)
				.body("$.size()", Matchers.equalTo(1))
				.body("[0].name", Matchers.equalTo("Initial"))
				.body("[0].maxSeats", Matchers.equalTo(2))
				.body("[0].startTime", Matchers.equalTo("1970-01-01T00:00:00.000+00:00"))
				.body("[0].participantList[0].name", Matchers.equalTo("Artur"));
	}

	@Test
	void shouldCreateConference() {
		ExtractableResponse<Response> response = RestAssured.
			given()
				.filter(new RequestLoggingFilter())
				.contentType("application/json")
				.body("{\"name\": \"Suur conference\", \"maxSeats\":100, \"startTime\":\"2019-01-20\"}")
			.when()
				.post("http://localhost:" + port + "/conferences")
			.then()
				.statusCode(201)
				.extract();

		RestAssured
			.when()
				.get(response.header("Location"))
			.then()
				.statusCode(200)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("Suur conference"))
				.body("maxSeats", Matchers.equalTo(100))
				.body("startTime", Matchers.equalTo("2019-01-20T00:00:00.000+00:00"))
				.body("participantList", Matchers.equalTo(new ArrayList<Participant>()));
	}

	@Test
	void shouldNotUpdateConference() {
		RestAssured
			.given()
				.filter(new RequestLoggingFilter())
				.contentType("application/json")
				.body("{\"name\": \"Muudetud\", \"maxSeats\":-1, \"startTime\":\"2020-01-20\"}")
			.when()
				.put("http://localhost:" + port + "/conferences/" + initialConferenceId)
			.then()
				.statusCode(400)
				.body("error[0]", Matchers.equalTo("Conference can't have more participants then number of max seats"))
				.body("error[1]", Matchers.equalTo("Conference must have atleast one seat"));
	}

	@Test
	void shouldDeleteConference() {
		RestAssured
			.given()
				.filter(new RequestLoggingFilter())
				.contentType("application/json")
			.when()
				.delete("http://localhost:" + port + "/conferences/" + initialConferenceId)
			.then()
				.statusCode(200)
				.body(Matchers.equalTo(""));
	}

	@Test
	void shouldAddParticipant() {
		RestAssured.
			given()
				.filter(new RequestLoggingFilter())
				.contentType("application/json")
				.body("{\"name\": \"Karl Marten\"}")
			.when()
				.post("http://localhost:" + port + "/conferences/" + initialConferenceId + "/participant")
			.then()
				.statusCode(200)
				.body("name", Matchers.equalTo("Initial"))
				.body("maxSeats", Matchers.equalTo(2))
				.body("startTime", Matchers.equalTo("1970-01-01T00:00:00.000+00:00"))
				.body("participantList[1].name", Matchers.equalTo("Karl Marten"));
	}

	@Test
	void shouldNotAddParticipant() {
		shouldAddParticipant();
		RestAssured
			.given()
				.filter(new RequestLoggingFilter())
				.contentType("application/json")
				.body("{\"name\": \"Marcus\"}")
			.when()
				.post("http://localhost:" + port + "/conferences/" + initialConferenceId + "/participant")
			.then()
				.statusCode(400)
				.body("error[0]", Matchers.equalTo("Conference can't have more participants then number of max seats"));
	}

	@Test
	void shouldRemoveParticipant() {
		RestAssured
			.given()
				.filter(new RequestLoggingFilter())
				.contentType("application/json")
			.when()
				.delete("http://localhost:" + port + "/conferences/" + initialConferenceId + "/participant/" + initialParticipantId)
			.then()
				.statusCode(200)
				.body("name", Matchers.equalTo("Initial"))
				.body("maxSeats", Matchers.equalTo(2))
				.body("startTime", Matchers.equalTo("1970-01-01T00:00:00.000+00:00"))
				.body("participantList", Matchers.equalTo(new ArrayList<Participant>()));
	}

	@Test
	void shouldGetAvailableSeats() {
		RestAssured
			.given()
				.filter(new RequestLoggingFilter())
				.contentType("application/json")
			.when()
				.get("http://localhost:" + port + "/conferences/"  + initialConferenceId + "/available_seats")
			.then()
				.statusCode(200)
				.body(Matchers.equalTo("1"));
	}
}
