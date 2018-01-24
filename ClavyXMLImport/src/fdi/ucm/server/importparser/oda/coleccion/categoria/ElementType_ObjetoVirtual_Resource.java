/**
 * 
 */
package fdi.ucm.server.importparser.oda.coleccion.categoria;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import fdi.ucm.server.importparser.oda.InterfaceOdaparser;
import fdi.ucm.server.importparser.oda.NameConstantsOda;
import fdi.ucm.server.importparser.oda.StaticFunctionsOda;
import fdi.ucm.server.importparser.oda.coleccion.LoadCollectionOda;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteFile;
import fdi.ucm.server.modelComplete.collection.document.CompleteLinkElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteOperationalValue;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteIterator;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteLinkElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalView;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * Implementa el atributo de los recursos en los Objetos Virtuales
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_ObjetoVirtual_Resource implements InterfaceOdaparser {

	protected CompleteElementType AtributoMeta;
	protected CompleteTextElementType ID;
	protected CompleteIterator IteradorPadre;
	protected CompleteOperationalValueType Valor;
	protected CompleteOperationalValueType Valor2;
	protected LoadCollectionOda LColec;
	protected HashMap<Integer, Integer> Ambitos;
	protected static HashMap<Integer, Integer> AmbitosResource;
	
	
	public ElementType_ObjetoVirtual_Resource() {
		if (AmbitosResource==null)
			AmbitosResource=new HashMap<Integer, Integer>();
	}
	
	public ElementType_ObjetoVirtual_Resource(CompleteIterator I,LoadCollectionOda L) {
		if (AmbitosResource==null)
			AmbitosResource=new HashMap<Integer, Integer>();
		IteradorPadre=I;
		AtributoMeta=new CompleteLinkElementType(NameConstantsOda.RESOURCENAME,I);
		LColec=L;
		
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
		OVInstances();
		
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
						CompleteDocuments FileC=LColec.getCollection().getURLC().get(name);
						
						if (OVirtual!=null&&FileC!=null)
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
						
						LColec.getCollection().getFilesId().put(id,FileC);
						E.getAmbitos().add(Base);
						OVirtual.getDescription().add(E);
						
						
						
						CompleteLinkElement E3=new CompleteLinkElement((CompleteLinkElementType)AtributoMeta,FileC);
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
							if (FileC==null)
								LColec.getLog().add("Warning: El recurso propio URL al que apunta con referencia objeto : '" + name +"', no existe, Idrecurso: '"+id+ "'(ignorado)");	
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
						
						ElementType_NODE Nodo=new ElementType_NODE(id,nombre,navegable,visible,tipo_valores,vocabulario,AtributoMeta,false,LColec);
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

	private void OVInstances() {
		try {
			ResultSet rs=LColec.getSQL().RunQuerrySELECT("SELECT * FROM resources where type='OV' order by idov;");
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
					
					String idovrefered="";
					if(rs.getObject("idov_refered")!=null)
						idovrefered=rs.getObject("idov_refered").toString();
					

					
					
					if (idov!=null&&!idov.isEmpty()&&!idovrefered.isEmpty())
						{
						Integer Idov=Integer.parseInt(idov);
						Integer idovrefered2=Integer.parseInt(idovrefered);
						
						
						
						CompleteDocuments OVirtual=LColec.getCollection().getObjetoVirtual().get(Idov);
						CompleteDocuments OVirtualRef=LColec.getCollection().getObjetoVirtual().get(idovrefered2);
						
						if (OVirtual!=null&&OVirtualRef!=null)
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
						
						
						boolean Visiblebool=false;
						if (Visible.equals("S"))
							Visiblebool=true;

						{

						CompleteTextElement E=new CompleteTextElement(ID, id);
						E.getAmbitos().add(Base);
						OVirtual.getDescription().add(E);

						CompleteOperationalValue Valor=new CompleteOperationalValue(this.Valor,Boolean.toString(Visiblebool));
						E.getShows().add(Valor);
						}
						
						{
							CompleteLinkElement E3=new CompleteLinkElement((CompleteLinkElementType)AtributoMeta,OVirtualRef);
						E3.getAmbitos().add(Base);
						OVirtual.getDescription().add(E3);
						
						Integer Id=Integer.parseInt(id);
						AmbitosResource.put(Id, Base);

						CompleteOperationalValue Valor=new CompleteOperationalValue(Valor2,Boolean.toString(Visiblebool));

						E3.getShows().add(Valor);
						
						
						}

						Ambitos.put(Idov, Base+1);	
						
						}
					else 
					{
						if (OVirtual==null)
							LColec.getLog().add("Warning: Objeto Virtual al que se asocia este recurso no existe, Id de objeto virtual : '" +Idov + "', Idrecurso: '"+id+ "'(ignorado)");
						if (OVirtualRef==null)
							LColec.getLog().add("Warning: El Objeto Virtal referencia al que apunta : Referencia : '" + idovrefered + "' no existe, Idrecurso: '"+id+ "'(ignorado)");
					}
						}
					else {
						if (idov==null||idov.isEmpty())
							LColec.getLog().add("Warning: Objeto Virtual asociado al que se asocia el recurso es vacio, Idrecurso: '"+id+"' es vacio o el file referencia asociado es vacio (ignorado)");
						if (idovrefered==null||idovrefered.isEmpty())
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
						CompleteDocuments FileC=LColec.getCollection().getFilesId().get(idreferedFile);
						if (FileC==null) 
							{
							FileC=LColec.getCollection().getFilesC().get(idovreferedFile+"/"+name);
							if (FileC!=null)
								LColec.getLog().add("Warning: El recurso referencia al que apunta con referencia al objeto: Referencia : '" + idreferedFile + "' y objeto : '" + name +"', no existe, Idrecurso: '"+id+ "' REPARADO a traves de su objeto virtual '" + idovreferedFile + "'");
							}
						
						if (OVirtual!=null&&FileC!=null)
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
						
						LColec.getCollection().getFilesId().put(id,FileC);
						
						
						CompleteLinkElement E3=new CompleteLinkElement((CompleteLinkElementType)AtributoMeta,FileC);
						
						
			
						
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
							if (FileC==null)
								LColec.getLog().add("Warning: El recurso referencia al que apunta con referencia al objeto: ReferenciaOV : '" + idovreferedFile + "' Referencia Recurso : '" + idreferedFile + "' y objeto : '" + name +"', no existe, Idrecurso: '"+id+ "'(ignorado)");
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
							CompleteFile FileActual=LColec.getCollection().getFiles().get(Idov+"/"+name);
							CompleteDocuments FileC=LColec.getCollection().getFilesC().get(Idov+"/"+name);
							
							if (OVirtual!=null&&FileC!=null)
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
							
							LColec.getCollection().getFilesId().put(id,FileC);
							E.getAmbitos().add(Base);
							OVirtual.getDescription().add(E);
							
							
							
							CompleteLinkElement E3=new CompleteLinkElement((CompleteLinkElementType)AtributoMeta,FileC);
							E3.getAmbitos().add(Base);
							OVirtual.getDescription().add(E3);			
						

								CompleteOperationalValue Valor=new CompleteOperationalValue(Valor2,Boolean.toString(Visiblebool));

								E3.getShows().add(Valor);
							

							CompleteLinkElement FileValue=new CompleteLinkElement(Grammar_File.getOWNER(),OVirtual);
							
							FileC.getDescription().add(FileValue);
							
							Integer Id=Integer.parseInt(id);
							AmbitosResource.put(Id, Base);
							
							if (iconoOV.equals("S"))
								OVirtual.setIcon(FileActual.getPath());

							Ambitos.put(Idov, Base+1);
							}
							else {
								if (OVirtual==null)
									LColec.getLog().add("Warning: Objeto Virtual al que se asocia este recurso no existe, Id de objeto virtual : '" +Idov + "', Idrecurso: '"+id+ "'(ignorado)");
								if (FileC==null)
									LColec.getLog().add("Warning: El recurso propio al que apunta con referencia objeto : '" + name +"', no existe, Idrecurso: '"+id+ "'(ignorado)");	
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
	public void setAtributoMeta(CompleteLinkElementType atributoMeta) {
		AtributoMeta = atributoMeta;
	}
	
	public static HashMap<Integer, Integer> getAmbitosResource() {
		return AmbitosResource;
	}

}
