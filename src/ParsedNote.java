public class ParsedNote {
	public long time;
	public byte note;
	public long duration;
	
	public ParsedNote(long time, byte note, long duration) {
		this.time = time;
		this.note = note;
		this.duration = duration;
	}
}
