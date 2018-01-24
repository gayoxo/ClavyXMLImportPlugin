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
import fdi.ucm.server.importparser.oda.coleccion.categoria.ElementType_NODE;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalView;

/**
 * Clase que define la carga de los datos
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Datos implements InterfaceOdaparser{

	
	private CompleteElementType AtributoMeta;
	private LoadCollectionOda LColec;
	private CompleteGrammar GPadre;


	public ElementType_Datos(CompleteGrammar Padre,LoadCollectionOda L) {

		AtributoMeta=new CompleteElementType(NameConstantsOda.DATOS, Padre);
		LColec=L;
		GPadre=Padre;
		
		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsOda.PRESNTACION); 
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsOda.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsOda.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsOda.SUMMARYSHOWN,Boolean.toString(false),VistaOV);

		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		
		CompleteOperationalView VistaOVMeta=new CompleteOperationalView(NameConstantsOda.META);
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsOda.TYPE,NameConstantsOda.DATOS,VistaOVMeta);
		VistaOVMeta.getValues().add(ValorMeta);
		AtributoMeta.getShows().add(VistaOVMeta);
		
		AtributoMeta.getShows().add(VistaOV);
	}

	@Override
	public void ProcessAttributes() {
		try {
			ResultSet rs=LColec.getSQL().RunQuerrySELECT("SELECT * FROM section_data where idpadre=1 order by orden;");
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
					
					String tipo_valores=rs.getObject("tipo_valores").toString();
					
					String vocabulario=null;
					if(rs.getObject("vocabulario")!=null)
						vocabulario=rs.getObject("vocabulario").toString();
					
					String extensible="N";
					if(rs.getObject("extensible")!=null)
						extensible=rs.getObject("extensible").toString();
					
					if (id!=null&&!id.isEmpty()&&nombre!=null&&!nombre.isEmpty()&&tipo_valores!=null&&!tipo_valores.isEmpty()
//							&&((tipo_valores.equals("C")&&vocabulario!=null)||(!(tipo_valores.equals("C"))))
							)
						{
						
						nombre=nombre.trim();
						nombre = StaticFunctionsOda.CleanStringFromDatabase(nombre,LColec);
						
//						boolean Summary=false;
//						if (nombre.equals(NameConstantsOda1.DESCRIPTIONNAME))
//							Summary=true;
						
						boolean Extensible=false;
						if (extensible.equals("S"))
							Extensible=true;
						
						if (id.equals(NameConstantsOda.IDDESCRIPTIONNAME))
						{
							ProcessDescripcion(id);
							if (!nombre.isEmpty())
							{
							CompleteOperationalView VistaOVMeta=new CompleteOperationalView(NameConstantsOda.META);
							CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsOda.DESCRIPTIONNAME,nombre,VistaOVMeta);
							VistaOVMeta.getValues().add(ValorMeta);
							GPadre.getViews().add(VistaOVMeta);
							}
							
						}
						else
						{
						ElementType_NODE Nodo=new ElementType_NODE(id,nombre,navegable,visible,tipo_valores,vocabulario,AtributoMeta,false,LColec);
						Nodo.ProcessAttributes();
						Nodo.ProcessInstances();
						
						
						
						CompleteOperationalView VistaOVOda=new CompleteOperationalView(NameConstantsOda.ODA);
						Nodo.getAtributoMeta().getShows().add(VistaOVOda);
						CompleteOperationalValueType ValorOda=new CompleteOperationalValueType(NameConstantsOda.EXTENSIBLE,Boolean.toString(Extensible),VistaOVOda);
						VistaOVOda.getValues().add(ValorOda);
						
						AtributoMeta.getSons().add(Nodo.getAtributoMeta());
						}

						}
					else
					{
					if (tipo_valores==null||tipo_valores.isEmpty())
						LColec.getLog().add("Warning: Tipo de valores vacio o nulo, id estructura: '"+id+"', estuctura padre : '1' (ignorado)");
					if (nombre==null||nombre.isEmpty())
						LColec.getLog().add("Warning: Nombre de la estructura del recurso vacia, id estructura: '"+id+"', padre : '1' (ignorado)");
					if ((tipo_valores.equals("C")&&vocabulario==null))
						LColec.getLog().add("Warning: Tipo de estructura controlado pero valor de vocabulario vacio, id estructura: '"+id+"', padre : '1' (ignorado)");
						
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

	/**
	 * Metodo que procesa las descripciones permitiendo unirlas a los Documentos
	 * @param id
	 */
	private void ProcessDescripcion(String id) {
		try {
			ResultSet rs=LColec.getSQL().RunQuerrySELECT("SELECT * FROM text_data where idseccion="+id+";");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String id1=rs.getObject("id").toString();
					
					String idov=rs.getObject("idov").toString();
					
					String value="";
					if(rs.getObject("value")!=null)
						value=rs.getObject("value").toString();
					
					
					
					if (idov!=null&&!idov.isEmpty()&&!value.isEmpty())
						{
						
						
						value=value.trim();
						String valueclean = StaticFunctionsOda.CleanStringFromDatabase(value,LColec);
						
						
						int Idov=Integer.parseInt(idov);
						
						try {
							CompleteDocuments C=LColec.getCollection().getObjetoVirtual().get(Idov);
							C.setDescriptionText(valueclean);
						} catch (Exception e) {
							LColec.getLog().add("Warning: Descripcion del objeto virtual asociada a un Objeto virtual vacio o nulo, id en text_data: '"+id1+"' (ignorado) con valor '"+ valueclean +"'" );
						}
						
						
						}
					else
					{
						if (idov==null||idov.isEmpty())
							LColec.getLog().add("Warning: Descripcion del objeto virtual asociada a un Objeto virtual vacio o nulo, id en text_data: '"+id1+"' (ignorado)");
						if (value==null||value.isEmpty())
							LColec.getLog().add("Warning: Texto descripcion asociado a el objeto virtual vacio o nulo: id en text_data: '"+id1+"' (ignorado)");
						}
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
	}

	@Override
	public void ProcessInstances() {
		// No tiene Instancias
		
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
	public void setAtributoMeta(CompleteElementType atributoMeta) {
		AtributoMeta = atributoMeta;
	}
	
	
	
	
	
}
