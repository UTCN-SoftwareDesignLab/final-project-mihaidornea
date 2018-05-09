package server.repository;

import server.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import server.model.User;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByFromUserAndToUser(User fromUser, User toUser);

}
