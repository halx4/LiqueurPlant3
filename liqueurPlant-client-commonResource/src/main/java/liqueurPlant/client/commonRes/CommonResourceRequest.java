package liqueurPlant.client.commonRes;

public class CommonResourceRequest {
		private String requesterID;
		private CommonResourceRequestType requestType;
		
		String requesterID() {
			return requesterID;
		}

		CommonResourceRequestType type() {
			return requestType;
		}

		

		CommonResourceRequest(String requesterID,CommonResourceRequestType requestType) {
			super();
			this.requesterID = requesterID;
			this.requestType=requestType;
		}
		
		
		
}
