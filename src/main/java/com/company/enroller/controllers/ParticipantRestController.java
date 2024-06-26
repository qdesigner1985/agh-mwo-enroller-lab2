package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)//dodawanie użytkownika
	public ResponseEntity<?> addParticipant(@RequestBody Participant participant) {
		if (participantService.findByLogin(participant.getLogin()) != null) {
			return new ResponseEntity<String>(
					"Unable to create. A participant with login " + participant.getLogin() + " already exist.",
					HttpStatus.CONFLICT);
		}
		participantService.add(participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)//aktualizacja użytkownika
	public ResponseEntity<?> update(@PathVariable("id") String login, @RequestBody Participant updatedParticipant) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		participant.setPassword(updatedParticipant.getPassword());
		participantService.update(participant);
		return new ResponseEntity<Participant>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)//usuwanie użytkownika
	public ResponseEntity<?> delete(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		participantService.delete(participant);
		return new ResponseEntity<Participant>(HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(@RequestParam (value = "sortBy", defaultValue = "") String sortMethod,
			@RequestParam (value = "sortOrder", defaultValue = "ASC") String sortOrder,
			@RequestParam (value = "key", defaultValue = "") String sortKey) {
				if (!sortMethod.equals("login") && !sortOrder.equals("ASC") && !sortOrder.equals("DESC")){
						return new ResponseEntity("Wrong input parameters", HttpStatus.CONFLICT);
				}
				Collection<Participant> participants = participantService.getAll(sortMethod, sortOrder, sortKey);
				return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
			}
	//@RequestMapping(value = "", method = RequestMethod.GET)
	//public ResponseEntity<?> getParticipantss() {
	//	Collection<Participant> participants = participantService.getAll();
	//	return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	//}

}
