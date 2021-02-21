import java.io.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import java.util.List;
import java.util.Iterator;

public class ManipulerXML {

	static Document document;
	static Element racine,sousRacine,racineF;

	public static void main(String[] args)
	{
		try
		{
			lireFichier("xml_bpmn/motivating_example_part1.bpmn.xml");
			modifierFichier();
			enregistreFichier("xml_bpmn/bpmn1.xml");
		}
		catch(Exception e){}
	}
	
	//Méthode pour parser le fichier XML
	static void lireFichier(String fichier) throws Exception
	{
		SAXBuilder sxb = new SAXBuilder();
		document = sxb.build(new File(fichier));
		racine = document.getRootElement();
		
	}
	
	//Méthode pour modifier le fichier XML (supprimer definitions & process)
	static void modifierFichier () throws Exception
	{
		sousRacine = racine.getChild("process");//element process prend la racine
		racineF = new Element (sousRacine.getName()); //on crée une nouvelle racine "startEvent"
		System.out.println(racineF.getName());
		List listProcess = sousRacine.getChildren(); //une liste de tous les elements de process
		Iterator iRacine = listProcess.iterator(); //un ietrator sur listProcess
		while (iRacine.hasNext()) 
		{
			Element courant = (Element)iRacine.next();
			String nomFils = courant.getName();
			Element fils = new Element(nomFils);
			List listAttributs = courant.getAttributes();
			Iterator iAttribut = listAttributs.iterator();
			while (iAttribut.hasNext())
			{
				Attribute attribut = (Attribute)iAttribut.next();
				String nomAttribut = attribut.getName();
				String valAttribut = attribut.getValue();
				fils.setAttribute(nomAttribut, valAttribut);
			}
			racineF.addContent(fils);
		}
	}
	static void enregistreFichier(String fichier) throws Exception
		{
			Document docfinal = new Document(racineF);
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(docfinal, new FileOutputStream(fichier));
		}
}
