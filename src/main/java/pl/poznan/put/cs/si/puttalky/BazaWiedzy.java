package pl.poznan.put.cs.si.puttalky;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.kie.api.runtime.KieSession;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/** Author: agalawrynowicz<br>
 * Date: 19-Dec-2016 */

public class BazaWiedzy {

    private OWLOntologyManager manager = null;
    private OWLOntology ontologia;
    private Set<OWLClass> listaKlas;
    private Set<OWLClass> listaDodatkow;
    private Set<OWLClass> listaNazwanychPizz;
	private Set<OWLClass> listaMięsnychPizz;
	private Set<OWLClass> listaWegePizz;
    
    OWLReasoner silnik;
    
    public void inicjalizuj() {
		InputStream plik = this.getClass().getResourceAsStream("/pizza.owl");
		manager = OWLManager.createOWLOntologyManager();
		
		try {
			ontologia = manager.loadOntologyFromOntologyDocument(plik);
			silnik = new Reasoner.ReasonerFactory().createReasoner(ontologia);
			listaKlas = ontologia.getClassesInSignature();
			listaDodatkow = new HashSet<OWLClass>();
			listaNazwanychPizz = new HashSet<OWLClass>();
			listaMięsnychPizz = new HashSet<OWLClass>();
			listaWegePizz = new HashSet<OWLClass>();

			OWLClass dodatek  = manager.getOWLDataFactory().getOWLClass(IRI.create("http://semantic.cs.put.poznan.pl/ontologie/pizza.owl#Dodatek"));
			for (org.semanticweb.owlapi.reasoner.Node<OWLClass> klasa: silnik.getSubClasses(dodatek, false)) {
				listaDodatkow.add(klasa.getRepresentativeElement());
			}

			OWLClass nazwanaPizza  = manager.getOWLDataFactory().getOWLClass(IRI.create("http://semantic.cs.put.poznan.pl/ontologie/pizza.owl#NazwanaPizza"));
			for (org.semanticweb.owlapi.reasoner.Node<OWLClass> klasaP: silnik.getSubClasses(nazwanaPizza, false)) {
				listaNazwanychPizz.add(klasaP.getRepresentativeElement());
			}
			OWLClass mięsnaPizza  = manager.getOWLDataFactory().getOWLClass(IRI.create("http://semantic.cs.put.poznan.pl/ontologie/pizza.owl#PizzaZMięsem"));
			for (org.semanticweb.owlapi.reasoner.Node<OWLClass> klasaP: silnik.getSubClasses(mięsnaPizza, false)) {
				listaMięsnychPizz.add(klasaP.getRepresentativeElement());
			}
			OWLClass wegePizza  = manager.getOWLDataFactory().getOWLClass(IRI.create("http://semantic.cs.put.poznan.pl/ontologie/pizza.owl#PizzaWegetariańska"));
			for (org.semanticweb.owlapi.reasoner.Node<OWLClass> klasaP: silnik.getSubClasses(wegePizza, false)) {
				listaWegePizz.add(klasaP.getRepresentativeElement());
			}
			
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    public Set<String> dopasujDodatek(String s){
    	Set<String> result = new HashSet<String>();
    	for (OWLClass klasa : listaDodatkow){
    		if (klasa.toString().toLowerCase().contains(s.toLowerCase()) && s.length()>2){
    			result.add(klasa.getIRI().toString());
    		}
    	}
    	return result;
    }

	public Set<String> dopasujNazwePizzy(String s){
		Set<String> result = new HashSet<String>();
		for (OWLClass klasa : listaNazwanychPizz){
			if (klasa.toString().toLowerCase().contains(s.toLowerCase()) && s.length()>2){
				result.add(klasa.getIRI().toString());
			}
		}
		return result;
	}
    
    public Set<String> wyszukajPizzePoDodatkach(ArrayList<String> iri){
    	Set<String> pizze = new HashSet<String>();
    	OWLObjectProperty maDodatek = manager.getOWLDataFactory().getOWLObjectProperty(IRI.create("http://semantic.cs.put.poznan.pl/ontologie/pizza.owl#maDodatek"));
    	Set<OWLClassExpression> ograniczeniaEgzystencjalne = new HashSet<OWLClassExpression>();
    	
    	for (String i: iri) {
    		OWLClass dodatek = manager.getOWLDataFactory().getOWLClass(IRI.create(i));
    		OWLClassExpression wyrazenie = manager.getOWLDataFactory().getOWLObjectSomeValuesFrom(maDodatek, dodatek);
    		ograniczeniaEgzystencjalne.add(wyrazenie);
    	}   	
  	
    	OWLClassExpression pozadanaPizza = manager.getOWLDataFactory().getOWLObjectIntersectionOf(ograniczeniaEgzystencjalne);
    	
		for (org.semanticweb.owlapi.reasoner.Node<OWLClass> klasa: silnik.getSubClasses(pozadanaPizza, false)) {
			pizze.add(klasa.getEntities().iterator().next().asOWLClass().getIRI().getFragment());
		}
	
		return pizze;
    }

	public Set<String> getWegePizze() {
    	Set<String> pizze = new HashSet<String>();
    	for (OWLClass klasa : listaWegePizz){
    		pizze.add(klasa.toString());
		}
		return pizze;
	}

	public Set<String> getMięsnePizze() {
		Set<String> pizze = new HashSet<String>();
		for (OWLClass klasa : listaMięsnychPizz){
			pizze.add(klasa.toString());
		}
		return pizze;
	}

	public static void main(String[] args) {
		BazaWiedzy baza = new BazaWiedzy();
		baza.inicjalizuj();
		
		OWLClass mieso = baza.manager.getOWLDataFactory().getOWLClass(IRI.create("http://semantic.cs.put.poznan.pl/ontologie/pizza.owl#DodatekMięsny"));
		for (org.semanticweb.owlapi.reasoner.Node<OWLClass> klasa: baza.silnik.getSubClasses(mieso, true)) {
			System.out.println("klasa:"+klasa.toString());
		}
		for (OWLClass d:  baza.listaDodatkow){
			System.out.println("dodatek: "+d.toString());
		}

	}

}
