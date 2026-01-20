package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.ClasseCategorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClasseCategorieRepository extends JpaRepository<ClasseCategorie, Integer> {
	java.util.Optional<ClasseCategorie> findByClasse_IdClasseAndCategorie_IdCategorie(Integer idClasse, Integer idCategorie);
	java.util.List<ClasseCategorie> findByCategorie_IdCategorie(Integer idCategorie);
}
