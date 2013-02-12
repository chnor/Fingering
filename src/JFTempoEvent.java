public class JFTempoEvent extends JFMidiEvent {
	public int tempo;
	
	public JFTempoEvent(long time, byte voice, int tempo) {
		super(time, voice);
		
		this.tempo = tempo;
	}
}
