package dev.cobblesword.repository;

import dev.cobblesword.types.PlayerData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerDataRepository extends MongoRepository<PlayerData, String>
{
    Page<PlayerData> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<PlayerData> findAllByOrderByTotalWinsDesc(Pageable pageable);
}
