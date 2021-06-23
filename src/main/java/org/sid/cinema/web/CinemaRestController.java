package org.sid.cinema.web;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.sid.cinema.dao.FilmRepository;
import org.sid.cinema.dao.TicketRepository;
import org.sid.cinema.entities.Film;
import org.sid.cinema.entities.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@RestController
public class CinemaRestController {
	/* 
	@GetMapping(path="/listfilms")
	public List<Film> film(){
		return filmrepository.findAll();
		}*/
	@Autowired
	private FilmRepository filmrepository;
	@Autowired
	private TicketRepository ticketrepository;

	@GetMapping(path="/imageFilm/{id}",produces=MediaType.IMAGE_JPEG_VALUE)
	public  byte[] image(@PathVariable(name="id")Long id)throws Exception{
		Film f= filmrepository.findById(id).get();
		String photoName=f.getPhoto(); 
		File file =new File(System.getProperty("user.home")+"/eclipse-workspace/cinema/images/"+photoName);
		//System.out.println(System.getProperty("user.home")+"/eclipse-workspace/cinema/images/"+photoName);
		Path path= Paths.get(file.toURI());
		return Files.readAllBytes(path);
	}
	@PostMapping("/payerTickets")
	@Transactional
	public List<Ticket> payerTikets(@RequestBody TicketForm ticketForm)
	{
		List<Ticket> listTickets= new ArrayList<>();
		ticketForm.getTickets().forEach(idTicket->
		{
			Ticket ticket=ticketrepository.findById(idTicket).get();
			ticket.setNomClient(ticketForm.getNomclient());
			ticket.setCodePayement(ticketForm.getCodePayement());
			ticket.setReserve(true);
			ticketrepository.save(ticket);
			listTickets.add(ticket);
		});
		return listTickets;
	}
}
@Data 
class TicketForm{
	private String nomclient;
	private int codePayement;
	private List<Long> tickets = new ArrayList <>();
}