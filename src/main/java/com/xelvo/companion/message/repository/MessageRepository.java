package com.xelvo.companion.message.repository;

import com.xelvo.companion.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findBySenderIdOrRecipientIdOrderByCreatedAtDesc(Long senderId, Long recipientId, Pageable pageable);
    List<Message> findBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByCreatedAtAsc(
            Long senderId1, Long recipientId1, Long senderId2, Long recipientId2);
    long countByRecipientIdAndReadAtIsNull(Long recipientId);
}
