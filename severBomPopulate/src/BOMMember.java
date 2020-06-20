package serverbompopulate;

/**
 * @author Yara Medhat
 *	Class representing a BOM Member
 *	Including : identifier code, verbalization, mapping, documentation
 */
public class BOMMember {

	private String identifierCode;
	private String verbalization;
	private String mapping;
	private String documentation;

	public BOMMember(String identifierCode, String verbalization, String mapping,
			String documentation) {
		this.identifierCode = identifierCode;
		this.verbalization = verbalization;
		this.mapping = mapping;
		this.documentation = documentation;
	}
	public String getIdentifierCode() {
		return identifierCode;
	}
	public String getVerbalization() {
		// Default text is same as the identifierCode
		if (verbalization == null) {
			return identifierCode;
		}
		return verbalization;
	}
	public String getBOM2XOMMapping() {
		// Default translation is item between double quotes
		if (mapping == null) {
			return "return \"" + identifierCode + "\";";
		}
		return mapping;
	}

	public String getDocumentation() {
		// Default text is same as the identifierCode
		if (documentation == null) {
			return "";
		}
		return documentation;
	}

}
