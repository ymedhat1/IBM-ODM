Database Dynamic Domain Plugin
-------------------------------------
The DB Dynamic Domain Plugin is an easy to use code that helps developers configure their own dynamic plugin on any DB. It is a simple step by step instructions that is based upon the sample provided in the ODM samples named “serverBomPopulate under “Data Sources for BOM Domains” category.
This tool was born out of 3 business needs:
	1.	It enables dynamic synchronization of DB elements with the BOM.
	2.	The usability for developers and no need to re-implement for every business project/need.

Access to Database is implemented 
The plugin is implemented to read from more than one properties file a list of all members including:
     . XOM Path
     . Verbalization column (from DB)
     . Identifier Column
     . Code Column
     . SQL sentence to select all the above
 The plugin contains the DB Connection details driver, URL, username, password
 
 A BOM Member class is implemented to hold all the retrieved data from DB
 Resource Manager Class, responsible of Database connection
 DomainHelper Class which initializes the values by calling getDomainValuesFromDB function that retrieves all specified domains within the mapping.properties file.

How to install:
---------------
You'll need the following components installed:
	•	DB Connection Properties.
	•	Direct connection or an open port security to the DB.
	•	List of all BOM Members need to be synchronized with the DB.
	•	
To enable the use of this sample you will need to:
	1.	Clone this repository into the target directory where you'd like to install the application
	2.	Import the repository into your workspace
	3.	Modify files:
    a) db_config.properties => add your DB connection properties and credentials
    b) mapping.properties => To include a mapping (using the same sample structure) to the BOM members including:
        ⁃	XOM Path (Domain Name)
        ⁃	Identifier Code (retrieved from the DB)
        ⁃	Verbalization Code (retrieved from the DB)
        ⁃	Code Column (retrieved from the DB)
        ⁃	SQL Statement (The select values statement that will retrieve all the above from the DB)
    c) Add another properties file in case of other connections

Once your configuration is complete, you can start building the plugin with ant tasks:
- Clean
- Build

How to use:
-------------
	1.	Modify files:
        a) db_config.properties => add your DB connection properties and credentials
        b) mapping.properties => To include a mapping (using the same sample structure) to the BOM members including:
              ⁃	XOM Path (Domain Name)
              ⁃	Identifier Code (retrieved from the DB)
              ⁃	Verbalization Code (retrieved from the DB)
              ⁃	Code Column (retrieved from the DB)
              ⁃	SQL Statement (The select values statement that will retrieve all the above from the DB)
        c) Add another properties file in case of other connections
	2.	Once your configuration is complete, you can start building the plugin with ant tasks:
	    	- Clean
        - Build
	3.	Deploy using the deployment steps available in https://www.ibm.com/support/knowledgecenter/SSQP76_8.7.1/com.ibm.odm.dcenter.custom/topics/tsk_authoring_dc_domains.html using the following steps:
	⁃	Create a preferences.properties file that maps the key you specified as the value for the domainValueProviderName property to the fully qualified name of your implementation class, and include that file in the JAR file that contains your implementation:teamserver.myProviderName= ilog.rules.teamserver.MyBOMDomainValueProvider where myProviderName is the key you specified as the value for the domainValueProviderName property in the BOM.You can also set this preference as a Decision Center configuration parameter using the set-config-param Ant task: ant set-config-param -Dkey=teamserver.myProviderName -Dvalue= ilog.rules.teamserver.MyBOMDomainValueProvider
	⁃	Repackage the Decision Center archive with the repackage-ear command. In the repackage-ear command, use the -DadditionalJars argument to specify the JAR file containing your custom class. (including the DB Jars)

Next steps for this asset:
----------------------------
This asset is being developed mainly as an extension of the Data Sources for BOM Sample. Currently we are working on version 2, which will focus on
	1.	Code Cleanup
	2.	Creating a UI wizard that will guide the developer to select BOM members and map them to the DB
	3.	Automated building of the properties file using the information gathered from (2)
	4.	Adding web service domain values retrieval
