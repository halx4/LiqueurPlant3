package liqueurPlant.core;

import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.response.LwM2mResponse;


public class LwM2mResourceParser {

	public static String valueOf(LwM2mNode node){
		return string2Value(node.toString());

	}
	
	public static String valueOf(LwM2mResponse response){
		
		return string2Value(response.toString());

		
	}
	
	private static String string2Value(String string){
		int start=string.indexOf("value=");
		String substring=string.substring(start+6);
		//System.out.println("substring="+substring);
		String[] split=substring.split(",");
		//System.out.println("slpit0="+split[0]);
	
		return split[0];
	}
}