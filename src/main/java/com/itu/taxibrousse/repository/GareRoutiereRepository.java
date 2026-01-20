package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.GareRoutiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GareRoutiereRepository extends JpaRepository<GareRoutiere, Integer> {
    List<GareRoutiere> findByVille_IdVille(Integer idVille);
}
