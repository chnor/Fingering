
public abstract class JFMidiEvent implements Comparable<JFMidiEvent> {
	public long time;
	public byte voice;
	
	public JFMidiEvent(long time, byte voice) {
		this.time = time;
		this.voice = voice;
	}
	
	public int compareTo(JFMidiEvent other) {
		if(this.time - other.time < 0) return -1;
		else if(this.time - other.time > 0) return 1;
		else return 0;
	}
}
