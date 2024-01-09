package App;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ScoreCard implements Serializable {
	static int id = 1;
	private int scoreCardId;
	private String playerName;
	private int numberOfWins = 0;
	private int numberOfLosses = 0;
	private int numberOfDraws = 0;
	static Set<ScoreCard> scoreCardList;
	public ScoreCard() {
		scoreCardList = new HashSet();
	}
	public ScoreCard(String playerName, int numberOfWins, int numberOfLosses, int numberOfDraws) {
		scoreCardId = id++;
		this.playerName = playerName;
		this.numberOfWins = numberOfWins;
		this.numberOfLosses = numberOfLosses;
		this.numberOfDraws = numberOfDraws;
	}
	
//	public ScoreCard() {
//		// TODO Auto-generated constructor stub
//	}
	public int getScoreCardId() {
		return scoreCardId;
	}
	public void setScoreCardId(int scoreCardId) {
		this.scoreCardId = scoreCardId;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getNumberOfWins() {
		return numberOfWins;
	}
	public void setNumberOfWins(int numberOfWins) {
		this.numberOfWins = numberOfWins;
	}
	public int getNumberOfLosses() {
		return numberOfLosses;
	}
	public void setNumberOfLosses(int numberOfLosses) {
		this.numberOfLosses = numberOfLosses;
	}
	public int getNumberOfDraws() {
		return numberOfDraws;
	}
	public void setNumberOfDraws(int numberOfDraws) {
		this.numberOfDraws = numberOfDraws;
	}
	
	@Override
	public String toString() {
		return "ScoreCard [playerName=" + playerName + ", numberOfWins=" + numberOfWins + ", numberOfLosses="
				+ numberOfLosses + ", numberOfDraws=" + numberOfDraws + "]";
	}
	
	public String toCSVString() {
        return playerName + "," + numberOfWins + "," + numberOfLosses + "," + numberOfDraws;
    }

    public static ScoreCard fromCSVString(String csvString) {
        String[] parts = csvString.split(",");
        if (parts.length == 4) {
            String playerName = parts[0];
            int numberOfWins = Integer.parseInt(parts[1]);
            int numberOfLosses = Integer.parseInt(parts[2]);
            int numberOfDraws = Integer.parseInt(parts[3]);
            return new ScoreCard(playerName, numberOfWins, numberOfLosses, numberOfDraws);
        } else {
            throw new IllegalArgumentException("Invalid CSV string: " + csvString);
        }
    }
}
