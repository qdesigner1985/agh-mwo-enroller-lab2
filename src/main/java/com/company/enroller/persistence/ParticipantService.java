package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("participantService")
public class ParticipantService {

    DatabaseConnector connector;

    public ParticipantService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Participant> getAll() {
        String hql = "FROM Participant";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }

    public Participant findByLogin(String login) {
        return connector.getSession().get(Participant.class, login);
    }

    public Collection<Participant> findByLoginKey(String key) {
        String hql = "SELECT p FROM Participant p WHERE p.login like '%" + key + "%'";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }

    public Participant add(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(participant);
        transaction.commit();
        return participant;
    }

    public void update(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().merge(participant);
        transaction.commit();
    }

    public void delete(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(participant);
        transaction.commit();
    }

    public Collection<Participant> getAll(String sortBy, String sortOrder, String loginvalue) {
        String hql = "FROM Participant WHERE login LIKE :login";
        if (sortBy.equals("login")){
            hql += " ORDER by " + sortBy;
            if (sortOrder.equals("ASC") || sortOrder.equals("DESC"))
            {
                hql += " " + sortOrder;
            }
        }
        Query query = connector.getSession().createQuery(hql);
        //query.setParameter("login", "user2");
        query.setParameter("login", "%" + loginvalue + "%");
        return query.list();
    }

}
