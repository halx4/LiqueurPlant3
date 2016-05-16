#  Instuctions for using Test Server General (v.1) 
#  author: foivos christoulakis


READ (uses client,objID,ObjInstIP,ResIP)
	sends READ request
	
WRITE (uses client,objID,ObjInstIP,ResIP,value)
	sends WRITE request
	(not implemented yet)	

EXECUTE (uses client,objID,ObjInstIP,ResIP)
	sends EXECUTE request
	
bind observation (uses client,objID,ObjInstIP,ResIP)
	establishes an observation
	(not implemented yet)

bind std observations (uses client)
	establishes observations needed for fill-empty cycle
	
start fill-empty cycle (uses: - )
	starts fill empty cycle. Before executing, "bind std observations" must have been executed succesfully.
	
//-------------------------------------

parameters: 
	1) name of *.properties file in folder ./runConfigs/ without the .properties extension
		eg. > java -jar testServer-general.jar server-standard
		
		properties' file required properties:
		1)serverID (use 0)
		2)serverIP : IP to bind the Leshan Server. (you can use ip. usual settings: "0.0.0.0" or "localhost")
		3)serverPort : Port to bind the Leshan Server.
	
//--------------------------------------
		
Other requirements:
	*.json file with custom objects' declaration in leshan acceptable form must be in ./src/main/resources/ (any *.json name is accepted)
	
	
	
		