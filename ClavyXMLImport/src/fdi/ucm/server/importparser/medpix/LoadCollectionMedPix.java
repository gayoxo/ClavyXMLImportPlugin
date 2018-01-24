/**
 * 
 */
package fdi.ucm.server.importparser.medpix;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fdi.ucm.server.modelComplete.ImportExportDataEnum;
import fdi.ucm.server.modelComplete.ImportExportPair;
import fdi.ucm.server.modelComplete.LoadCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollectionAndLog;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * @author Joaquin Gayoso Cabada
 *
 */
public class LoadCollectionMedPix extends LoadCollection{

	
	private static ArrayList<ImportExportPair> Parametros;
	private CompleteCollection CC;
	public static boolean consoleDebug=false;

	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LoadCollectionMedPix LC=new LoadCollectionMedPix();
		LoadCollectionMedPix.consoleDebug=true;
		
		ArrayList<String> AA=new ArrayList<String>();
		AA.add("Ejemplo");
		AA.add("ArtsSpainXML.xml");
		
		CompleteCollectionAndLog Salida=LC.processCollecccion(AA);
		if (Salida!=null)
			{
			
			System.out.println("Correcto");
			
			for (String warning : Salida.getLogLines())
				System.err.println(warning);

			
			System.exit(0);
			
			}
		else
			{
			System.err.println("Error");
			System.exit(-1);
			}
	}

	

	@Override
	public CompleteCollectionAndLog processCollecccion(ArrayList<String> dateEntrada) {
		try {
			
			HashMap<CompleteGrammar,HashMap<String,CompleteTextElementType>> ListaElem=new HashMap<CompleteGrammar,HashMap<String,CompleteTextElementType>>();
			HashMap<String,CompleteGrammar> ListaGram=new HashMap<String,CompleteGrammar>();
			
			CompleteCollectionAndLog Salida=new CompleteCollectionAndLog();
			CC=new CompleteCollection(dateEntrada.get(0), new Date()+"");
			Salida.setCollection(CC);
			Salida.setLogLines(new ArrayList<String>());
			
			String FileS = dateEntrada.get(1);
			File XMLD=new File(FileS);
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(XMLD);
			
			NodeList nList = doc.getElementsByTagName("records");
			nList = ((Element)nList.item(0)).getElementsByTagName("record");
			
			
			for (int temp = 0; temp < nList.getLength(); temp++)
			{
				
				try {
					Node nNode = nList.item(temp);
					
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						
						Element eElement = (Element) nNode;
						String descript = eElement.getElementsByTagName("description").item(0).getTextContent();
						String icon = eElement.getElementsByTagName("icon").item(0).getTextContent();
						CompleteDocuments CD=new CompleteDocuments(CC, descript, icon);
						
						CC.getEstructuras().add(CD);
						
						try {
							NodeList Resto=eElement.getChildNodes();
							for (int temp2 = 0; temp2 < Resto.getLength(); temp2++)
							{
								
								try {
									Node nNodeH = Resto.item(temp2);
									
									if (nNodeH.getNodeType() == Node.ELEMENT_NODE) {
										
										
										
										Element eElement2 = (Element) nNodeH;
										if (!(eElement2.getTagName().equals("description")||(eElement2.getTagName().equals("icon"))))
												{
											String GA=eElement2.getAttribute("gram");
											if (GA.isEmpty()||GA==null)
												GA="Ungrammar";
											
											CompleteGrammar Grama=ListaGram.get(GA);
											
											if (Grama==null)
												{
												Grama=new CompleteGrammar(GA, GA, CC);
												CC.getMetamodelGrammar().add(Grama);
												ListaGram.put(GA, Grama);
												ListaElem.put(Grama, new HashMap<String,CompleteTextElementType>());
												Salida.getLogLines().add("Creada la Gramatica-> " +GA);
												}
												
											String ElemTy=eElement2.getTagName();
											
											HashMap<String, CompleteTextElementType> ListaElemTy = ListaElem.get(Grama);
											CompleteTextElementType ElementoTy=ListaElemTy.get(ElemTy);
											
											if (ElementoTy==null)
											{
											ElementoTy=new CompleteTextElementType(ElemTy, Grama);
											Grama.getSons().add(ElementoTy);
											ListaElemTy.put(ElemTy, ElementoTy);
											ListaElem.put(Grama, ListaElemTy);
											Salida.getLogLines().add("Creada la Gramatica-> " +ElemTy);
											}
											
											String Valor=eElement2.getTextContent();
											
											CompleteTextElement EE=new CompleteTextElement(ElementoTy,Valor);
											CD.getDescription().add(EE);
											
											
												}
										
										
										
									
									}
								} catch (Exception e) {
									Salida.getLogLines().add("Error in record "+temp);
								}
								
								
								
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						
					
					}
				} catch (Exception e) {
					Salida.getLogLines().add("Error in record "+temp);
				}
				
				
				
			}
			
//			CompleteCollectionAndLog Salida=new CompleteCollectionAndLog();
//			CC=new CompleteCollection("MedPix", new Date()+"");
//			Salida.setCollection(CC);
//			Logs=new ArrayList<String>();
//			Salida.setLogLines(Logs);
//			encounterID=new HashMap<String,CompleteDocuments>();
//			topicID=new HashMap<String,List<CompleteDocuments>>();
//			ListImageEncounter=new ArrayList<CompleteElementTypeencounterIDImage>();
//			ListImageEncounterTopics=new ArrayList<CompleteElementTypeencounterIDImage>();
//			ListTopicID=new ArrayList<CompleteElementTypetopicIDTC>();
//			
//			ProcesaCasos();
//			ProcesaCasoID();
//			ProcesaTopics();
//			//AQUI se puede trabajar
			
			
			return Salida;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	


	

	@Override
	public ArrayList<ImportExportPair> getConfiguracion() {
		if (Parametros==null)
		{
			ArrayList<ImportExportPair> ListaCampos=new ArrayList<ImportExportPair>();
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.Text, "Collection Name"));
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.File, "XML File"));
			Parametros=ListaCampos;
			return ListaCampos;
		}
		else return Parametros;
	}

	@Override
	public String getName() {
		return "XML";
	}

	@Override
	public boolean getCloneLocalFiles() {
		return false;
	}

}
