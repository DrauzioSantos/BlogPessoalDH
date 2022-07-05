package com.blogpessoal.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import com.blogpessoal.blogpessoal.model.Usuario;
import com.blogpessoal.blogpessoal.repository.UsuarioRepository;
import com.blogpessoal.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start()
	{
		usuarioRepository.deleteAll();
	}
	
	
	@Test
	@Order(1)
	@DisplayName("Cadastrar um Usuário")
	public void deveCriarUmUsuario()
	{
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,"Gabriel Henrique","gabriel_henrique@gmail.com","12345678","http://fotolegal.jpg")); 
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar",HttpMethod.POST,requisicao,Usuario.class);
		assertEquals(HttpStatus.CREATED,resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(),resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(),resposta.getBody().getUsuario());
		assertEquals(requisicao.getBody().getFoto(),resposta.getBody().getFoto());
	}
	
	@Test
	@Order(2)
	@DisplayName("Não deve permitir ducplicação de Usuário")
	public void naoDeveDuplicarUsuario()
	{
		usuarioService.CadastrarUsuario(new Usuario(0L,"Adriana Mucciolo","adriana_mucciolo@gmail.com","12345678","http://fotolegaladriana.jpg"));
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,"Adriana Mucciolo","adriana_mucciolo@gmail.com","12345678","http://fotolegaladriana.jpg"));
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar",HttpMethod.POST,requisicao,Usuario.class);
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
		
	}
	
	@Test
	@Order(3)
	@DisplayName("Alterar um Usuário")
	public void deveAlterarUmUsuario()
	{
		Optional<Usuario> usuarioCreate = usuarioService.CadastrarUsuario(new Usuario(0L,"Joyce","joyce@gmail.com","12345678","http://fotojoyce.jpg"));
		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(),"Joyce Meireles","joyce_meireles@@gmail.com","12345678","http://fotojoyce.jpg");
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);
		ResponseEntity<Usuario> resposta = testRestTemplate.withBasicAuth("root","root").exchange("/usuarios/cadastrar",HttpMethod.PUT,requisicao,Usuario.class);
		assertEquals(HttpStatus.OK,resposta.getStatusCode());
		assertEquals(usuarioUpdate.getNome(),resposta.getBody().getNome());
		assertEquals(usuarioUpdate.getUsuario(),resposta.getBody().getUsuario());
		assertEquals(usuarioUpdate.getFoto(),resposta.getBody().getFoto());
	}
	
	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios()
	{
		usuarioService.CadastrarUsuario(new Usuario(0L,"Kevim Lhouis","kevim.lhouis@gmail.com","12345678","http://fotokevim.jpg"));
		usuarioService.CadastrarUsuario(new Usuario(0L,"Vanessa Jesus","vanesa.jesus@gmail.com","12345678","http://fotovanessa.jpg"));
		ResponseEntity<String> resposta = testRestTemplate.withBasicAuth("root","root").exchange("/usuario/all", HttpMethod.GET,null,String.class);
		assertEquals(HttpStatus.OK,resposta.getStatusCode());
	}
}
