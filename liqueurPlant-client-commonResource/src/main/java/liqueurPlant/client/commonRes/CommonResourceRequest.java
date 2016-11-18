package liqueurPlant.client.commonRes;

public class CommonResourceRequest {
		private int requesterID;
		private CommonResourceRequestType requestType;
		
		int requesterID() {
			return requesterID;
		}

		CommonResourceRequestType type() {
			return requestType;
		}

		

		CommonResourceRequest(int requesterID,CommonResourceRequestType requestType) {
			super();
			this.requesterID = requesterID;
			this.requestType=requestType;
		}
		
		
		
}
