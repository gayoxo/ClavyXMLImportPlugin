/**
 * 
 */
package fdi.ucm.server.importparser.oda.oda1.coleccion.categoria;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import fdi.ucm.server.importparser.oda.InterfaceOdaparser;
import fdi.ucm.server.importparser.oda.NameConstantsOda;
import fdi.ucm.server.importparser.oda.coleccion.LoadCollectionOda;
import fdi.ucm.server.importparser.oda.coleccion.categoria.ElementType_ObjetoVirtual_Resource;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteOperationalValue;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteIterator;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalView;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * Clase que define la creacion de un virtual object metadata
 * @author Joaquin Gayoso-Cabada
 *
 */
public class Grammar_ObjetoVirtual implements InterfaceOdaparser {

	private CompleteGrammar AtributoMeta;
	private CompleteTextElementType IDOV;
	private ElementType_ObjetoVirtual_Resource Recursos;
	private CompleteOperationalValueType ValorOdaPUBLIC;
//	private HashMap<Integer, Element> ObjetoVirtualMetaValueAsociado;
	private CompleteOperationalView VistaOV;
	private CompleteOperationalView VistaOVOda;
	private LoadCollectionOda LColec;
	private CompleteTextElementType URLORIGINAL;

	
	public Grammar_ObjetoVirtual(CompleteCollection completeCollection, LoadCollectionOda L) {
		AtributoMeta=new CompleteGrammar(NameConstantsOda.VIRTUAL_OBJECTNAME, NameConstantsOda.VIRTUAL_OBJECTNAME,completeCollection);
		
		VistaOV=new CompleteOperationalView(NameConstantsOda.PRESNTACION); 
		LColec=L;
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsOda.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsOda.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsOda.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		
		CompleteOperationalView VistaOVMeta=new CompleteOperationalView(NameConstantsOda.META);

		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsOda.TYPE,NameConstantsOda.VIRTUAL_OBJECT,VistaOVMeta);
		
		VistaOVMeta.getValues().add(ValorMeta);
	
		
		VistaOVOda=new CompleteOperationalView(NameConstantsOda.ODA);
		
		ValorOdaPUBLIC=new CompleteOperationalValueType(NameConstantsOda.PUBLIC,Boolean.toString(true),VistaOVOda);

		CompleteOperationalValueType ValorOda2=new CompleteOperationalValueType(NameConstantsOda.PRIVATE,Boolean.toString(false),VistaOVOda);

		VistaOVOda.getValues().add(ValorOdaPUBLIC);
		VistaOVOda.getValues().add(ValorOda2);
		
		AtributoMeta.getViews().add(VistaOVMeta);
		
		AtributoMeta.getViews().add(VistaOV);
		
		AtributoMeta.getViews().add(VistaOVOda);
	}
	
	
	/* (non-Javadoc)
	 * @see fdi.ucm.server.importparser.oda1.Oda1parserModel#ProcessAttributes()
	 */
	@Override
	public void ProcessAttributes() {
		{
		IDOV=new CompleteTextElementType(NameConstantsOda.IDOVNAME, AtributoMeta);
		
		
		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsOda.PRESNTACION); 
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsOda.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsOda.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsOda.SUMMARYSHOWN,Boolean.toString(true),VistaOV);
		
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		
		CompleteOperationalView VistaOVMeta=new CompleteOperationalView(NameConstantsOda.META);

		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsOda.TYPE,NameConstantsOda.IDOV,VistaOVMeta);
		
		VistaOVMeta.getValues().add(ValorMeta);
		
		IDOV.getShows().add(VistaOVMeta);
		
		IDOV.getShows().add(VistaOV);
		
		AtributoMeta.getSons().add(IDOV);
		
		CompleteOperationalView VistaOV2=new CompleteOperationalView(NameConstantsOda.METATYPE);
		 CompleteOperationalValueType Valor4=new CompleteOperationalValueType(NameConstantsOda.METATYPETYPE,NameConstantsOda.TEXT,VistaOV2);
		 VistaOV2.getValues().add(Valor4);
		 IDOV.getShows().add(VistaOV2);
		
		}
		
		{
			URLORIGINAL=new CompleteTextElementType(NameConstantsOda.URLORIGINAL, AtributoMeta);
			
			
			CompleteOperationalView VistaOVMeta=new CompleteOperationalView(NameConstantsOda.META);

			CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsOda.TYPE,NameConstantsOda.URLORIGINAL,VistaOVMeta);
			
			VistaOVMeta.getValues().add(ValorMeta);
			
			URLORIGINAL.getShows().add(VistaOVMeta);
			
			CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsOda.METATYPE);
			 CompleteOperationalValueType Valor4=new CompleteOperationalValueType(NameConstantsOda.METATYPETYPE,NameConstantsOda.TEXT,VistaOV);
			 VistaOV.getValues().add(Valor4);
			 URLORIGINAL.getShows().add(VistaOV);
			
			 CompleteOperationalView VistaOV2=new CompleteOperationalView(NameConstantsOda.META);
			 CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsOda.METATYPETYPE,NameConstantsOda.IGNORED,VistaOV2);
			 VistaOV2.getValues().add(Valor);
			 URLORIGINAL.getShows().add(VistaOV2);
			 
			
			AtributoMeta.getSons().add(URLORIGINAL);
			}
		
		
		
		CompleteIterator I=new CompleteIterator(AtributoMeta);
		AtributoMeta.getSons().add(I);
		Recursos=new ElementType_ObjetoVirtual_Resource(I,LColec);
		Recursos.ProcessAttributes();
		I.getSons().add(Recursos.getAtributoMeta());
		

		
		
		
	}

	/* (non-Javadoc)
	 * @see fdi.ucm.server.importparser.oda1.Oda1parserModel#ProcessInstances()
	 */
	@Override
	public void ProcessInstances() {
		OwnInstances();
		Recursos.ProcessInstances();

	}

	/**
	 * Procesa las instancias propias
	 */
	private void OwnInstances() {
//		 ObjetoVirtualMetaValueAsociado=new HashMap<Integer, Element>();
		 HashMap<Integer, CompleteDocuments> ObjetoVirtual= new HashMap<Integer, CompleteDocuments>();
		try {
			ResultSet rs=LColec.getSQL().RunQuerrySELECT("SELECT * FROM virtual_object;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String id=rs.getObject("id").toString();
					
					String publicoT="";
					if(rs.getObject("ispublic")!=null)
						publicoT=rs.getObject("ispublic").toString();
					
					if (publicoT!=null&&!publicoT.isEmpty())
						{
						int Idov=Integer.parseInt(id);
						boolean publico=true;
						if (publicoT.equals("N"))
							publico=false;
						CompleteCollection C=LColec.getCollection().getCollection();
						CompleteDocuments sectionValue = new CompleteDocuments(C,AtributoMeta,"","");
						C.getEstructuras().add(sectionValue);
						CompleteTextElement E=new CompleteTextElement(IDOV, id);
						sectionValue.getDescription().add(E);
						
						CompleteOperationalValue ValorOdaPUBLIInstance=new CompleteOperationalValue(ValorOdaPUBLIC,Boolean.toString(publico));

				//		Element MetaValueAsociado = new Element(AtributoMeta);
						sectionValue.getViewsValues().add(ValorOdaPUBLIInstance);
//						MetaValueAsociado.getShows().add(ValorOdaPUBLIInstance);
						
//						ObjetoVirtualMetaValueAsociado.put(Idov, MetaValueAsociado);
						ObjetoVirtual.put(Idov, sectionValue);
						
						
						StringBuffer SB=new StringBuffer();
						
						if (LColec.getBaseURLOdaSimple().isEmpty()||
								(!LColec.getBaseURLOdaSimple().startsWith("http://")
										&&!LColec.getBaseURLOdaSimple().startsWith("https://")
										&&!LColec.getBaseURLOdaSimple().startsWith("ftp://")))
							SB.append("http://");
						
						SB.append(LColec.getBaseURLOdaSimple());
						if (!LColec.getBaseURLOdaSimple().isEmpty()&&!LColec.getBaseURLOdaSimple().endsWith("//"))
							SB.append("/");
						SB.append(NameConstantsOda.VIEWDOC+id);
						
						String Path=SB.toString();
						
						CompleteTextElement RR=new CompleteTextElement(URLORIGINAL, Path);
						sectionValue.getDescription().add(RR);
						
						}
					else {
						if (publicoT==null||publicoT.isEmpty())
							LColec.getLog().add("Warning: Estado de privacidad ambiguo en, Identificador Objeto virtual: '"+id+"' (ignorado)");
					}
				}
				LColec.getCollection().setObjetoVirtual(ObjetoVirtual);
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
	 * @return the vistaOV
	 */
	public CompleteOperationalView getVistaOV() {
		return VistaOV;
	}


	/**
	 * @param vistaOV the vistaOV to set
	 */
	public void setVistaOV(CompleteOperationalView vistaOV) {
		VistaOV = vistaOV;
	}

	
	
	


	
	

}
