package liqueurPlant.client.commonRes;

public class CommonResource {
		private int ownerID=-1;

		public int getOwnerID() {
			return ownerID;
		}

		public void setOwnerID(int ownerID) {
			this.ownerID = ownerID;
		}
		
		public void removeOwner(){this.ownerID=-1;}
		
		public boolean isOwned(){
			if(ownerID>=0)return true;
			else return false;
		}
}
