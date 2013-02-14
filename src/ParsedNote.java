public class ParsedNote implements Comparable<ParsedNote> {
	public long time;
	public byte note;
	public long duration;
	
	public ParsedNote(long time, byte note, long duration) {
		this.time = time;
		this.note = note;
		this.duration = duration;
	}
	
	public int compareTo(ParsedNote other) {
		if(this.time - other.time < 0) return -1;
		else if(this.time - other.time > 0) return 1;
		else return 0;
	}
}
