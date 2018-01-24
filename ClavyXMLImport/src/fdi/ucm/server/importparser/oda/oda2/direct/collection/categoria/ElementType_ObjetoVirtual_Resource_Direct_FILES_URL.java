/**
 * 
 */
package fdi.ucm.server.importparser.oda.oda2.direct.collection.categoria;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import fdi.ucm.server.importparser.oda.NameConstantsOda;
import fdi.ucm.server.importparser.oda.StaticFunctionsOda;
import fdi.ucm.server.importparser.oda.coleccion.LoadCollectionOda;
import fdi.ucm.server.importparser.oda.coleccion.categoria.ElementType_ObjetoVirtual_Resource;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteFile;
import fdi.ucm.server.modelComplete.collection.document.CompleteOperationalValue;
import fdi.ucm.server.modelComplete.collection.document.CompleteResourceElementFile;
import fdi.ucm.server.modelComplete.collection.document.CompleteResourceElementURL;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteIterator;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalView;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteResourceElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * Implementa el atributo de los recursos en los Objetos Virtuales
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_ObjetoVirtual_Resource_Direct_FILES_URL extends ElementType_ObjetoVirtual_Resource {

	private ArrayList<Long> idsOV=new ArrayList<Long>();
	
	public ElementType_ObjetoVirtual_Resource_Direct_FILES_URL(CompleteIterator I,LoadCollectionOda L) {
		super();
		IteradorPadre=I;
		AtributoMeta=new CompleteResourceElementType(NameConstantsOda.RESOURCENAME,I);
		LColec=L;
		idsOV=new ArrayList<Long>();
		
		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsOda.PRESNTACION); 
		
		Valor2=new CompleteOperationalValueType(NameConstantsOda.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor4=new CompleteOperationalValueType(NameConstantsOda.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsOda.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor4);
		VistaOV.getValues().add(Valor3);
		
		CompleteOperationalView VistaOVMeta=new CompleteOperationalView(NameConstantsOda.META);

		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsOda.TYPE,NameConstantsOda.RESOURCE,VistaOV);
		VistaOVMeta.getValues().add(ValorMeta);
		
		AtributoMeta.getShows().add(VistaOVMeta);
		
		AtributoMeta.getShows().add(VistaOV);
	}
	
	/* (non-Javadoc)
	 * @see fdi.ucm.server.importparser.oda1.Oda1parserModel#ProcessAttributes()
	 */
	@Override
	public void ProcessAttributes() {
		{
		ID=new CompleteTextElementType(NameConstantsOda.IDNAME, AtributoMeta);
		AtributoMeta.getSons().add(ID);
		
		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsOda.PRESNTACION); 
		
		Valor=new CompleteOperationalValueType(NameConstantsOda.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsOda.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsOda.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		ID.getShows().add(VistaOV);
		
		CompleteOperationalView VistaOV2=new CompleteOperationalView(NameConstantsOda.META);
		 CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsOda.METATYPETYPE,NameConstantsOda.IGNORED,VistaOV2);
		 VistaOV2.getValues().add(Valor);
		 ID.getShows().add(VistaOV2);
		}

		
		
	}

	/* (non-Javadoc)
	 * @see fdi.ucm.server.importparser.oda1.Oda1parserModel#ProcessInstances()
	 */
	@Override
	public void ProcessInstances() {
		OwnInstances();

	}

	/**
	 * Procesa las instancias propias
	 */
	private void OwnInstances() {
		Ambitos=new HashMap<Integer, Integer>();
		OwnInstancesPropias();
		OwnInstancesURL();
		InstancesAjenas();
		
		atributes_Recursos();
		
		
		
	}
	
	
	/**
	 * 
	 */
	private void OwnInstancesURL() {
		try {
			ResultSet rs=LColec.getSQL().RunQuerrySELECT("SELECT * FROM resources where type='U' order by idov;");
			if (rs!=null) 
			{
				Integer preIdov=null;
				int MaxCount=0;
				int count=0;
				while (rs.next()) {
					
					String id=rs.getObject("id").toString();
					String idov=rs.getObject("idov").toString();
					
					String Visible="N";
					if(rs.getObject("visible")!=null)
						Visible=rs.getObject("visible").toString();
					
					String name="";
					if(rs.getObject("name")!=null)
						name=rs.getObject("name").toString();
					
					
					if (idov!=null&&!idov.isEmpty()&&!name.isEmpty())
						{
						Integer Idov=Integer.parseInt(idov);
						
						name=name.trim();
						name=StaticFunctionsOda.CleanStringFromDatabase(name,LColec);
						CompleteDocuments OVirtual=LColec.getCollection().getObjetoVirtual().get(Idov);
						
						
						if (OVirtual!=null)
						{
							
							Integer Base=Ambitos.get(Idov);
							
							if (Base==null)
							{
								Ambitos.put(Idov, 0);
								Base=0;
								}
							
							
						if (preIdov!=null&&preIdov.intValue()==Idov.intValue())
							count++;
						else
							{
							if (count>MaxCount)
								MaxCount=count;
							
							preIdov=Idov;
							count=0;
							}
						
						
						boolean Visiblebool=true;
						if (Visible.equals("N"))
							Visiblebool=false;
						

						
						CompleteTextElement E=new CompleteTextElement(ID, id);
						
//						LColec.getCollection().getFilesId().put(id,name);
						E.getAmbitos().add(Base);
						OVirtual.getDescription().add(E);
						
						Long Idl=Long.parseLong(id);
						idsOV.add(Idl);
						
						
						CompleteResourceElementURL E3=new CompleteResourceElementURL((CompleteResourceElementType)AtributoMeta,name);
						E3.getAmbitos().add(Base);
						OVirtual.getDescription().add(E3);			
					
							CompleteOperationalValue Valor=new CompleteOperationalValue(Valor2,Boolean.toString(Visiblebool));

							E3.getShows().add(Valor);
						
						
						Integer Id=Integer.parseInt(id);
						AmbitosResource.put(Id, Base);

						Ambitos.put(Idov, Base+1);
						}
						else {
							if (OVirtual==null)
								LColec.getLog().add("Warning: Objeto Virtual al que se asocia este recurso no existe, Id de objeto virtual : '" +Idov + "', Idrecurso: '"+id+ "'(ignorado)");
							
						}
						}
					else {
						if (idov==null||idov.isEmpty())
							LColec.getLog().add("Warning: Objeto Virtual asociado al que se asocia el recurso es vacio, Idrecurso: '"+id+"' es vacio o el file referencia asociado es vacio (ignorado)");
						if (name==null||name.isEmpty())
							LColec.getLog().add("Warning: Nombre recurso URL propio asociado vacio, Idrecurso: '"+id+"' (ignorado)");

					}
				}
			IteradorPadre.setAmbitoSTotales(MaxCount);
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private void atributes_Recursos() {
		try {
			ResultSet rs=LColec.getSQL().RunQuerrySELECT("SELECT * FROM section_data where idpadre=3 order by orden;");
			if (rs!=null) 
			{
				while (rs.next()) {
					String id=rs.getObject("id").toString();
					
					String nombre=rs.getObject("nombre").toString();
					
					String navegable="N";
					if(rs.getObject("browseable")!=null)
						navegable=rs.getObject("browseable").toString();
					
					String visible="N";
					if(rs.getObject("visible")!=null)
						visible=rs.getObject("visible").toString();
					
					String tipo_valores=null;
					if(rs.getObject("tipo_valores")!=null)
						tipo_valores=rs.getObject("tipo_valores").toString();
					
					String vocabulario=null;
					if(rs.getObject("vocabulario")!=null)
						vocabulario=rs.getObject("vocabulario").toString();
					
					if (nombre!=null&&!nombre.isEmpty()&&tipo_valores!=null&&!tipo_valores.isEmpty()&&((tipo_valores.equals("C")&&vocabulario!=null)||(!(tipo_valores.equals("C")))))
						{
						
						nombre=nombre.trim();
						nombre = StaticFunctionsOda.CleanStringFromDatabase(nombre,LColec);
						
						ElementType_NODE Nodo=new ElementType_NODE(id,nombre,navegable,visible,tipo_valores,vocabulario,AtributoMeta,false,LColec,idsOV);
						Nodo.ProcessAttributes();
						Nodo.ProcessInstances();
						AtributoMeta.getSons().add(Nodo.getAtributoMeta());
						}
					else
						{
						if (tipo_valores==null||tipo_valores.isEmpty())
							LColec.getLog().add("Warning: Tipo de valores vacio o nulo, id estructura: '"+id+"', estuctura padre : '3' (ignorado)");
						if (nombre==null||nombre.isEmpty())
							LColec.getLog().add("Warning: Nombre de la estructura del recurso vacia, id estructura: '"+id+"', padre : '3' (ignorado)");
						if ((tipo_valores.equals("C")&&vocabulario==null))
							LColec.getLog().add("Warning: Tipo de estructura controlado pero valor de vocabulario vacio, id estructura: '"+id+"', padre : '3' (ignorado)");
							
						}
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		
	}

	

	private void InstancesAjenas() {
		try {
			ResultSet rs=LColec.getSQL().RunQuerrySELECT("SELECT * FROM resources where type='F' order by idov;");
			if (rs!=null) 
			{
				Integer preIdov=null;
				int MaxCount=0;
				int count=0;
				while (rs.next()) {
					
					String id=rs.getObject("id").toString();
					String idov=rs.getObject("idov").toString();
					
					String Visible="N";
					if(rs.getObject("visible")!=null)
						Visible=rs.getObject("visible").toString();
					
					String idovreferedFile="";
					if(rs.getObject("idov_refered")!=null)
						idovreferedFile=rs.getObject("idov_refered").toString();
					
					String idreferedFile="";
					if(rs.getObject("idresource_refered")!=null)
						idreferedFile=rs.getObject("idresource_refered").toString();
					
					String name="";
					if(rs.getObject("name")!=null)
						name=rs.getObject("name").toString();
					
					
					if (idov!=null&&!idov.isEmpty()&&!name.isEmpty()&&(!idovreferedFile.isEmpty()||!idreferedFile.isEmpty()))
						{
						Integer Idov=Integer.parseInt(idov);
						
						name=name.trim();
						name=StaticFunctionsOda.CleanStringFromDatabase(name,LColec);
						
						CompleteDocuments OVirtual=LColec.getCollection().getObjetoVirtual().get(Idov);
						
						
StringBuffer SB=new StringBuffer();
						
						if (LColec.getBaseURLOda().isEmpty()||
								(!LColec.getBaseURLOda().startsWith("http://")
										&&!LColec.getBaseURLOda().startsWith("https://")
										&&!LColec.getBaseURLOda().startsWith("ftp://")))
							SB.append("http://");
						
						SB.append(LColec.getBaseURLOda());
						if (!LColec.getBaseURLOda().isEmpty()&&!LColec.getBaseURLOda().endsWith("//"))
							SB.append("/");
						SB.append(idov+"/"+name);
						
						String Path=SB.toString();
						
						CompleteFile FileC=LColec.getCollection().getFiles().get(Path);
						
						if (FileC==null) 
							{
							FileC=new CompleteFile(Path, LColec.getCollection().getCollection());
							LColec.getCollection().getFiles().put(Path, FileC);
							LColec.getCollection().getCollection().getSectionValues().add(FileC);
							}
						
						if (OVirtual!=null)
						{
							
						
							Integer Base=Ambitos.get(Idov);
							
							if (Base==null)
							{
								Ambitos.put(Idov, 0);
								Base=0;
								}	
							
							
						if (preIdov!=null&&preIdov.intValue()==Idov.intValue())
							count++;
						else
							{
							if (count>MaxCount)
								MaxCount=count;
							
							preIdov=Idov;
							count=0;
							}
						
						
						boolean Visiblebool=true;
						if (Visible.equals("N"))
							Visiblebool=false;
						
						
						CompleteTextElement E=new CompleteTextElement(ID, id);
						E.getAmbitos().add(Base);
						OVirtual.getDescription().add(E);
						
						
						
						CompleteResourceElementFile E3=new CompleteResourceElementFile((CompleteResourceElementType)AtributoMeta,FileC);
						
						Long Idl=Long.parseLong(id);
						idsOV.add(Idl);
			
						
						E3.getAmbitos().add(Base);
						OVirtual.getDescription().add(E3);
//						
						Integer Id=Integer.parseInt(id);
						AmbitosResource.put(Id, Base);
						
						Ambitos.put(Idov, Base+1);
//						MetaBooleanValue E2=new MetaBooleanValue(VISIBLE, Visiblebool);
//						E2.getAmbitos().add(count);
//						OVirtual.getDescription().add(E2);

							CompleteOperationalValue Valor=new CompleteOperationalValue(Valor2,Boolean.toString(Visiblebool));

							E3.getShows().add(Valor);
					
						}
						else{
							if (OVirtual==null)
								LColec.getLog().add("Warning: Objeto Virtual al que se asocia este recurso no existe, Id de objeto virtual : '" +Idov + "', Idrecurso: '"+id+ "'(ignorado)");
							
						}
						}
					else {
						if (idov==null||idov.isEmpty())
							LColec.getLog().add("Warning: Objeto Virtual asociado al que se asocia el recurso es vacio, Idrecurso: '"+id+"' es vacio o el file referencia asociado es vacio (ignorado)");
						if (name==null||name.isEmpty())
							LColec.getLog().add("Warning: Nombre recurso referencia asociado vacio, Idrecurso: '"+id+"' (ignorado)");
						if (idovreferedFile==null||idovreferedFile.isEmpty())
							LColec.getLog().add("Warning: Nombre objeto virtual referencia vacio, Idrecurso: '"+id+"' (ignorado)");
					}
				}
			IteradorPadre.setAmbitoSTotales(MaxCount);
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Procesa las instancias que corresponden a mis elementos
	 */
	private void OwnInstancesPropias() {
			try {
				ResultSet rs=LColec.getSQL().RunQuerrySELECT("SELECT * FROM resources where type='P' order by idov;");
				if (rs!=null) 
				{
					Integer preIdov=null;
					int MaxCount=0;
					int count=0;
					while (rs.next()) {
						
						String id=rs.getObject("id").toString();
						String idov=rs.getObject("idov").toString();
						
						String Visible="N";
						if(rs.getObject("visible")!=null)
							Visible=rs.getObject("visible").toString();
						
						String iconoOV="N";
						if(rs.getObject("iconoov")!=null)
							iconoOV=rs.getObject("iconoov").toString();
						
						String name="";
						if(rs.getObject("name")!=null)
							name=rs.getObject("name").toString();
						
						
						if (idov!=null&&!idov.isEmpty()&&!name.isEmpty())
							{
							Integer Idov=Integer.parseInt(idov);
							
							name=name.trim();
							name=StaticFunctionsOda.CleanStringFromDatabase(name,LColec);
							CompleteDocuments OVirtual=LColec.getCollection().getObjetoVirtual().get(Idov);
//							CompleteFile FileActual=LColec.getCollection().getFiles().get(Idov+"/"+name);
							StringBuffer SB=new StringBuffer();
							
							if (LColec.getBaseURLOda().isEmpty()||
									(!LColec.getBaseURLOda().startsWith("http://")
											&&!LColec.getBaseURLOda().startsWith("https://")
											&&!LColec.getBaseURLOda().startsWith("ftp://")))
								SB.append("http://");
							
							SB.append(LColec.getBaseURLOda());
							if (!LColec.getBaseURLOda().isEmpty()&&!LColec.getBaseURLOda().endsWith("//"))
								SB.append("/");
							SB.append(idov+"/"+name);
							
							String Path=SB.toString();
							
							CompleteFile FileC=LColec.getCollection().getFiles().get(Path);
							
							if (FileC==null) 
							{
							FileC=new CompleteFile(Path, LColec.getCollection().getCollection());
							LColec.getCollection().getFiles().put(Path, FileC);
							LColec.getCollection().getCollection().getSectionValues().add(FileC);
							}
							
							if (OVirtual!=null)
							{
								
								Integer Base=Ambitos.get(Idov);
								
								if (Base==null)
								{
									Ambitos.put(Idov, 0);
									Base=0;
									}
								
								
							if (preIdov!=null&&preIdov.intValue()==Idov.intValue())
								count++;
							else
								{
								if (count>MaxCount)
									MaxCount=count;
								
								preIdov=Idov;
								count=0;
								}
							
							
							boolean Visiblebool=true;
							if (Visible.equals("N"))
								Visiblebool=false;
							
							Long Idl=Long.parseLong(id);
							idsOV.add(Idl);
							
							CompleteTextElement E=new CompleteTextElement(ID, id);
							
							E.getAmbitos().add(Base);
							OVirtual.getDescription().add(E);
							
							
							
							CompleteResourceElementFile E3=new CompleteResourceElementFile((CompleteResourceElementType)AtributoMeta,FileC);
							E3.getAmbitos().add(Base);
							OVirtual.getDescription().add(E3);			
						

								CompleteOperationalValue Valor=new CompleteOperationalValue(Valor2,Boolean.toString(Visiblebool));

								E3.getShows().add(Valor);
							
							Integer Id=Integer.parseInt(id);
							AmbitosResource.put(Id, Base);
							
							if (iconoOV.equals("S"))
								OVirtual.setIcon(FileC.getPath());

							Ambitos.put(Idov, Base+1);
							}
							else {
								if (OVirtual==null)
									LColec.getLog().add("Warning: Objeto Virtual al que se asocia este recurso no existe, Id de objeto virtual : '" +Idov + "', Idrecurso: '"+id+ "'(ignorado)");

							}
							}
						else {
							//En la creacion de los objetos archivos aparece esta captura de errores
//							if (idov==null||idov.isEmpty())
//								LColec.getLog().add("Warning: Objeto Virtual asociado al que se asocia el recurso es vacio, Idrecurso: '"+id+"' es vacio o el file referencia asociado es vacio (ignorado)");
//							if (name==null||name.isEmpty())
//								LColec.getLog().add("Warning: Nombre recurso propio asociado vacio, Idrecurso: '"+id+"' (ignorado)");

						}
					}
				IteradorPadre.setAmbitoSTotales(MaxCount);
				rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
	}

	/**
	 * @return the atributoMeta
	 */
	public CompleteElementType getAtributoMeta() {
		return AtributoMeta;
	}

	/**
	 * @param atributoMeta the atributoMeta to set
	 */
	public void setAtributoMeta(CompleteResourceElementType atributoMeta) {
		AtributoMeta = atributoMeta;
	}
	
	public static HashMap<Integer, Integer> getAmbitosResource() {
		return AmbitosResource;
	}

}
