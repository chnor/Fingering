import java.util.*;
import org.jfugue.*;


public class MidiParserListener extends ParserListenerAdapter {
	private long currentTime = 0;
	private byte currentVoice = 0;
	
	private ArrayList<ParsedNote> notes = new ArrayList<ParsedNote>();
	
	public ArrayList<ParsedNote> getNotes() {
		return notes;
	}
	
	public void noteEvent(Note note) {
		if(note.getDuration() != 0 && currentVoice == 0)
			notes.add(new ParsedNote(currentTime, note.getValue(), note.getDuration()));
	}
    public void voiceEvent(Voice voice) {
    	currentVoice = voice.getVoice();
    }
    public void timeEvent(Time time) {
    	currentTime = time.getTime();
    }
}
