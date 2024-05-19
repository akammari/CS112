package transit;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered linked
 * list to simulate transit
 * 
 * @author 
 * @author 
 */
public class Transit {
	private TNode trainZero; // a reference to the zero node in the train layer

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */ 
	public Transit() { trainZero = null; }

	/* 
	 * Default constructor used by the driver and Autolab. 
	 * DO NOT use in your code.
	 * DO NOT remove from this file
	 */
	public Transit(TNode tz) { trainZero = tz; }
	
	/*
	 * Getter method for trainZero
	 *
	 * DO NOT remove from this file.
	 */
	public TNode getTrainZero () {
		return trainZero;
	}

	/**
	 * Makes a layered linked list representing the given arrays of train stations, bus
	 * stops, and walking locations. Each layer begins with a location of 0, even though
	 * the arrays don't contain the value 0. Store the zero node in the train layer in
	 * the instance variable trainZero.
	 * 
	 * @param trainStations Int array listing all the train stations
	 * @param busStops Int array listing all the bus stops
	 * @param locations Int array listing all the walking locations (always increments by 1)
	 */
	public void makeList(int[] trainStations, int[] busStops, int[] locations) {
		int index=locations.length;
		trainZero=new TNode(index);
		index--;

		while(index >= 0){
			trainZero=new TNode(index, trainZero, null);
			index--;
		}

		TNode bsz=null;
		for (int i=busStops.length-1; i>=0; i--) {
			TNode temp=getTrainZero();
			while (temp.getLocation()!=busStops[i])
				temp=temp.getNext();
			bsz=new TNode(busStops[i],bsz,temp);
		}
		trainZero=new TNode(0,bsz,trainZero);

		TNode tsz = null;
		for (int i = trainStations.length-1; i>=0; i--) {
			TNode temp=getTrainZero();
			while (temp.getLocation()!= trainStations[i])
				temp=temp.getNext();
			tsz = new TNode(trainStations[i],tsz,temp);
		}
		trainZero = new TNode(0,tsz,trainZero);
	}
	
	/**
	 * Modifies the layered list to remove the given train station but NOT its associated
	 * bus stop or walking location. Do nothing if the train station doesn't exist
	 * 
	 * @param station The location of the train station to remove
	 */
	public void removeTrainStation(int station) {
		if(station==0) return;
		TNode t=trainZero;
		while(t.getNext()!=null && t.getNext().getLocation()<station){
			t = t.getNext();
		}
		if (t.getNext()!=null && t.getNext().getLocation()==station) t.setNext(t.getNext().getNext());
	}

	/**
	 * Modifies the layered list to add a new bus stop at the specified location. Do nothing
	 * if there is no corresponding walking location.
	 * 
	 * @param busStop The location of the bus stop to add
	 */
	public void addBusStop(int busStop) {
		/* TNode tempLocation=trainZero.getDown().getDown();
		while(tempLocation.getNext()!=null) tempLocation=tempLocation.getNext();
		if (tempLocation.getLocation()<busStop) return;

		TNode firstWalk=trainZero.getDown().getDown();
		while(firstWalk.getLocation()!=busStop) firstWalk=firstWalk.getNext();
		TNode newStop=new TNode(busStop,null,firstWalk);

		TNode busTemp=trainZero.getDown();
		TNode busTempNext=busTemp.getNext();
		while(busTempNext!= null && busTempNext.getLocation()<=busStop){
			busTemp=busTempNext;
			busTempNext=busTempNext.getNext();
		}
		if (busTemp.getLocation()==busStop) return;
		busTemp.setNext(newStop);
		newStop.setNext(busTempNext); */
		TNode b4=trainZero.getDown(); //get bus stop before
		while(b4.getNext()!=null && b4.getNext().getLocation()<busStop) b4=b4.getNext();

		TNode tempLocation=trainZero.getDown().getDown(); // check for beyond all locations
		while(tempLocation.getNext()!=null) tempLocation=tempLocation.getNext();

		boolean bsExist=b4.getNext()!= null && (b4.getNext().getLocation()==busStop ||b4.getLocation()==busStop) && (tempLocation.getLocation()>=busStop); // boolean to check if busStop exists or is beyond, next line returns if beyond or exits
		if(bsExist) return;

		TNode fw=trainZero.getDown().getDown();// get down node with loop
		while(fw.getLocation()!=busStop) fw=fw.getNext();
		b4.setNext(new TNode(busStop,b4.getNext(),fw));//set node
	}
	
	/**
	 * Determines the optimal path to get to a given destination in the walking layer, and 
	 * collects all the nodes which are visited in this path into an arraylist. 
	 * 
	 * @param destination An int representing the destination
	 * @return
	 */
	public ArrayList<TNode> bestPath(int destination) {
		ArrayList<TNode> listT=new ArrayList<TNode>();


		TNode tmMax=trainZero;
		while(tmMax.getNext()!=null && tmMax.getNext().getLocation()<=destination){
			listT.add(tmMax);
			tmMax=tmMax.getNext();
		}
		listT.add(tmMax);

		TNode bmMax=tmMax.getDown();
		while(bmMax.getNext()!=null && bmMax.getNext().getLocation()<=destination){
			listT.add(bmMax);
			bmMax=bmMax.getNext();
		}
		listT.add(bmMax);

		TNode wmMax=bmMax.getDown();
		while(wmMax.getNext()!=null && wmMax.getNext().getLocation()<=destination){
			listT.add(wmMax);
			wmMax=wmMax.getNext();
		}
		listT.add(wmMax);
	    return listT;
	}

	/**
	 * Returns a deep copy of the given layered list, which contains exactly the same
	 * locations and connections, but every node is a NEW node.
	 * 
	 * @return A reference to the train zero node of a deep copy
	 */
	public TNode duplicate() {
		TNode lNw=null;
		TNode tTemp=null;
		TNode lastLocation=trainZero.getDown().getDown();

		while(lastLocation.getNext()!=tTemp) lastLocation=lastLocation.getNext();
		for (int i = lastLocation.getLocation(); i>=0; i--) {
			TNode newStop=new TNode(i, lNw, null);
			lNw=newStop;
		}
		
		TNode busNew=null;
		tTemp=null;
		while(busNew==null || busNew.getLocation()!=0){
			TNode lastBus=trainZero.getDown();
			while(lastBus.getNext()!=tTemp) lastBus=lastBus.getNext();
			tTemp=lastBus;
			TNode temp=lNw;
			while(temp.getNext()!=null && temp.getNext().getLocation()<=lastBus.getLocation()) temp=temp.getNext();
			TNode busTemp=new TNode(temp.getLocation(),busNew,temp);
			busNew=busTemp;
		}

		TNode tnew = null;
		tTemp = null;


		while(tnew==null || tnew.getLocation()!=0){
			TNode lastTrain=trainZero;
			while(lastTrain.getNext()!=tTemp)lastTrain=lastTrain.getNext();
			tTemp=lastTrain;
			TNode temp=busNew;
			while(temp.getNext()!=null && temp.getNext().getLocation()<=lastTrain.getLocation()) temp=temp.getNext();
			TNode trainTemp=new TNode(temp.getLocation(),tnew,temp);
			tnew=trainTemp;
		}

	    return tnew;
	}

	/**
	 * Modifies the given layered list to add a scooter layer in between the bus and
	 * walking layer.
	 * 
	 * @param scooterStops An int array representing where the scooter stops are located
	 */
	public void addScooter(int[] scooterStops) {
		TNode scootyNNew=null;

		for (int i=scooterStops.length-1; i>=0; i--) {
			TNode busNode=trainZero.getDown();
			while(busNode.getNext()!=null && busNode.getNext().getLocation()<=scooterStops[i])busNode=busNode.getNext();
			TNode walkNode=trainZero.getDown().getDown();
			while(walkNode.getNext()!=null && walkNode.getNext().getLocation()<=scooterStops[i]) walkNode=walkNode.getNext();
			//System.out.println("i= "+scooterStops[i]+" busNode= "+busNode.getLocation()+" walkNode= "+walkNode.getLocation());
			TNode newScooter=new TNode(scooterStops[i],scootyNNew,walkNode);
			if (busNode.getLocation()==scooterStops[i]) busNode.setDown(newScooter);
			scootyNNew=newScooter;
		}


		TNode newScooter=new TNode(0,scootyNNew,trainZero.getDown().getDown());
		scootyNNew=newScooter;
		trainZero.getDown().setDown(scootyNNew);
	}

	/**
	 * Used by the driver to display the layered linked list. 
	 * DO NOT edit.
	 */
	public void printList() {
		// Traverse the starts of the layers, then the layers within
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// Output the location, then prepare for the arrow to the next
				StdOut.print(horizPtr.getLocation());
				if (horizPtr.getNext() == null) break;
				
				// Spacing is determined by the numbers in the walking layer
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print("--");
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print("-");
				}
				StdOut.print("->");
			}

			// Prepare for vertical lines
			if (vertPtr.getDown() == null) break;
			StdOut.println();
			 
			TNode downPtr = vertPtr.getDown();
			// Reset horizPtr, and output a | under each number
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				while (downPtr.getLocation() < horizPtr.getLocation()) downPtr = downPtr.getNext();
				if (downPtr.getLocation() == horizPtr.getLocation() && horizPtr.getDown() == downPtr) StdOut.print("|");
				else StdOut.print(" ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();
				for (int j = 0; j < numLen-1; j++) StdOut.print(" ");
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
	
	/**
	 * Used by the driver to display best path. 
	 * DO NOT edit.
	 */
	public void printBestPath(int destination) {
		ArrayList<TNode> path = bestPath(destination);
		for (TNode vertPtr = trainZero; vertPtr != null; vertPtr = vertPtr.getDown()) {
			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the number if this node is in the path, otherwise spaces
				if (path.contains(horizPtr)) StdOut.print(horizPtr.getLocation());
				else {
					int numLen = String.valueOf(horizPtr.getLocation()).length();
					for (int i = 0; i < numLen; i++) {
						StdOut.print(" ");
					}
				}
				if (horizPtr.getNext() == null) break;
				
				// ONLY print the edge if both ends are in the path, otherwise spaces
				String separator = (path.contains(horizPtr) && path.contains(horizPtr.getNext())) ? ">" : " ";
				for (int i = horizPtr.getLocation()+1; i < horizPtr.getNext().getLocation(); i++) {
					StdOut.print(separator + separator);
					
					int numLen = String.valueOf(i).length();
					for (int j = 0; j < numLen; j++) StdOut.print(separator);
				}

				StdOut.print(separator + separator);
			}
			
			if (vertPtr.getDown() == null) break;
			StdOut.println();

			for (TNode horizPtr = vertPtr; horizPtr != null; horizPtr = horizPtr.getNext()) {
				// ONLY print the vertical edge if both ends are in the path, otherwise space
				StdOut.print((path.contains(horizPtr) && path.contains(horizPtr.getDown())) ? "V" : " ");
				int numLen = String.valueOf(horizPtr.getLocation()).length();

				for (int j = 0; j < numLen-1; j++) {
					StdOut.print(" ");
				}
				
				if (horizPtr.getNext() == null) break;
				
				for (int i = horizPtr.getLocation()+1; i <= horizPtr.getNext().getLocation(); i++) {
					StdOut.print("  ");

					if (i != horizPtr.getNext().getLocation()) {
						numLen = String.valueOf(i).length();
						for (int j = 0; j < numLen; j++) StdOut.print(" ");
					}
				}
			}
			StdOut.println();
		}
		StdOut.println();
	}
}