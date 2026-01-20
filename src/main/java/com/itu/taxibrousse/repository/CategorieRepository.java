package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
}
