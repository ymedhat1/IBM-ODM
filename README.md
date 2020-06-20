# IBM-BOM DB Plugin
# This is an implementation of sample "serverBomPopulate" from "Data Sources for BOM domains" samples
# Access to Database is implemented 
# The plugin is implemented to read from more than one propoerties file a list of all members including:
#     . XOM Path
#     . Verbalization column (from DB)
#     . Identifier Column
#     . Code Column
#     . SQL sentence to select all the above
# The plugin contains the DB Connection details driver, URL, username, password
# 
# A BOM Member class is implemented to hold all the retreived data from DB
# Resource Manager Class, responsible of Database connection
# DomainHelper Class which initializes the values by calling getDomainValuesFromDB function that retreives all specified domains within the mapping.properties file.
