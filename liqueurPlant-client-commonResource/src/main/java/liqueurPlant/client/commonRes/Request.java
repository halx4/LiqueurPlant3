package liqueurPlant.client.commonRes;

public class Request {
		private String requesterID;
		private RequestType requestType;
		
		String requesterID() {
			return requesterID;
		}

		RequestType type() {
			return requestType;
		}

		

		Request(String requesterID,RequestType requestType) {
			super();
			this.requesterID = requesterID;
			this.requestType=requestType;
		}
		
		
		
}
