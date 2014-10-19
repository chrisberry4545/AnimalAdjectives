package chrisb.animaladjectives.content;

public class FullWord {

	private String word1;
	private String word2;
	
	public String getFirstPart() {
		return word1;
	}
	
	public String getLastPart() {
		return word2;
	}
	
	public void setFirstPart(String word) {
		word1 = word;
	}
	
	public void setSecondPart(String word) {
		word2 = word;
	}
	
	public String getFullWord() {
		return word1 + " " + word2;
	}
}
