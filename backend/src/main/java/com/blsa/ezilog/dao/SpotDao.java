package com.blsa.ezilog.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blsa.ezilog.model.spot.SpotArea;

public interface SpotDao extends JpaRepository<SpotArea, Long> {

    @Query(value = "SELECT * FROM spot_area where now() between upload_date and expire_date", countQuery = "SELECT count(*) FROM spot_area where now() between upload_date and expire_date", nativeQuery = true)
    Page<SpotArea> allValidSpot(Pageable request);
    
    @Query(value = "SELECT * FROM spot_area where now() between upload_date and expire_date", nativeQuery = true)
    List<SpotArea> allValidSpotNotPage();
    
    @Query(value = "SELECT * FROM spot_area where expire_date < now()", nativeQuery = true)
    Optional<List<SpotArea>> allNotValidSpot();

    @Query(value = "SELECT * FROM spot_area WHERE owner = :owner and expire_date > now()", nativeQuery = true)
    Optional<List<SpotArea>> checkValidUserSpot(String owner);

    @Query(value = "SELECT * FROM spot_area WHERE post_no = :postNo and expire_date > now()", nativeQuery = true)
    Optional<SpotArea> checkValidPostSpot(Long postNo);
}
