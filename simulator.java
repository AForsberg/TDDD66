package Lab1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class simulator {

	private int[] bandwidthHistory = new int[565];
	private int[] requestedQuality = new int[565];
	

	//Method that reads the file and splits up the Strings.
	//The last two columns will be used in another method to determine the download speed.
	private void ReadFile() throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(
				"/home/hughe531/TDDD66/report.2011-01-30_1323CET.log"));
		String line = null;
		int index = 0;
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split("\\s+");
			bandwidthHistory[index] = CalculateBandwidth(Integer
					.parseInt(parts[4]), Integer.parseInt(parts[5]));
			index++;
		}
		PrintResults(bandwidthHistory);
	}

	private void PrintResults(int [] a) {
		System.out.println(Arrays.toString(a));
	}
	

	//Method that determines the download speed by looking at the 
	//size and the time it took the packet to be recieved.
	private int CalculateBandwidth(int bytes, int time) {
		int bandwidth = 0;

		bandwidth = (bytes / time) * 8;

		return bandwidth;

	}

	//Method that sets the quality of the streaming. 
	//It makes it only possible to change the streaming one step up and one or two steps down.
	private void setQuality(videoPlayer player) {
		int previousQuality = -1;
		for (int i = 0; i < bandwidthHistory.length; i++) {
			if (player.checkQuality(bandwidthHistory[i]) > previousQuality){
				requestedQuality[i] = previousQuality + 1;
			}else if (player.checkQuality(bandwidthHistory[i]) < previousQuality - 2) {
				requestedQuality[i] = previousQuality - 2;
			} else {
				requestedQuality[i] = player.checkQuality(bandwidthHistory[i]);
			}
			previousQuality = requestedQuality[i];
			
		}
	}
	
	//Method that determines if the streaming is going to pause or play by looking at the buffersize.
	//If the buffer is less than 4 the streaming will pause and the next arriving packet will fill the buffer.
	//If the buffer is bigger than 4 but less than 6 the streaming will play and download another packet.
	//However, if the buffer is bigger than 6 the streaming will play but it will NOT download/request another packet.
	
	private void bufferOperation(videoPlayer player){
		
		boolean currentlyDownloading = false;
		boolean waitForMinBuf = false;
		int currentBandwidth = 0;
		int currentFragmentSize = 0;
		int fragmentDownloaded = 0;
		fragment newfragment = null;
		
		for (int i = 0; i < requestedQuality.length; i++) {
			currentBandwidth = bandwidthHistory[i];
			
			player.bufferHistory[i] = player.currentBufSize;
			int currentSize = player.getCurrentBufSize();
			System.out.println("Current buffer size = " +player.currentBufSize);
			
			if(currentSize > 6)
				waitForMinBuf = true;
			
			
			
			if(currentlyDownloading == false && waitForMinBuf == false){
				newfragment = new fragment(requestedQuality[i],4);				
				currentlyDownloading = true;
				}
			if(newfragment.getCurrentlyDownloaded() >= newfragment.getFragmentSize()){
				player.setCurrentBufSize(currentSize +4);
				currentlyDownloading = false;
			}
			if(currentlyDownloading){
				newfragment.setCurrentlyDownloaded(currentBandwidth/newfragment.getQuality());
			}
			
			/*if(currentSize <= 4 && currentlyDownloading == false){
					
				currentFragmentSize = player.currentBitrate[i] * 4;
				fragmentDownloaded = fragmentDownloaded + currentBandwidth;
				currentlyDownloading = true;
				
				player.setOperation("Pause");
				System.out.println(" Player operation = " + player.getOperation() +" Requested quality = " +requestedQuality[i]);
			}else if (currentSize <= 6 && waitForMinBuf == false && currentlyDownloading == false){
				
				player.setOperation("Play");
				System.out.println(" Player operation = " + player.getOperation() +" Requested quality = " +requestedQuality[i]);
				player.setCurrentBufSize(player.currentBufSize - 1);
			}else{
				player.setOperation("Play");
				System.out.println(" Player operation = " + player.getOperation() +" Requested quality = " +requestedQuality[i]);
				waitForMinBuf = true;
			}*/
			
			
			player.setCurrentBufSize(player.currentBufSize - 1);

			
		}
	}
	private void writeResults(videoPlayer player, simulator sim){
		try {
			//buffersize
			BufferedWriter writer = new BufferedWriter(new FileWriter("/home/hughe531/TDDD66/input1.txt"));
			//current quality
			BufferedWriter writer2 = new BufferedWriter(new FileWriter("/home/hughe531/TDDD66/input2.txt"));
			//requested quality
			BufferedWriter writer3 = new BufferedWriter(new FileWriter("/home/hughe531/TDDD66/input3.txt"));
			 for (int i = 0; i < player.bufferHistory.length; i++) {
	                writer.write(i + " " + player.bufferHistory[i] + "\n");
	                if (i == 0) {
	                	  writer2.write(i + " " + 0 + "\n");
					}else
	                writer2.write(i + " " +  sim.requestedQuality[i-1] +  "\n");
	                writer3.write(i + " " +sim.requestedQuality[i] + "\n");
			 }
			 writer.close();
			 writer2.close();
			 writer3.close();
			 
			 
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void main(String[] args) throws IOException {
		simulator sim = new simulator();
		videoPlayer player = new videoPlayer();
		sim.ReadFile();
		sim.setQuality(player);
		sim.PrintResults(sim.requestedQuality);
		sim.bufferOperation(player);
		sim.PrintResults(player.bufferHistory);
		sim.writeResults(player, sim);
	}

}
