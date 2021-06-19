package org.sid.cinema.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.sid.cinema.dao.*;
import org.sid.cinema.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService {
     @Autowired
	private VilleRepository villerepository;
     @Autowired
	private CinemaRepository cinemarepository;
     @Autowired
	private PlaceRepository placerepository;
     @Autowired
	private CategorieRepository categorierepository;
     @Autowired
	private TicketRepository ticketrepository;
     @Autowired
	private SalleRepository sallerepository;
     @Autowired
	private SeanceRepository seancerepository;
     @Autowired
	private ProjectionRepository projectionrepository;
     @Autowired
	private FilmRepository filmrepository;
	
	@Override
	public void initVilles() 
	{  //create list 
		Stream.of("tunis", "djerba","sousse","kairouan","Mahdia").forEach(nameville->
		{	Ville ville= new Ville();
			ville.setName(nameville);
			villerepository.save(ville);
		});	
	}

	@Override
	public void initCinemas() {

		villerepository.findAll().forEach(v->{
			Stream.of("cinemamadina","founon","imax","megarama","carthage")
				.forEach(namecinema->{
					Cinema cinema = new Cinema();
					cinema.setName(namecinema);
					cinema.setNombreSalles(3+(int)(Math.random()*7));
					cinema.setVille(v);
					cinemarepository.save(cinema);
				});
		});
		
	}

	
	@Override
	public void initSalles() {
		cinemarepository.findAll().forEach(cinema ->
		{
			for(int i=0;i<cinema.getNombreSalles();i++)
			{
			Salle salle =new Salle();
			salle.setName("salle"+(i+1));
			salle.setCinema(cinema);
			salle.setNombrePlace(20+(int)(Math.random()*10));
			sallerepository.save(salle);
			}
		});
		
	}
	@Override
	public void initPlaces() {
		sallerepository.findAll().forEach(salle ->
		{
			for(int i=0; i<salle.getNombrePlace();i++)
			{
				Place place =new Place();
				place.setNumero(i+1);
				place.setSalle(salle);
				placerepository.save(place);
			}
			
		});
		
	}
	
	@Override
	public void initSeances() {
		DateFormat dateformat=new SimpleDateFormat("HH:mm");
		Stream.of("12:00","15:00","19:00","21:00").forEach(s->{
			Seance seance =new Seance();
			try {
				seance.setHeureDebut(dateformat.parse(s));
				seancerepository.save(seance);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
		
	}
	
	@Override
	public void initCategories() {
			Stream.of("Romantic","Action","fiction","Drama").forEach(c->{
			Categorie categorie =new Categorie();
			categorie.setName(c);
			categorierepository.save(categorie);
			
			});
	}
	
	@Override
	public void initFilms() {
		List<Categorie> categories = categorierepository.findAll();
		double[] duree=new double [] {1,1.5,2,2.5,3};
		Stream.of("Arrival","Luca","Flint","Forevermygirl","SpiderMan").forEach(f->{
		Film film =new Film();
		film.setTitre(f);
		film.setDuree(duree[new Random().nextInt(duree.length)]);
		film.setPhoto(f+".jpg");
		film.setCategorie(categories.get(new Random().nextInt(categories.size())));
		filmrepository.save(film);
		});
	}
	@Override
	public void initProjections() {
		double[] price=new double [] {50,100,150,200};
		villerepository.findAll().forEach(ville->{
			ville.getCinemas().forEach(cinema->{
				cinema.getSalles().forEach(salle->{
					filmrepository.findAll().forEach(film->{
						seancerepository.findAll().forEach(seance->{
							Projection projection =new Projection();
							projection.setDateProjection(new Date());
							projection.setFilm(film);
							projection.setPrix(price[new Random().nextInt(price.length)]);
							projection.setSalle(salle);
							projection.setSeance(seance);
							projectionrepository.save(projection);
						});
					});
				});
			});
			
		});
		
	}

	
	@Override
	public void initTickets() {
		projectionrepository.findAll().forEach(p->{
			p.getSalle().getPlaces().forEach(place->{
				Ticket ticket = new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReserve(false);
			  ticketrepository.save(ticket);
			});
		});
		
	}

	

	
	
	

	

	
}
