package chrisb.animaladjectives.storage;

import java.util.ArrayList;
import java.util.List;

import chrisb.animaladjectives.content.FullWord;

public class PreviousAndNextStorageManager {

	
	private List<FullWord> wordsList = new ArrayList<FullWord>();
	private int maxWordListLength = 20;
	private int currentPosition = -1;
	
	public PreviousAndNextStorageManager() {
		
	}
	
	public void addWord(FullWord word) {
		if (wordsList.size() > maxWordListLength) {
			wordsList.remove(0);
		} else {
			currentPosition++;
		}
		removeAllWordsAboveCurrentPosition();
		wordsList.add(word);
	}
	
	private void removeAllWordsAboveCurrentPosition() {
		if (currentPosition < wordsList.size()) {
			wordsList = wordsList.subList(0, currentPosition);
		}
	}
	
	public FullWord getPreviousWord() {
		int newPosition = currentPosition - 1;
		if (wordsList.size() > 0 && newPosition >= 0 && wordsList.size() > newPosition) {
			currentPosition--;
			return wordsList.get(currentPosition);
		}
		return null;
	}
	
	public FullWord getNextWord() {
		int newPosition = currentPosition + 1;
		if (wordsList.size() > newPosition && newPosition >= 0) { 
			currentPosition++;
			return wordsList.get(currentPosition);
		}
		return null;
	}
	
}
