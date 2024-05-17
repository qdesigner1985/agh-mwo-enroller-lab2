package com.company.enroller.persistence;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;
	Session session;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Meeting findById(long id) {
		Meeting meeting = (Meeting) connector.getSession().get(Meeting.class, id);
		return meeting;
	}
	public Collection<Meeting> findMeetings(String title, String description, Participant participant, String sortMode) {
		String hql = "FROM Meeting as meeting WHERE title LIKE :title AND description LIKE :description ";
		if (participant!=null) {
			hql += " AND :participant in elements(participants)";
		}
		if (sortMode.equals("title")) {
			hql += " ORDER BY title";
		}
		Query query = this.session.createQuery(hql);
		query.setParameter("title", "%" + title + "%").setParameter("description", "%" + description + "%");
		if (participant!=null) {
			query.setParameter("participant", participant);
		}

		return query.list();
	}

	public void create(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public void deleteMeeting(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(meeting);
		transaction.commit();
	}

	public void updateMeeting(Meeting meetingUpdate) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(meetingUpdate);
		transaction.commit();
	}

	public void addParticipant(Long id, Participant participant) {
		Meeting meeting = findById(id);
		meeting.addParticipant(participant);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().update(meeting);
		transaction.commit();
	}

	public void removeParticipant(Long id, Participant participant) {
		Meeting meeting = findById(id);
		meeting.removeParticipant(participant);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().update(meeting);
		transaction.commit();
	}
}