package liqueurPlant.annotationsStuff;

public @interface ObjectType {
	int 					id();
	String 					name() 			default "NOTDEFHERE"; 					//optional
	ResourceMultiplicity	multiplicity()	default ResourceMultiplicity.NOTDEFHERE;//optional
	Mandatory 				mandatory() 	default Mandatory.NOTDEFHERE;			//optional
	String 					urn() 			default "NOTDEFHERE";					//optional
}
