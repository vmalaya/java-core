package com.sigma.software.repositories;

import com.sigma.software.entities.Message;
import com.sigma.software.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.*;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class MessageRepository {
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.sigma.software");
    private EntityManager entityManager = entityManagerFactory.createEntityManager();

    public void save(Message message) throws NamingException {
        UserTransaction transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        try {
            transaction.begin();
        } catch (NotSupportedException | SystemException e) {
            e.printStackTrace();
        }
        message.setDateTime(ZonedDateTime.now());
        User sender = message.getSender();
        Long senderId = sender.getId();
        List<Message> sentMessages = findSentMessages(senderId);
        sender.setSentMessages(sentMessages);
        User recipient = message.getRecipient();
        recipient.setReceivedMessages(findReceivedMessages(recipient.getId()));
        entityManager.merge(message);
        try {
            transaction.commit();
        } catch (RollbackException | HeuristicRollbackException | SystemException | HeuristicMixedException e) {
            e.printStackTrace();
        }
    }

    public List<Message> findSentMessages(Long userId) {
        List<Message> resultList = entityManager.createQuery("select m from Message as m where m.sender.id = :userId",
                                                             Message.class)
                                                .setParameter("userId", userId)
                                                .getResultList();
        return Collections.unmodifiableList(resultList);
    }

    public List<Message> findReceivedMessages(Long userId) {
        List<Message> resultList = entityManager.createQuery("select m from Message as m where m.recipient.id = :userId",
                                                             Message.class)
                                                .setParameter("userId", userId)
                                                .getResultList();
        return Collections.unmodifiableList(resultList);
    }

    public List<Message> findMessages(User user) {
        List<Message> messages = entityManager.createQuery(
                "select m from Message as m where  m.sender.id = :id or m.recipient.id =:id",
                Message.class)
                                              .setParameter("id", user.getId())
                                              .getResultList();
        return Collections.unmodifiableList(messages);
    }
}
