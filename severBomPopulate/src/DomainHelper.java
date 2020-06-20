/*
 * Licensed Materials - Property of IBM
 * 5725-B69 5655-Y17 5724-Y00 5724-Y17 5655-V84
 * Copyright IBM Corp. 1987, 2017. All Rights Reserved.
 *
 * Note to U.S. Government Users Restricted Rights: 
 * Use, duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 */

package serverbompopulate;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import serverbompopulate.BOMMember;

/**
 * A static function provides a helper for a dedicated fully-qualified class,
 * which is responsible for providing the values.
 */
public class DomainHelper {

	// Hashmap storing configurations for a specific class
	// Key = fully-qualified name of the domain class
	private static HashMap<String, DomainHelper> classdomains = new HashMap<String, DomainHelper>();

	/**
	 * Representation of a static reference: name, verbalization, and
	 * translation (b2x). Instances of this class are stored in each database
	 * domain helper.
	 */
	public static class Item {
		private String name;// my Identifier Code
		private String verbalization;
		private String code;// my mapping

		public Item(String name, String verbalization, String code) {
			this.name = name;
			this.verbalization = verbalization;
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public String getVerbalization() {
			// Default text is same as the name
			if (verbalization == null)
				return name;
			return verbalization;
		}

		public String getTranslation() {
			// Default translation is item between double quotes
			if (code == null)
				return "\"" + name + "\"";
			return code;
		}

	}

	// An instance of the helper is attached to each domain class
	private String className;
	// Store the domain items in a list
	private List<Item> domainItems = new ArrayList<Item>();
	HashMap<String, List<BOMMember>> domains = new HashMap<String, List<BOMMember>>();

	/**
	 * Private constructor, use the factory to get such an instance.
	 * 
	 * @param fullyQualifiedName
	 *            The name of the class.
	 */
	public DomainHelper(){
		
	}
	private DomainHelper(String fullyQualifiedName) {
		this.className = fullyQualifiedName;
	}

	/**
	 * Initialize values for the class name given the attributes.
	 */
	public void initValues() {
		
		domains = getDomainValuesFromDB();
		domainItems.clear();
		
		List<BOMMember> classDomains = domains.get(this.className);

		System.out.println("post get className");
		
		if(classDomains != null && classDomains.size() > 0){
			
			for (int i = 0; i < classDomains.size(); i++) {
				
				domainItems.add(new Item("ID_"
						+ classDomains.get(i).getIdentifierCode(), classDomains
						.get(i).getVerbalization(), classDomains.get(i)
						.getBOM2XOMMapping()));
			}
		}

	}

	@SuppressWarnings("finally")
	private HashMap<String, List<BOMMember>> getDomainValuesFromDB() {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// DB
		try {
			Properties properties = new Properties();
			
			InputStream input = this.getClass().getResourceAsStream("mapping.properties");
			properties.load(input);
			
			input.close();			
			conn = ResourceManager.getConnection();			
			Iterator<Entry<Object, Object>> propIterator = properties.entrySet().iterator();
			
			while (propIterator.hasNext()) {
				
				Entry<Object, Object> propEntry = propIterator.next();
				if (propEntry.getKey().toString().endsWith("domainName")) {

					String domainName = (String) propEntry.getValue();

					String sql = properties.getProperty(domainName + ".SQL");
					try {
						stmt = conn.prepareStatement(sql);

						rs = stmt.executeQuery();
					} catch (Exception s) {
						s.printStackTrace();
						continue;
					}
					ArrayList<BOMMember> bomMembers = new ArrayList<BOMMember>();

					while (rs.next()) {

						String identifierColumn = properties
								.getProperty(domainName + ".Identifier_column");

						String identifierCode = rs.getString(identifierColumn);

						String verbalizationColumn = properties
								.getProperty(domainName
										+ ".verbalization_column");

						String verbalization = rs
								.getString(verbalizationColumn);

						String mappingColumn = properties
								.getProperty(domainName + ".mapping_column");

						String codeColumn = properties.getProperty(domainName
								+ ".code_column");

						String codeValue = rs.getString(codeColumn);

						String mapping = "";

						mapping = "return new " + domainName + "(\""
								+ codeValue + "\", \"" + verbalization + "\");";

						if (mappingColumn != null)
							mapping = rs.getString(mappingColumn);

						
						String documentationColumn = properties
								.getProperty(domainName
										+ ".documentation_column");

						String documentation = null;

						if (documentationColumn != null)
							documentation = rs.getString(documentationColumn);

						BOMMember bomMember = new BOMMember(identifierCode,
								verbalization, mapping, documentation);

						System.out.println("10");
						bomMembers.add(bomMember);
					}
					domains.put(domainName, bomMembers);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResourceManager.close(rs);
			ResourceManager.close(stmt);
			ResourceManager.close(conn);
			//return domains;
		}
		// FINACLE
		
		
		try {
			System.out.println("11");
			Properties properties = new Properties();
			
			System.out.println("11-");
			InputStream input = this.getClass().getResourceAsStream("mapping2.properties");
			
			System.out.println("11+" + input);
			properties.load(input);
			System.out.println("11++");
			
			input.close();
			System.out.println("11+++");
			conn = ResourceManager.getFinacleConnection();

			Iterator<Entry<Object, Object>> propIterator = properties.entrySet().iterator();
			System.out.println("12");
			while (propIterator.hasNext()) {
				Entry<Object, Object> propEntry = propIterator.next();
				if (propEntry.getKey().toString().endsWith("domainName")) {

					String domainName = (String) propEntry.getValue();

					String sql = properties.getProperty(domainName + ".SQL");

					System.out.println("SQL : " + sql);
					stmt = conn.prepareStatement(sql);
					rs = stmt.executeQuery();
					ArrayList<BOMMember> bomMembers = new ArrayList<BOMMember>();

					while (rs.next()) {

						String identifierColumn = properties
								.getProperty(domainName + ".Identifier_column");

						String identifierCode = rs.getString(identifierColumn);

						String verbalizationColumn = properties
								.getProperty(domainName
										+ ".verbalization_column");

						String verbalization = rs
								.getString(verbalizationColumn);

						String mappingColumn = properties
								.getProperty(domainName + ".mapping_column");

						String codeColumn = properties.getProperty(domainName
								+ ".code_column");

						String codeValue = rs.getString(codeColumn);

						String mapping = "";

						// mapping = "return " + "\"" + codeValue + "\";";

						//
						// if (categoryValue == null && descriptionValue ==
						// null)
						mapping = "return new " + domainName + "(\""
								+ codeValue + "\", \"" + verbalization + "\");";
						// else
						// mapping = "return new " + domainName + "("
						// + identifierCode + ", \"" + codeValue
						// + "\", \"" + verbalization +"\", \"" +
						// descriptionValue
						// + "\", \"" + categoryValue + "\");";
						//

						System.out.println(mapping);
						if (mappingColumn != null)
							mapping = rs.getString(mappingColumn);

						String documentationColumn = properties
								.getProperty(domainName
										+ ".documentation_column");

						String documentation = null;

						if (documentationColumn != null)
							documentation = rs.getString(documentationColumn);

						BOMMember bomMember = new BOMMember(identifierCode,
								verbalization, mapping, documentation);
						System.out.println("13");
						bomMembers.add(bomMember);
					}
					domains.put(domainName, bomMembers);
					System.out.println("14");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to connect to Finacle");
			System.out.println("End");
			return domains;
		} finally {
			ResourceManager.close(rs);
			ResourceManager.close(stmt);
			ResourceManager.close(conn);
			System.out.println("15");
			return domains;
		}
		
	}

	/**
	 * @return The list of names for the items of the current helper.
	 */
	public Collection<String> getItemNames() {
		ArrayList<String> names = new ArrayList<String>();
		Iterator<Item> it = domainItems.iterator();
		while (it.hasNext()) {
			Item element = (Item) it.next();
			names.add(element.getName());
		}
		return names;
	}

	/**
	 * Gets the verbalization for a given domain item name.
	 * 
	 * @param valueName
	 *            The name of the static reference attribute.
	 * @return The verbalization string.
	 */
	public String getVerbalization(String valueName) {
		Iterator<Item> it = domainItems.iterator();
		while (it.hasNext()) {
			Item element = (Item) it.next();
			if (valueName.equalsIgnoreCase(element.getName()))
				return element.getVerbalization();
		}
		// Should never happen
		return null;
	}

	/**
	 * @param valueName
	 *            The name of the value.
	 */
	public String getTranslation(String valueName) {
		Iterator<Item> it = domainItems.iterator();
		while (it.hasNext()) {
			Item element = it.next();
			if (valueName.equalsIgnoreCase(element.getName()))
				return element.getTranslation();
		}
		// Should never happen
		return null;
	}

	/**
	 * The main entry point for the <code>DomainHelper</code> class. Looks for
	 * an existing domain helper of a given class, and creates it if it does not
	 * exist.
	 *
	 * @param className
	 *            The fully-qualified name of the static reference domain.
	 * @return A helper class dedicated to the domain in the argument.
	 */
	public static DomainHelper getDomainHelper(String className) {
		DomainHelper helper = classdomains.get(className);
		if (helper == null) {
			helper = new DomainHelper(className);
			classdomains.put(className, helper);
		}
		return helper;
	}

}
