package App;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileManager {
	private final String FILE_PATH = "scorecards.csv";
	public void serialize(Map<String, ScoreCard> scoreCardMap) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, false))) {
            for (Map.Entry<String, ScoreCard> entry : scoreCardMap.entrySet()) {
                ScoreCard scoreCard = entry.getValue();
                writer.println(scoreCard.toCSVString());
//                System.out.println("Serialized Successfully.. " + scoreCard);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e) {
        	System.out.println("Unknown Exception Occured.."+e.getMessage());
        }
    }
    
    public Map<String, ScoreCard> deserialize() {
        Map<String, ScoreCard> scoreCardMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ScoreCard scoreCard = ScoreCard.fromCSVString(line);
                scoreCardMap.put(scoreCard.getPlayerName(), scoreCard);
//                System.out.println("Deserialized successfully\n" + scoreCard);
            }
        } catch (FileNotFoundException e) {
//            System.out.println("File not found. Creating a new file.\nFor now the DATA is EMTY\n");
            return new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
        catch(Exception e) {
        	System.out.println("Caught an Exception e"+e.getMessage());
        	return new HashMap<>();
        }
        return scoreCardMap;
    }
    
    public void clearFile() {
        try (PrintWriter writer = new PrintWriter(FILE_PATH)) {
            // Just creating a new PrintWriter with the file path will clear the existing data.
            System.out.println("File cleared successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
