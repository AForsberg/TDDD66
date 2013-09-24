package Lab1;

public class videoPlayer {
	
	int currentBufSize;
	String operation = "Pause";
	
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public int getCurrentBufSize() {
		return currentBufSize;
	}

	public void setCurrentBufSize(int currentBufSize) {
		this.currentBufSize = currentBufSize;
	}

	int maxBuf = 6;
	int minBuf = 4;
	
	int playbackQuality = 0;
	
	int checkQuality(int bandwidth){
		int currentBandwidth = 0;
		if (bandwidth >= 250 && bandwidth < 500) {
			currentBandwidth = 0;
		}else if (bandwidth >= 500 && bandwidth < 850) {
			currentBandwidth = 1;			
		}else if (bandwidth >= 850 && bandwidth < 1300) {
			currentBandwidth = 2;
		}else if (bandwidth >= 1300) {
			currentBandwidth = 3;
		}
		return currentBandwidth;
	}
	
	

}
