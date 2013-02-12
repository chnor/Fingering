import org.jfugue.*;


public class JFParserListenerTest extends ParserListenerAdapter {
	private int noteCounter = 0, parallelNoteCounter = 0, sequentialNoteCounter = 0, tempoCounter = 0, voiceCounter = 0, timeCounter = 0, measureCounter = 0;
	public void noteEvent(Note note) {
		noteCounter++;
	}
	public void parallelNoteEvent(Note note) {
		parallelNoteCounter++;
	}
    public void	sequentialNoteEvent(Note note) {
    	sequentialNoteCounter++;
    }
    public void tempoEvent(Tempo tempo) {
    	tempoCounter++;
    }
    public void voiceEvent(Voice voice) {
    	voiceCounter++;
    }
    public void timeEvent(Time time) {
    	timeCounter++;
    }
    public void measureEvent(Measure measure) {
    	measureCounter++;
    }
    
    public void printCounters() {
    	System.out.println("noteCounter: " + noteCounter);
    	System.out.println("parallelNoteCounter: " + parallelNoteCounter);
    	System.out.println("sequentialNoteCounter: " + sequentialNoteCounter);
    	System.out.println("tempoCounter: " + tempoCounter);
    	System.out.println("voiceCounter: " + voiceCounter);
    	System.out.println("timeCounter: " + timeCounter);
    	System.out.println("measureCounter: " + measureCounter);
    }
}
