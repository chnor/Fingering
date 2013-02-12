public class JFNoteEvent extends JFMidiEvent {
	public byte note;
	public long duration;
	
	public JFNoteEvent(long time, byte voice, byte note, long duration) {
		super(time, voice);
		
		this.note = note;
		this.duration = duration;
	}
}
