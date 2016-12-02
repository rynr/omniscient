package org.rjung.service.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MessageRepository extends ElasticsearchRepository<MessageDTO, String> {

    Page<MessageDTO> findByUserOrderByCreatedAtDesc(String user, Pageable pageable);

    MessageDTO findOneByIdAndUser(String id, String user);

}
