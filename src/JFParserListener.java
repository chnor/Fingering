import java.util.*;
import org.jfugue.*;


public class JFParserListener extends ParserListenerAdapter {
	private long currentTime = 0;
	private byte currentVoice = 0;
	
	private ArrayList<JFMidiEvent> midiEvents = new ArrayList<JFMidiEvent>();
	
	public ArrayList<JFMidiEvent> getEvents() {
		return midiEvents;
	}
	
	public void noteEvent(Note note) {
		midiEvents.add(new JFNoteEvent(currentTime, currentVoice, note.getValue(), note.getDuration()));
	}
    public void tempoEvent(Tempo tempo) {
    	midiEvents.add(new JFTempoEvent(currentTime, currentVoice, tempo.getTempo()));
    }
    public void voiceEvent(Voice voice) {
    	currentVoice = voice.getVoice();
    }
    public void timeEvent(Time time) {
    	currentTime = time.getTime();
    }
}
