/**
 * 
 */
package fdi.ucm.server.importparser.oda.coleccion.categoria;

import java.sql.ResultSet;
import java.sql.SQLException;

import fdi.ucm.server.importparser.oda.InterfaceOdaparser;
import fdi.ucm.server.importparser.oda.NameConstantsOda;
import fdi.ucm.server.importparser.oda.StaticFunctionsOda;
import fdi.ucm.server.importparser.oda.coleccion.LoadCollectionOda;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteResourceElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteResourceElementURL;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteLinkElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalView;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteResourceElementType;

/**
 * Clase que define la implementacion de una clase para los files.
 * @author Joaquin Gayoso-Cabada
 *
 */
public class Grammar_URL implements InterfaceOdaparser {

	private CompleteGrammar AtributoMeta;
//	private MetaText PATH;
	private CompleteResourceElementType PathF;
	private CompleteOperationalView VistaOV;
	private LoadCollectionOda LColec;
	private static CompleteLinkElementType OWNER;
	
	public Grammar_URL(CompleteCollection completeCollection, LoadCollectionOda L) {
		AtributoMeta=new CompleteGrammar(NameConstantsOda.URL, NameConstantsOda.FILENAME,completeCollection);
		LColec=L;
		
		VistaOV=new CompleteOperationalView(NameConstantsOda.PRESNTACION); 
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsOda.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsOda.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsOda.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		
		CompleteOperationalView VistaOVMeta=new CompleteOperationalView(NameConstantsOda.META);
		
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsOda.TYPE,NameConstantsOda.URL,VistaOVMeta);
		
		VistaOVMeta.getValues().add(ValorMeta);
		
		AtributoMeta.getViews().add(VistaOVMeta);
		
		AtributoMeta.getViews().add(VistaOV);
	}
	
	
	/* (non-Javadoc)
	 * @see fdi.ucm.server.importparser.oda1.Oda1parserModel#ProcessAttributes()
	 */
	@Override
	public void ProcessAttributes() {
		

				
		{
			PathF=new CompleteResourceElementType(NameConstantsOda.URI, AtributoMeta);
			AtributoMeta.getSons().add(PathF);
			
			CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsOda.PRESNTACION); 
			
			CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsOda.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsOda.BROWSERSHOWN,Boolean.toString(false),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsOda.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			VistaOV.getValues().add(Valor);
			VistaOV.getValues().add(Valor2);
			VistaOV.getValues().add(Valor3);
			
			CompleteOperationalView VistaOVMeta=new CompleteOperationalView(NameConstantsOda.META);
			CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsOda.TYPE,NameConstantsOda.URI,VistaOV);
			VistaOVMeta.getValues().add(ValorMeta);
			
			PathF.getShows().add(VistaOVMeta);
			PathF.getShows().add(VistaOV);
			}
	}

	/* (non-Javadoc)
	 * @see fdi.ucm.server.importparser.oda1.Oda1parserModel#ProcessInstances()
	 */
	@Override
	public void ProcessInstances() {
			try {
				ResultSet rs=LColec.getSQL().RunQuerrySELECT("SELECT id,idov,name FROM resources where type='U';");
				if (rs!=null) 
				{
					while (rs.next()) {
						
						
						String id=rs.getObject("id").toString();
						String idov=rs.getObject("idov").toString();
				
						String name="";
						if(rs.getObject("name")!=null)
							name=rs.getObject("name").toString();
						
						
						if (idov!=null&&!idov.isEmpty()&&!name.isEmpty())
							{
						
							name=name.trim();
							name=StaticFunctionsOda.CleanStringFromDatabase(name,LColec);
							
							
							CompleteCollection C=LColec.getCollection().getCollection();
							
							
							
							
							
//							sectionValue.setIcon(sectionValue);
							CompleteDocuments sectionValue=new CompleteDocuments(C,AtributoMeta,name,name);
							
							
							C.getEstructuras().add(sectionValue);
							
//							MetaTextValue E=new MetaTextValue(PATH, idov+"/"+name);
//							sectionValue.getDescription().add(E);
							
							
							LColec.getCollection().getURLC().put(name, sectionValue);
							
							//Element MV=new Element(AtributoMeta);
					//		sectionValue.getDescription().add(MV);
							
							CompleteResourceElement MRV=new CompleteResourceElementURL(PathF, name);
							sectionValue.getDescription().add(MRV);
							
//							sectionValue.setIcon(Path);

								
							
							}
						else{
							if (idov==null||idov.isEmpty())
								LColec.getLog().add("Warning: Objeto Virtual asociado al que se asocia el recurso propio es vacio, Idrecurso: '"+id+"' es vacio o el file referencia asociado es vacio (ignorado)");
							if (name==null||name.isEmpty())
								LColec.getLog().add("Warning: Nombre recurso propio asociado vacio, Idrecurso: '"+id+"' (ignorado)");
						}
					}
				rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

	}


	/**
	 * @return the atributoMeta
	 */
	public CompleteGrammar getAtributoMeta() {
		return AtributoMeta;
	}


	/**
	 * @param atributoMeta the atributoMeta to set
	 */
	public void setAtributoMeta(CompleteGrammar atributoMeta) {
		AtributoMeta = atributoMeta;
	}


	/**
	 * @return the oWNER
	 */
	public static CompleteLinkElementType getOWNER() {
		return OWNER;
	}


	/**
	 * @param oWNER the oWNER to set
	 */
	public static void setOWNER(CompleteLinkElementType oWNER) {
		OWNER = oWNER;
	}

	
	
}
