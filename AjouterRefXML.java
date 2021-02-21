import java.io.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import java.util.List;
import java.util.Iterator;

public class AjouterRefXML {

	static Document document;
	static Element racine,nRacine;

	public static void main(String[] args)
	{
		try
		{
			lireFichier("xml_bpmn/bpmn1.xml");
			ajouterReference();
			enregistreFichier("xml_bpmn/bpmn2.xml");
		}
		catch(Exception e){}
	}
	
	//Méthode pour parser le fichier XML
	static void lireFichier(String fichier) throws Exception
	{
		SAXBuilder sxb = new SAXBuilder(); // on crée une instance de SAXBuilder
		document = sxb.build(new File(fichier)); // on crée un nouveau document JDOM avec en arguement le fichier XML
		racine = document.getRootElement(); // on initialise un nouvel element racine avec l'element racine du document
		System.out.println("Etape 1 : Le parsing du XML est terminé");
	}
	
	//Méthode pour ajouter l'attribut Ref aux balises : startEvent, inclusiveGateway, exclusiveGateway, rangeGateway et task
	static void ajouterReference () throws Exception
	{
		nRacine = new Element (racine.getName()); // nouvelle racine pour le nouveau document
		List listProcess = racine.getChildren(); // liste des enfants de la racine process
		Iterator iProcess = listProcess.iterator(); // un iterator pour la liste
		int iRef = 1; // la reference pour les attributs des balises : startEvent,les gateways, les tasks et endEvent
		while (iProcess.hasNext())
		{
			Element courant = (Element)iProcess.next(); // on récrée l'Element courant à chaque tour de boucle afin de pouvoir utiliser les méthodes propres aux Element
			String nomCourant = courant.getName(); // Nom de l'element courant
			Element fils = new Element(nomCourant); // on crée un element fils pour pourvoir faire la madifications puis l'enregistrement dans le nouvel fichier XML
			String nomFils = fils.getName(); // Nom de l'element fils
			List listAttributs = courant.getAttributes(); // liste des attributs de l'element courant
			Iterator iAttribut = listAttributs.iterator(); // un iterator pour la liste des attributs de l'element courant
			while (iAttribut.hasNext())
			{
				Attribute attribut = (Attribute)iAttribut.next(); // on récrée l'Attribut de l'element courant à chaque tour de boucle afin de pouvoir utiliser les méthodes propres aux Attributs
				String nomAttribut = attribut.getName(); // Nom de l'attribut de l'element courant
				String valAttribut = attribut.getValue(); // Valeur de l'attribut de l'element courant
				fils.setAttribute(nomAttribut, valAttribut); // fils prend les attributs et leurs valeurs de chaque element
			}
			// on passe à l'ajout de Ref et sa valeur qui est convertie en String (String.valueOf(iRef))
			if(nomFils.equals("inclusiveGateway")) //si l'element est inclusiveGateway
			{
				fils.setAttribute("Ref", String.valueOf(iRef));
				iRef++;
			}
			if(nomFils.equals("exclusiveGateway")) //si l'element est exclusiveGateway
			{
				fils.setAttribute("Ref", String.valueOf(iRef));
				iRef++;
			}
			if(nomFils.equals("task")) //si l'element est task
			{
				fils.setAttribute("Ref", String.valueOf(iRef));
				iRef++;
			}
			if(nomFils.equals("rangeGateway")) //si l'element est rangeGateway
			{
				fils.setAttribute("Ref", String.valueOf(iRef));
				iRef++;
			}
			nRacine.addContent(fils);	
		}
		System.out.println("Etape 2 : L'ajout des refs aux balises est terminé");
	}
	
	static void enregistreFichier(String fichier) throws Exception
		{
			Document docfinal = new Document(nRacine);
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(docfinal, new FileOutputStream(fichier));
			System.out.println("Etape 3 : L'enregistrement du fichier XML est terminé");
		}
}
