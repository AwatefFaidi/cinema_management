package org.sid.cinema.web;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.sid.cinema.dao.FilmRepository;
import org.sid.cinema.entities.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CinemaRestController {
	/* 
	@GetMapping(path="/listfilms")
	public List<Film> film(){
		return filmrepository.findAll();
		}*/
	@Autowired
	private FilmRepository filmrepository;

	@GetMapping(path="/imageFilm/{id}",produces=MediaType.IMAGE_JPEG_VALUE)
	public  byte[] image(@PathVariable(name="id")Long id)throws Exception{
		Film f= filmrepository.findById(id).get();
		String photoName=f.getPhoto(); 
		File file =new File(System.getProperty("user.home")+"/eclipse-workspace/cinema/images/"+photoName);
		//System.out.println(System.getProperty("user.home")+"/eclipse-workspace/cinema/images/"+photoName);
		Path path= Paths.get(file.toURI());
		return Files.readAllBytes(path);
	}
}
