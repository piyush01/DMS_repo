package org.dspace.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class UpdateInputFormData 
{
	private static final Logger log=LoggerFactory.getLogger(UpdateInputFormData.class);
	public static final String inputformXml = "D:\\CBSL\\DMS_5.4\\config\\input-forms.xml";
	public static final String dspacecfg = "D:\\CBSL\\DMS_5.4\\config\\dspace.cfg";
	public static final String discoveryxml="D:\\CBSL\\DMS_5.4\\config\\spring\\api\\discovery.xml";
	public static final String messagesproperties = "D:\\eZeeFile\\DMS-5.4\\dspace-api\\src\\main\\resources\\Messages.properties"; 
	public static final String messagesproperties1 ="D:\\CBSL\\DMS_5.4\\config\\Messages.properties";
	static FileInputStream inputStream;
	static Set<Object> keys;
	
	public static void Add_FormMapElement(String collection_handle,String form_name)
	{
		try {
			 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	            Document document = documentBuilder.parse(inputformXml); 
	            log.info("File path=============>"+inputformXml);
	            log.info("Collection hendel====$$$$$$$$=====>"+collection_handle);
	            log.info("Form name   ====$$$$$$$$=====>"+form_name);
	            // Get form_map Element..............
	            Node form_map = document.getElementsByTagName("form-map").item(0);
	            log.info("Form_map=========>"+form_map);
	            /*Node page = document.getElementsByTagName("page").item(0);
	            Node form_definitions=document.getElementsByTagName("form-definitions").item(0);
	            Node form1=document.getElementsByTagName("form").item(0);
	            Node name_map = document.getElementsByTagName("name-map").item(0);
	            NamedNodeMap attribute = name_map.getAttributes();*/
				    // List<String> list=new ArrayList<String>();
					int j=-1;
					 String[] list=getMapAttribute();
					//System.out.println("array size====>"+list.length);
				      for(int i=0;i<list.length;i++)
				      {
				    	 
				    	if(list[i].equals(collection_handle))
				    	{
				    		
				    		j=i;
				    		break;
				    	}
				      }
				      if(j!=-1)
				      {
				    	  log.info("the namp-map Element are Allready exists in form-map element..");
				      }
				      else
				      {
				    	  
				    	// Create Name_map Element............
				            Element namemap = document.createElement("name-map");
							form_map.appendChild(namemap);
							// Create name_map Attribute...............
							Attr attr=document.createAttribute("collection-handle");
							Attr attr1=document.createAttribute("form-name");
							//Set Attribute value............
							attr.setValue(collection_handle);
							attr1.setValue(form_name);
							namemap.setAttributeNode(attr);
							namemap.setAttributeNode(attr1);   
							log.info("the namp-map Element are Created in form-map element..");
							
				      }
				
				      // write the DOM object to the file.............................
						TransformerFactory transformerFactory = TransformerFactory.newInstance();
		 	            Transformer transformer = transformerFactory.newTransformer(); 
		 	            DOMSource domSource = new DOMSource(document);
		 	            StreamResult streamResult = new StreamResult(new File(inputformXml));
		  	            transformer.transform(domSource, streamResult);
	        
		}catch (ParserConfigurationException pce) {

            pce.printStackTrace();

        } catch (TransformerException tfe) {

            tfe.printStackTrace();

        } catch (IOException ioe) {

            ioe.printStackTrace();

        } catch (SAXException sae) {

            sae.printStackTrace();

        }

	}
	public static void Add_FormFieldElement(String form_name,String dcSchema,String dcElement,String dcQualifier, String label,String input_type,String required) throws XPathExpressionException
	{
		
		try {
			 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	            Document document = documentBuilder.parse(inputformXml); 
	            log.info("File path=============>"+inputformXml);
	            log.info("Dc Schema====$$$$$$$$=====>"+dcSchema);
	            log.info("Form name   ====$$$$$$$$=====>"+form_name);
	            // Get form_map Element..............
	            Node form_map = document.getElementsByTagName("form-map").item(0);
	            Node form_definitions=document.getElementsByTagName("form-definitions").item(0);
	            Node name_map = document.getElementsByTagName("name-map").item(0);
	            NamedNodeMap attribute = name_map.getAttributes();
				    // List<String> list=new ArrayList<String>();
			         int c=-1,k = 0;
			 		String[] pagenumberlist=getpageAttributeform(form_name);
			 		String[] formlist=getFormAttribute();		
			 		 for(int i=0;i<formlist.length;i++)
				      {
				    	 if(formlist[i].equals(form_name))
				    	 {
				    		 for(int j=0;j<pagenumberlist.length;j++)
				    		 {
				    			 if(pagenumberlist[j].equals("1"))
				    			 {
				    		 	  c=Arrays.asList(formlist).indexOf(form_name);
				    			 }
				    		 }
				    	 }
				   
							
				      }
			 	
				      if(c!=-1)
				      {
				    	  log.info("value of c===========>"+c);
				    	///page 
				    	  Node page1=document.getElementsByTagName("page").item((c));
							
							// add Field Element
							Element field = document.createElement("field");
							page1.appendChild(field);
							
							// add other fields...........
							Element dc_schema = document.createElement("dc-schema");
							dc_schema.appendChild(document.createTextNode(dcSchema));
							field.appendChild(dc_schema);
							
							Element dc_element = document.createElement("dc-element");
							dc_element.appendChild(document.createTextNode(dcElement));
							field.appendChild(dc_element);
							if(dcQualifier==null)
							{
								Element dc_qualifier = document.createElement("dc-qualifier");
								dc_qualifier.appendChild(document.createTextNode(""));
								field.appendChild(dc_qualifier);
							}else
							{
							Element dc_qualifier = document.createElement("dc-qualifier");
							dc_qualifier.appendChild(document.createTextNode(dcQualifier));
							field.appendChild(dc_qualifier);
							}
							Element dc_label = document.createElement("label");
							dc_label.appendChild(document.createTextNode(label));
							field.appendChild(dc_label);
							
							if(input_type.equals("dropdown"))
							{
								Element dc_input_type=document.createElement("input-type");
								dc_input_type.appendChild(document.createTextNode(input_type));
								field.appendChild(dc_input_type);
								Attr attr=document.createAttribute("value-pairs-name");
								attr.setValue(label);
								dc_input_type.setAttributeNode(attr);
								
							}else{
							Element dc_input_type=document.createElement("input-type");
							dc_input_type.appendChild(document.createTextNode(input_type));
							field.appendChild(dc_input_type);
							}
							if(required.equals("yes"))
							{
								Element dc_required=document.createElement("required");
								dc_required.appendChild(document.createTextNode("Please Enter the value."));
								field.appendChild(dc_required);
							}
							else{
							
							}
				      }
				      else
				      {
				    	  
				    	  Element form = document.createElement("form");
							form_definitions.appendChild(form);
							Attr formattr=document.createAttribute("name");
							
							formattr.setValue(form_name);
							
							form.setAttributeNode(formattr);
				    	///page 
							
							Element page1 = document.createElement("page");
							form.appendChild(page1);
							Attr pageattr=document.createAttribute("number");
							pageattr.setValue("1");
							page1.setAttributeNode(pageattr);
							// add Field Element
							Element field = document.createElement("field");
							page1.appendChild(field);
							
							// add other fields...........
							Element dc_schema = document.createElement("dc-schema");
							dc_schema.appendChild(document.createTextNode(dcSchema));
							field.appendChild(dc_schema);
							
							Element dc_element = document.createElement("dc-element");
							dc_element.appendChild(document.createTextNode(dcElement));
							field.appendChild(dc_element);
							if(dcQualifier==null)
							{
								Element dc_qualifier = document.createElement("dc-qualifier");
								dc_qualifier.appendChild(document.createTextNode(""));
								field.appendChild(dc_qualifier);
							}else
							{
							Element dc_qualifier = document.createElement("dc-qualifier");
							dc_qualifier.appendChild(document.createTextNode(dcQualifier));
							field.appendChild(dc_qualifier);
							}
						
							
							Element dc_label = document.createElement("label");
							dc_label.appendChild(document.createTextNode(label));
							field.appendChild(dc_label);
							
							if(input_type.equals("dropdown"))
							{
								Element dc_input_type=document.createElement("input-type");
								dc_input_type.appendChild(document.createTextNode(input_type));
								field.appendChild(dc_input_type);
								Attr attr=document.createAttribute("value-pairs-name");
								attr.setValue(label);
								dc_input_type.setAttributeNode(attr);
								
							}else{
							Element dc_input_type=document.createElement("input-type");
							dc_input_type.appendChild(document.createTextNode(input_type));
							field.appendChild(dc_input_type);
							}
						if(required==null)
						{
							Element dc_required=document.createElement("required");
							dc_required.appendChild(document.createTextNode("Please Enter the value."));
							field.appendChild(dc_required);
						}else{
							
						}
				      }
				
				 // write the DOM object to the file.............................
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
 	            Transformer transformer = transformerFactory.newTransformer(); 
 	            DOMSource domSource = new DOMSource(document);
 	            StreamResult streamResult = new StreamResult(new File(inputformXml));
  	            transformer.transform(domSource, streamResult);
	           
		}catch (ParserConfigurationException pce) {

            pce.printStackTrace();

        } catch (TransformerException tfe) {

            tfe.printStackTrace();

        } catch (IOException ioe) {

            ioe.printStackTrace();

        } catch (SAXException sae) {

            sae.printStackTrace();

        }
	}
	
	
	public static void createBeanInDiscovery(String element,String data)
	{
		try {
			 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	            Document document = documentBuilder.parse(discoveryxml); 
	            // Get form_map Element..........
	            Node beans = document.getElementsByTagName("beans").item(0);
	            Element bean = document.createElement("bean");
	            beans.appendChild(bean);
	            Attr id=document.createAttribute("id");
	            Attr classatt=document.createAttribute("class");
				id.setValue("searchFilter"+element);
				classatt.setValue("org.dspace.discovery.configuration.DiscoverySearchFilterFacet");
				bean.setAttributeNode(id);
				bean.setAttributeNode(classatt);
				 Element property  = document.createElement("property");
				 bean.appendChild(property);
				 Attr name=document.createAttribute("name");
		            Attr value=document.createAttribute("value");
					name.setValue("indexFieldName");
					value.setValue(element);
					property.setAttributeNode(name);
					property.setAttributeNode(value);
					 Element property1  = document.createElement("property");
					 bean.appendChild(property1);
					 Attr name1=document.createAttribute("name");
					 name1.setValue("metadataFields");
					 property1.setAttributeNode(name1);
					 Element list=document.createElement("list");
					 property1.appendChild(list);
					 Element valueelement=document.createElement("value");
					 valueelement.appendChild(document.createTextNode(data));
					 list.appendChild(valueelement);
					 Element property2  = document.createElement("property");
					 bean.appendChild(property2);
					 Attr name2=document.createAttribute("name");
			            Attr value2=document.createAttribute("value");
						name2.setValue("facetLimit");
						value2.setValue("10");
						property2.setAttributeNode(name2);
						property2.setAttributeNode(value2);
					 Element property3  = document.createElement("property");
					 bean.appendChild(property3);
					 Attr name3=document.createAttribute("name");
			            Attr value3=document.createAttribute("value");
						name3.setValue("sortOrder");
						value3.setValue("COUNT");
						property3.setAttributeNode(name3);
						property3.setAttributeNode(value3);
				 // write the DOM object to the file.............................
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer(); 
	            DOMSource domSource = new DOMSource(document);
	            StreamResult streamResult = new StreamResult(new File(discoveryxml));
	            transformer.transform(domSource, streamResult);
		}catch (ParserConfigurationException pce) {

         pce.printStackTrace();

     } catch (TransformerException tfe) {

         tfe.printStackTrace();

     } catch (IOException ioe) {

         ioe.printStackTrace();

     } catch (SAXException sae) {

         sae.printStackTrace();

     }
		
	}
	

	public static void createsidebarFacetsdefaultConfiguration(String element) throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException, TransformerException
	{
		String homelist[]=getdefaultConfiguration();
		 int c=-1;
		 for(int i=0;i<homelist.length;i++)
		 {
			 if(homelist[i].equals("sidebarFacets"))
			 {
				 c=Arrays.asList(homelist).indexOf("sidebarFacets");
					break;
			 }
		 }
		 
		 if(c!=-1)
		 {

				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	            Document document = documentBuilder.parse(discoveryxml); 
	            Node list = document.getElementsByTagName("list").item(c);
	            Element ref = document.createElement("ref");
	            list.appendChild(ref);
	            Attr bean=document.createAttribute("bean");
	            bean.setValue("searchFilter"+element);
	            ref.setAttributeNode(bean);
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer(); 
	            DOMSource domSource = new DOMSource(document);
	            StreamResult streamResult = new StreamResult(new File(discoveryxml));
	            transformer.transform(domSource, streamResult);
	      
		 }
		 else
		 {
			log.info("ref not created...."); 
		 }
	}
	
	
	public static void createsearchFilterdefaultConfiguration(String element) throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException, TransformerException
	{
		String homelist[]=getdefaultConfiguration();
		 int c=-1;
		 for(int i=0;i<homelist.length;i++)
		 {
			 if(homelist[i].equals("searchFilters"))
			 {
				 c=Arrays.asList(homelist).indexOf("searchFilters");
					break;
			 }
		 }
		 
		 if(c!=-1)
		 {

				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	            Document document = documentBuilder.parse(discoveryxml); 
	            Node list = document.getElementsByTagName("list").item(c);
	            Element ref = document.createElement("ref");
	            list.appendChild(ref);
	            Attr bean=document.createAttribute("bean");
	            bean.setValue("searchFilter"+element);
	            ref.setAttributeNode(bean);
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer(); 
	            DOMSource domSource = new DOMSource(document);
	            StreamResult streamResult = new StreamResult(new File(discoveryxml));
	            transformer.transform(domSource, streamResult);
	      
		 }
		 else
		 {
			log.info("ref not created...."); 
		 }
		
	}
	
	
	public static void createsidebarFacetsHomeConfiguration(String element) throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException, TransformerException
	{
		String homelist[]=gethomepageConfiguration();
		 int c=-1;
		 for(int i=0;i<homelist.length;i++)
		 {
			 if(homelist[i].equals("sidebarFacets"))
			 {
				 c=Arrays.asList(homelist).indexOf("sidebarFacets");
					break;
			 }
		 }
		 
		 if(c!=-1)
		 {

				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	            Document document = documentBuilder.parse(discoveryxml); 
	            Node list = document.getElementsByTagName("list").item(c+6);
	            Element ref = document.createElement("ref");
	            list.appendChild(ref);
	            Attr bean=document.createAttribute("bean");
	            bean.setValue("searchFilter"+element);
	            ref.setAttributeNode(bean);
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer(); 
	            DOMSource domSource = new DOMSource(document);
	            StreamResult streamResult = new StreamResult(new File(discoveryxml));
	            transformer.transform(domSource, streamResult);
		 }
		 else
		 {
			log.info("ref not created...."); 
		 }
	}
	
	public static void createsearchFilterHomeConfiguration(String element) throws FileNotFoundException, XPathExpressionException, ParserConfigurationException, SAXException, IOException, TransformerException
	{
		String homelist[]=gethomepageConfiguration();
		 int c=-1;
		 for(int i=0;i<homelist.length;i++)
		 {
			 if(homelist[i].equals("searchFilters"))
			 {
				 c=Arrays.asList(homelist).indexOf("searchFilters");
					break;
			 }
		 }
		 
		 if(c!=-1)
		 {

				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	            Document document = documentBuilder.parse(discoveryxml); 
	            Node list = document.getElementsByTagName("list").item(c+7);
	            Element ref = document.createElement("ref");
	            list.appendChild(ref);
	            Attr bean=document.createAttribute("bean");
	            bean.setValue("searchFilter"+element);
	            ref.setAttributeNode(bean);
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer(); 
	            DOMSource domSource = new DOMSource(document);
	            StreamResult streamResult = new StreamResult(new File(discoveryxml));
	            transformer.transform(domSource, streamResult);
		 }
		 else
		 {
			log.info("ref not created...."); 
		 }
	}
	
	public static void UpdateMessageforsearchsidefacet(String element,String lavel) throws ConfigurationException
	{
		PropertiesConfiguration config=new PropertiesConfiguration(messagesproperties);
		 config.setProperty("jsp.search.facet.refine."+element,lavel);
	    config.save();

	}
	public static void UpdateMessageforsearch(String element,String lavel) throws ConfigurationException, IOException
	{
		GetPropertiesValues prop=new GetPropertiesValues();
		String key=prop.getsearchFilter(messagesproperties1,element);
		System.out.println(key);
		if(key!=null&&key.equals(element))
		{
			log.info("Great '%s' contains substring %n"+ lavel);
		}
		else
		{
			PropertiesConfiguration config=new PropertiesConfiguration(messagesproperties1);
			 config.setProperty("jsp.search.filter."+element,lavel);
		    config.save();
		}
	
		

	}
	
	public static void UpdateMessageforAdvancesearch(String element,String lavel) throws ConfigurationException, IOException
	{
		
		GetPropertiesValues prop=new GetPropertiesValues();
		String key=prop.getsearchAdvance(messagesproperties1,element);
		System.out.println(key);
		if(key!=null&&key.equals(element))
		{
			log.info("Great '%s' contains substring %n"+ lavel);
		}
		else
		{
			PropertiesConfiguration config=new PropertiesConfiguration(messagesproperties1);
			 config.setProperty("jsp.search.advanced.type."+element,lavel);
		    config.save();
		}
	
		
		

	}
	//====Update dspace file............
	public static void UpdateItemDisplayValue(String data) throws IOException, ConfigurationException
	{
	/*	GetPropertiesValues prop=new GetPropertiesValues();
		String webui_itemdisplay_default=prop.getPropValues(dspacecfg);
		log.info("Old properties values=============>"+webui_itemdisplay_default);
		 PropertiesConfiguration config=new PropertiesConfiguration(dspacecfg);
		 config.setProperty("webui.itemdisplay.default",webui_itemdisplay_default+","+" "+data);
	    	config.save();
	    	log.info("Dspace file updated");*/
		GetPropertiesValues prop=new GetPropertiesValues();
		String webui_itemdisplay_default=prop.getPropValues(dspacecfg);
		System.out.println("Old properties values=============>"+webui_itemdisplay_default);
		if(webui_itemdisplay_default.contains(data))
		{
			System.out.println("Great '%s' contains substring %n"+ webui_itemdisplay_default);

		}
		else
		{
			System.out.println("Sorry %s does not contains Substring %n"+webui_itemdisplay_default);
			PropertiesConfiguration config=new PropertiesConfiguration(dspacecfg);
			config.setProperty("webui.itemdisplay.default", webui_itemdisplay_default+","+" "+data);
			config.save();
		}
	}
	//================update Message file...
	public static void updateMessage(String data,String lavel) throws ConfigurationException, IOException
	{
		 /*PropertiesConfiguration config=new PropertiesConfiguration(messagesproperties);
		 config.setProperty("metadata.dc."+data,lavel);
	    config.save();*/
		GetPropertiesValues prop=new GetPropertiesValues();
		String key=prop.getMessagePropValues(messagesproperties,lavel);
		System.out.println(key);
		if(key!=null&&key.equals(lavel))
		{
			log.info("Great '%s' contains substring %n"+ lavel);
		}
		else
		{
			PropertiesConfiguration config=new PropertiesConfiguration(messagesproperties);
			 config.setProperty("metadata.dc."+data,lavel);
		    config.save();	
		}
	}
	public static void AddAdvance(String datavale) throws IOException, ConfigurationException
	{
		
		PropertiesConfiguration config=new PropertiesConfiguration(dspacecfg);
	 	Integer[] array=intvalue();
	 	String[] keys=getAdvanceKeys();
        int j=-1;
	    int max=array[0];
	    	for(int i=1;i<array.length;i++)
	    	{
	    		if(array[i]>max)
	    		{
	    			max=array[i];
	    		
	    		}
	    		//System.out.println(array[i]);
	    	}
	    	 
	         for(int i=0;i<keys.length;i++)
	         {
	        
	         	if(keys[i].equals("jspui.search.index.display."+max))
	         	{
	         		j=i;
	         		break;
	         	}
	  
	         }
	         if(j!=-1)
	     	{
	     		config.setProperty("jspui.search.index.display."+(max+1),datavale);
	     		  config.save();
	     		  
	     	}
	     	else
	     	{
	     		log.info("Key allready Present....");
	     	}
	      
	 
	}
	public static void AddAdvanceSearch(String datavale) throws IOException, ConfigurationException
	{
		
		PropertiesConfiguration config=new PropertiesConfiguration(dspacecfg);
	 	Integer[] array=intvalue();
	 	String[] keys=getAllKeys();
        int j=-1;
	    int max=array[0];
	    	for(int i=1;i<array.length;i++)
	    	{
	    		if(array[i]>max)
	    		{
	    			max=array[i];
	    		
	    		}
	    		//System.out.println(array[i]);
	    	}
	    	 
	         for(int i=0;i<keys.length;i++)
	         {
	        
	         	if(keys[i].equals("search.index."+max))
	         	{
	         		j=i;
	         		break;
	         	}
	  
	         }
	         if(j!=-1)
	     	{
	     		config.setProperty("search.index."+(max+1),datavale);
	     		  config.save();
	     		  
	     	}
	     	else
	     	{
	     		log.info("Key allready Present....");
	     	}
	      
	 
	}
	public static void AddBrowsemenu(String data,String lavel) throws ConfigurationException
	{
		 PropertiesConfiguration config=new PropertiesConfiguration(messagesproperties);
		 config.setProperty("browse.menu."+data,lavel);
	    config.save();

	}
	public static void AddBrowsetype(String data,String lavel) throws ConfigurationException
	{
		 PropertiesConfiguration config=new PropertiesConfiguration(messagesproperties);
		 config.setProperty("browse.type.metadata."+data,lavel);
	    config.save();
	}
	public static String[] getMapAttribute() throws ParserConfigurationException, SAXException, IOException
	 {
		 List<String> list=new ArrayList<String>();
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document document = db.parse(new File(inputformXml));
	        NodeList nodeList = document.getElementsByTagName("name-map");
	        for(int x=0,size= nodeList.getLength(); x<size; x++) {
	            //System.out.println(nodeList.item(x).getAttributes().getNamedItem("collection-handle").getNodeValue());
	            String attr=nodeList.item(x).getAttributes().getNamedItem("collection-handle").getNodeValue();
	        list.add(attr);
	            }        
			String[] attrvalue=new String[list.size()];
			return(String[])list.toArray(attrvalue);
	 }
	public static String[] getFormAttribute() throws ParserConfigurationException, SAXException, IOException
	 {
		 List<String> list=new ArrayList<String>();
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document document = db.parse(new File(inputformXml));
	        NodeList nodeList = document.getElementsByTagName("form");
	        for(int x=0,size= nodeList.getLength(); x<size; x++) {
	            //System.out.println(nodeList.item(x).getAttributes().getNamedItem("name").getNodeValue());
	            String attr=nodeList.item(x).getAttributes().getNamedItem("name").getNodeValue();
	        list.add(attr);
	            }        
			String[] attrvalue=new String[list.size()];
			return(String[])list.toArray(attrvalue);
	 }
	 public static String[] getpageAttributeform(String fname) throws ParserConfigurationException, FileNotFoundException, SAXException, IOException, XPathExpressionException
	 {
		 List<String> list=new ArrayList<String>();
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document document = db.parse(new File(inputformXml));
		final XPath xPath = XPathFactory.newInstance().newXPath();
	    //final XPathExpression expression = xPath.compile("//form[@name='fname']//page");
		 final XPathExpression expression = xPath.compile("//form[@name='"+fname+"']//page");
	    final NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
	    for (int i = 0; i < nodeList.getLength(); ++i) {
	        //System.out.println(((Element)nodeList.item(i)).getAttribute("number"));
	    	String attr=((Element)nodeList.item(i)).getAttribute("number");
	        list.add(attr);
	    }
	    String[] attrvalue=new String[list.size()];
		return (String[])list.toArray(attrvalue);
	 }
	 public static String[] getAllKeys() throws IOException{
			List<String> keylist=new ArrayList<String>();
			try{
			Properties prop = new Properties();
			
			inputStream=new FileInputStream(dspacecfg);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + dspacecfg+ "' not found in the classpath");
			}
			 keys = prop.keySet();
			 for(Object k:keys){
		         String key = (String)k;
		         for(int i=0;i<=keys.size();i++)
		         {
		         	if(key.equals("search.index."+i))
		         		keylist.add(key);
		        //System.out.println(key);
		         }
		     }
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
			String[] allkey=new String[keylist.size()];
			return(String[])keylist.toArray(allkey);
		}
	 
	 public static String[] getAdvanceKeys() throws IOException{
			List<String> keylist=new ArrayList<String>();
			try{
			Properties prop = new Properties();
			
			inputStream=new FileInputStream(dspacecfg);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + dspacecfg+ "' not found in the classpath");
			}
			 keys = prop.keySet();
			 for(Object k:keys){
		         String key = (String)k;
		         for(int i=0;i<=keys.size();i++)
		         {
		         	if(key.equals("jspui.search.index.display."+i))
		         		keylist.add(key);
		        //System.out.println(key);
		         }
		     }
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
			String[] allkey=new String[keylist.size()];
			return(String[])keylist.toArray(allkey);
		}
	 public static String[] getlastWord() throws IOException
	 {
	 	String [] keylist=getAllKeys();
	 	List<String> lastvalue=new ArrayList<String>();
	 	for(int i=0;i<keylist.length;i++)
	 	{
	 		lastvalue.add(keylist[i].substring(keylist[i].lastIndexOf(".")+1));
	 	}
	 String[] lastword=new String[lastvalue.size()];
	 return(String[])lastvalue.toArray(lastword);
	 	}
	 public static Integer[] intvalue() throws IOException
	 {
	 	String[] lastword=getlastWord();
	 	Integer[] intarray=new Integer[lastword.length];
	 	int i=0;
	 	for(String str:lastword)
	 	{
	 		intarray[i]=Integer.parseInt(str);
	 		i++;
	 	}
	 	return intarray;
	 	}
	 
	 public static String[] gethomepageConfiguration() throws ParserConfigurationException, FileNotFoundException, SAXException, IOException, XPathExpressionException
	 {
		 List<String> list=new ArrayList<String>();
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document document = db.parse(new File(discoveryxml));
		final XPath xPath = XPathFactory.newInstance().newXPath();
	    //final XPathExpression expression = xPath.compile("//form[@name='fname']//page");
		 final XPathExpression expression = xPath.compile("//bean[@id='homepageConfiguration']//property");
	    final NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
	    for (int i = 0; i < nodeList.getLength(); ++i) {
	        //System.out.println(((Element)nodeList.item(i)).getAttribute("name"));
	    	String attr=((Element)nodeList.item(i)).getAttribute("name");
	        list.add(attr);
	    }
	    String[] attrvalue=new String[list.size()];
		return (String[])list.toArray(attrvalue);
	 }
	 public static String[] getdefaultConfiguration() throws ParserConfigurationException, FileNotFoundException, SAXException, IOException, XPathExpressionException
	 {
		 List<String> list=new ArrayList<String>();
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document document = db.parse(new File(discoveryxml));
		final XPath xPath = XPathFactory.newInstance().newXPath();
	    //final XPathExpression expression = xPath.compile("//form[@name='fname']//page");
		 final XPathExpression expression = xPath.compile("//bean[@id='defaultConfiguration']//property");
	    final NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
	    for (int i = 0; i < nodeList.getLength(); ++i) {
	        //System.out.println(((Element)nodeList.item(i)).getAttribute("name"));
	    	String attr=((Element)nodeList.item(i)).getAttribute("name");
	        list.add(attr);
	    }
	    String[] attrvalue=new String[list.size()];
		return (String[])list.toArray(attrvalue);
	 }
	 
	 
	 /*
	  * Add by Sanjeev Kumar 10-04-2016
	  * 
	  * */
		public static void Add_valuePair(String valuepair_name,String display_value,String stored_value) throws XPathExpressionException
		{
			try {
				 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		            Document document = documentBuilder.parse(inputformXml); 
		            Node value_pair=document.getElementsByTagName("form-value-pairs").item(0);
		            int c=-1;
		            String[] valuepairname=getValuePairname(inputformXml);
		            for(int i=0;i<valuepairname.length;i++)
		            {
		            	if(valuepairname[i].equals(valuepair_name))
		            	{
		            		 c=Arrays.asList(valuepairname).indexOf(valuepair_name);
		            	}
		            }
		            if(c!=-1)
				      {
		            	 log.info("VAlue pair Name Exit=====================>"+c);
		            	 Node valuepair=document.getElementsByTagName("value-pairs").item((c));
		            	 Element pair = document.createElement("pair");
							valuepair.appendChild(pair);
							Element displayvalue=document.createElement("displayed-value");
							displayvalue.appendChild(document.createTextNode(display_value));
							pair.appendChild(displayvalue);
							if(stored_value==null)
							{
								Element storedvalue=document.createElement("stored-value");
								storedvalue.appendChild(document.createTextNode(""));
								pair.appendChild(storedvalue);
							}
							else{
							Element storedvalue=document.createElement("stored-value");
							storedvalue.appendChild(document.createTextNode(stored_value));
							pair.appendChild(storedvalue);
							}
				      }
		            else
		            {
		            	log.info("VAlue pair name not exit===========>"+c);
		            	Element valuepair=document.createElement("value-pairs");
						value_pair.appendChild(valuepair);
						Attr dcterm=document.createAttribute("dc-term");
						Attr valuepairname1=document.createAttribute("value-pairs-name");
						//Set Attribute value............
						dcterm.setValue(valuepair_name+"-1");
						valuepairname1.setValue(valuepair_name);
						valuepair.setAttributeNode(dcterm);
						valuepair.setAttributeNode(valuepairname1);   
						Element pair=document.createElement("pair");
						valuepair.appendChild(pair);
						Element displayvalue=document.createElement("displayed-value");
						displayvalue.appendChild(document.createTextNode(display_value));
						pair.appendChild(displayvalue);
						if(stored_value==null)
						{
							Element storedvalue=document.createElement("stored-value");
							storedvalue.appendChild(document.createTextNode(""));
							pair.appendChild(storedvalue);
						}
						else{
							Element storedvalue=document.createElement("stored-value");
							storedvalue.appendChild(document.createTextNode(stored_value));
							pair.appendChild(storedvalue);
						}
						
		            }
		            
		            // write the DOM object to the file.............................
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
	 	            Transformer transformer = transformerFactory.newTransformer(); 
	 	            DOMSource domSource = new DOMSource(document);
	 	            StreamResult streamResult = new StreamResult(new File(inputformXml));
	  	            transformer.transform(domSource, streamResult);
			} catch (ParserConfigurationException pce) {

	            pce.printStackTrace();

	        } catch (TransformerException tfe) {

	            tfe.printStackTrace();

	        } catch (IOException ioe) {

	            ioe.printStackTrace();

	        } catch (SAXException sae) {

	            sae.printStackTrace();

	        }
			
		}
		
		 public static String[] getValuePairname(String inputformXml) throws ParserConfigurationException, FileNotFoundException, SAXException, IOException, XPathExpressionException
		 {
			 List<String> list=new ArrayList<String>();
			 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		        DocumentBuilder db = dbf.newDocumentBuilder();
		        Document document = db.parse(new File(inputformXml));
			 NodeList nodeList = document.getElementsByTagName("value-pairs");
		        for(int x=0,size= nodeList.getLength(); x<size; x++) {
		            //System.out.println(nodeList.item(x).getAttributes().getNamedItem("value-pairs-name").getNodeValue());
		        String value=nodeList.item(x).getAttributes().getNamedItem("value-pairs-name").getNodeValue();
		        list.add(value);
		        }
		        String[] attrvalue=new String[list.size()];
				return (String[])list.toArray(attrvalue);
		 }
	 /*
	  * 
	  * End By sanjeev Kumar 10-04-2016
	  * 
	  * */
}
