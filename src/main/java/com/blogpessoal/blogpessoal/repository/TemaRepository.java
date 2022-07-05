package com.blogpessoal.blogpessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogpessoal.blogpessoal.model.Tema;

public interface TemaRepository extends JpaRepository<Tema,Long> {

	public List <Tema> findAllDescricaoContainingIgnoreCase(String descricao);
	
}
