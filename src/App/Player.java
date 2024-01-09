package App;

public class Player {
	static int id = 1;
	private int playerId;
	private String name;
	private int totalGamesPayed = 0;
	private int totalLosses = 0;
	private int totalWins = 0;
	private int totalDraws = 0;

	public Player(String name) {
		this.playerId = id++;
		this.name = name;
	}
	
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
	@Override
	public String toString() {
		return "Player [playerId=" + playerId + ", name=" + name + ", totalGamesPayed=" + totalGamesPayed
				+ ", totalLosses=" + totalLosses + ", totalWins=" + totalWins + ", totalDraws=" + totalDraws + "]";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getTotalGamesPayed() {
		return totalGamesPayed;
	}

	public void setTotalGamesPayed(int totalGamesPayed) {
		this.totalGamesPayed = totalGamesPayed;
	}
	
	public int getTotalDraws() {
		return totalDraws;
	}

	public void setTotalDraws(int totalDraws) {
		this.totalDraws = totalDraws;
	}

	public int getTotalWins() {
		return totalWins;
	}

	public void setTotalWins(int totalWins) {
		this.totalWins = totalWins;
	}

	public int getTotalLosses() {
		return totalLosses;
	}

	public void setTotalLosses(int totalLosses) {
		this.totalLosses = totalLosses;
	}
}
