package liqueurPlant.client.commonRes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.util.LinkedList;

import liqueurPlant.utilities.ExitHandler;

@SuppressWarnings("serial")
class CommonResourceGui extends Frame {
		
		Label currentOwnerL;
		Label pendingRequestsL;
		final Color noOwnerColor  =	new Color(255,51,51);
		final Color hasOwnerColor =	new Color(128,255,0);
		private final Color pendingRequestsLColor=new Color(204,255,204);
		
		CommonResourceGui(String name){
		
			
			this.setTitle(name);
			this.setLayout(null);
			this.setFont(new Font("TimesRoman", Font.PLAIN, 14));
			this.addWindowListener(new ExitHandler()); 
			this.setBackground(new Color(192,192,192));
			setBounds(1200,500,270,170);

			this.toFront();
			this.setResizable(true);
			

			currentOwnerL = new Label();
	        add(currentOwnerL);
	        currentOwnerL.setBounds(30, 60, 170, 20);
	        currentOwnerL.setVisible(true);
	        
	        pendingRequestsL = new Label();
	        add(pendingRequestsL);
	        pendingRequestsL.setBounds(30, 90, 170, 20);
	        pendingRequestsL.setBackground(pendingRequestsLColor);
	        pendingRequestsL.setVisible(true);

			// ---------------------------
			this.setVisible(true); // ----Frame setVisible
			// ---------------------------

		}
	
		void update(boolean isOwned,String ownerID, LinkedList<CommonResourceRequest> requestsList){
			if(isOwned){//has owner
				currentOwnerL.setBackground(hasOwnerColor);
				currentOwnerL.setText("Owner ID: "+ownerID);
			}
			else{//no owner
				currentOwnerL.setBackground(noOwnerColor);
				currentOwnerL.setText("Owner ID: -"); 
			}
			
			String pendingRequestsString=new String("Pending Requests: "+requestsList.size());
			//Iterator<CommonResourceRequest> iter=requests.iterator();
			//while(iter.hasNext()){
			//	pendingRequestsString=pendingRequestsString.concat(" "+iter.next().requesterID());
			//}
			//System.out.println("Gui requests string: "+pendingRequestsString);
			pendingRequestsL.setText(pendingRequestsString);
		}
		
	}//end nested class