package monprojet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.extern.slf4j.Slf4j;
import monprojet.dao.CityRepository;
import monprojet.dao.CountryRepository;
import monprojet.entity.City;
import monprojet.entity.Country;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/cities") // This means URL's start with /cities (after Application path)
@Slf4j
public class CityController {

	// On affichera par défaut la page 'cities.mustache'
	@Autowired
	private CityRepository cityDao;
	@Autowired
	private CountryRepository countryDao;
    private static final String DEFAULT_VIEW = "cities"; 
	/**
	 * Affiche la page d'édition des villes
	 * @param model Les infos transmises à la vue (injecté par Spring)
	 * @return le nom de la vue à afficher
	 */	
	@GetMapping(path = "show") //à l'URL http://localhost:8989/cities/show
	public String montreLesVilles(Model model) {
		log.info("On affiche les villes");
		// On initialise la ville avec des valeurs par défaut
		Country france = countryDao.findById(1).orElseThrow();
		City nouvelle = new City("Nouvelle ville", france);
		nouvelle.setPopulation(50);
		model.addAttribute("cities", cityDao.findAll());
		model.addAttribute("city", nouvelle);
		model.addAttribute("countries", countryDao.findAll());
		return DEFAULT_VIEW;
	}

    /**
	 * Insère une nouvelle ville dans la base
	 * @param laVille la ville à insérer, initialisée par Spring à partir des valeurs soumises dans le formulaire
	 * Spring fera automatiquement une requête SQL SELECT pour récupérer le pays à partir de son id.	 
	 * Spring fera une requête SQL INSERT pour insérer la ville dans la base
	 * @return
	 */
	@PostMapping(path="save") // Requête HTTP POST à l'URL http://localhost:8989/cities/save
	public String enregistreUneVille(City laVille) {
		cityDao.save(laVille);
		log.info("La ville {} a été enregistrée", laVille);
		// On redirige vers la page de liste des villes
		return "redirect:/cities/show";
	}

    /**
	 * Montre le formulaire permettant d'ajouter une ville
	 * @param laVille initialisé par Spring, valeurs par défaut à afficher dans le formulaire 
	 * @return le nom de la vue à afficher ('formulaireCity.html')
	 */
	@GetMapping(path = "add")
	public String montreLeFormulairePourAjout(Model model) {
		Country france = countryDao.findById(1).orElseThrow();
		City nouvelle = new City("Nouvelle ville", france);
		nouvelle.setPopulation(50);
		model.addAttribute("cities", cityDao.findAll());
		model.addAttribute("city", nouvelle);
		model.addAttribute("countries", countryDao.findAll());
		//return "formulaireCity";
        return DEFAULT_VIEW;
	}

    /**
	 * Appelé par le lien 'Modifier' dans 'show.html'
	 * Montre le formulaire permettant de modifier une ville
	 * @param laVille à partir de l'id de la ville transmis en paramètre, 
	 *                  Spring fera une requête SQL SELECT pour chercher la ville dans la base
	 * @param model pour transmettre les informations à la vue
	 * @return le nom de la vue à afficher ('editCity.html')
	 */
	@GetMapping(path = "edit")
	public String montreLeFormulairePourEdition(@RequestParam("id") City laVille, Model model) {
		model.addAttribute("cities", cityDao.findAll());
		model.addAttribute("countries", countryDao.findAll());
		model.addAttribute("city", laVille);
		//return "formulaireCity";
        return DEFAULT_VIEW;
	}

    /**
	 * Appelé par le lien 'Supprimer' dans 'show.html'
	 * @param categorie à partir de l'id de la ville transmis en paramètre, 
	 *                  Spring fera une requête SQL SELECT pour chercher la ville dans la base
	 * @return une redirection vers l'affichage de la liste des villes
	 */
	@GetMapping(path = "delete")
	public String supprimeUneCategoriePuisMontreLaListe(@RequestParam("id") City laVille) {
		cityDao.delete(laVille); // Ici on peut avoir une erreur (Si il y a des produits dans cette catégorie par exemple)
		return "redirect:show"; // on se redirige vers l'affichage de la liste
	}
}