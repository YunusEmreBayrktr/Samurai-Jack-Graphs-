public class Trail implements Comparable<Trail> {
    public Location source;
    public Location destination;
    public int danger;

    public Trail(Location source, Location destination, int danger) {
        this.source = source;
        this.destination = destination;
        this.danger = danger;
    }

	@Override
	public int compareTo(Trail that) {
		return (this.danger - that.danger);
	}
}
