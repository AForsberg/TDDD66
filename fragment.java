package Lab1;

public class fragment {
	
	private int fragmentSize;
	public int getFragmentSize() {
		return fragmentSize;
	}

	public void setFragmentSize(int fragmentSize) {
		this.fragmentSize = fragmentSize;
	}

	private int currentlyDownloaded;
	private int quality;
	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	private boolean isDone;
	
	public fragment(int quality, int size){
		fragmentSize = quality*size;
		currentlyDownloaded = 0;
		isDone = false;
		switch (quality) {
		case 0:
			this.quality = 250;
			break;
		case 1:
			this.quality = 500;
			break;
		case 2:
			this.quality = 850;
			break;
		case 3:
			this.quality = 1300;
			break;			
		default:
			break;
		}
	}

	public int getCurrentlyDownloaded() {
		return currentlyDownloaded;
	}

	public void setCurrentlyDownloaded(int currentlyDownloaded) {
		this.currentlyDownloaded = currentlyDownloaded;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

}
