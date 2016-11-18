package liqueurPlant.client.commonRes;

public class Lock {
		private final String NONE="-1";
		private final String NOOWNERSTRING="NONE";
		private String ownerID=NONE;

		public String getOwnerID() {
			return isOwned() ? ownerID : NOOWNERSTRING;
		}

		public void setOwnerID(String ownerID) {
			this.ownerID = ownerID;
		}
		
		public void removeOwner(){this.ownerID=NONE;}
		
		public boolean isOwned(){
			if(ownerID.equals(NONE))return false;
			else return true;
		}
}
