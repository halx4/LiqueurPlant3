package liquerPlant.annotationsStuff;

public @interface ResourceDef {
		int						parentObjectId();
		int						parentObjectInstanceId();
  		int 					id();
  		ServiceEnablementOp[]	onOperations();
  		
		String 					name() 					default "NOTDEFHERE"; 					//optional
		//ServiceEnablementOp[] 	supportedOperations() 	default ServiceEnablementOp.NOTDEFHERE;	//optional
		ResourceMultiplicity	multiplicity()			default ResourceMultiplicity.NOTDEFHERE;//optional
		Mandatory 				mandatory() 			default Mandatory.NOTDEFHERE;			//optional
		String 					type() 					default "NOTDEFHERE";					//optional
		String 					range() 				default "NOTDEFHERE";					//optional
		String 					units() 				default "NOTDEFHERE";					//optional
		String 					description() 			default "NOTDEFHERE"; 					//optional
		
  	}