package App;

import java.util.Scanner;

public class Main {

    private static int validChoices() {
        int choice = 0;
        System.out.println("\t\t" + printBoundary(20));
        System.out.println("\n\t\t Enter Your Choice\n");
        System.out.println("\t\t" + printBoundary(20));
        System.out.println("  1. PLAY GAME\n  2. PRINT ALL SCORE CARDS\n  3. DUAL WITH BOT \n  4. SET BOARD SIZE (n*n)\n  5. EXIT\n");
        lastPlayedGameInfo();
        Scanner sc = new Scanner(System.in);
        try {
            choice = sc.nextInt();
        } catch (Exception e) {
            System.out.println(" Please enter a valid choice\n");
            validChoices();
        }
        return choice;
    }

//    PRINT THE DATA IN A TABLE FORMAT
    static void printTable(String[] headers, int[] columnWidths, String[][] data) {
        int totalWidth = 1; // Start with 1 to account for the vertical border
        for (int width : columnWidths) {
            totalWidth += width + 3; // Add 3 for the vertical border and two spaces
        }
        System.out.println(printBoundary(totalWidth));

        // Print headers
        System.out.print("|");
        for (int i = 0; i < headers.length; i++) {
            System.out.format(" %-" + (columnWidths[i] - 1) + "s |", headers[i]); // Adjusted width
        }
        System.out.println();

        System.out.println(printBoundary(totalWidth));

        // PRINTING THE DATA ROWS
        for (String[] row : data) {
            System.out.print("|");
            for (int i = 0; i < row.length; i++) {
                System.out.format(" %-" + (columnWidths[i] - 1) + "s |", row[i]); // Left Adjusting the width
            }
            System.out.println();
        }

        System.out.println(printBoundary(totalWidth));
    }


//	TO GET THE LAST PLAYED GAME INFO
    private static void lastPlayedGameInfo() {
        System.out.println("\t\t" + printBoundary(26));
        System.out.println("\t\t|  Last Played Game Info  |\t\t");
        System.out.println("\t\t" + printBoundary(26));
        System.out.println();
        if (GameManager.stackHistory.isEmpty()) {
            System.out.println("\t\tThere were no last played games..");
        } else {
        	GameManager.Log obj = GameManager.stackHistory.peek();
        	String[] headers = {"Player name", "Opponent name", "Winner"};
            int[] columnWidths = {13, 17, 10};
            String[][] data = new String[1][3];
            data[0][0] = obj.getP1().getName();
            data[0][1] = obj.getP2().getName();
        	if(obj.isDraw()) {
        		headers[2] = "Draw";
        		data[0][2] = "It's a TIE";
                printTable(headers, columnWidths, data);
        	}
        	else {
        		data[0][2] = obj.getWhoWon().getName();
                printTable(headers, columnWidths, data);
        	}
            
        }
    }

//    TO PRINT THE OUTER BOUNDARY (+---+)
    static String printBoundary(int a) {
        return "+" + String.valueOf('-').repeat(a - 2) + "+";
    }

    public static void main(String[] args) {
        boolean loop = true;
        FileManager fm = new FileManager();
        GameManager gm = new GameManager();
        Scanner sc = new Scanner(System.in);
        while (loop) {
            int choice = validChoices();
            switch (choice) {
                case 1:
                    gm.playGame(false);
                    break;
                case 2:
                    gm.printScoreCard();
                    break;
                case 3:
                	System.out.println("Gonna play with bot'");
                	gm.playGame(true);
                    break;
                case 4:
                	gm.setSizeOfXOBoard();
                	break;
                case 5:
                	fm.serialize(GameManager.playerScoreCardMap);
                    System.out.println("\t\tThank you for your time..");
                    loop = false;
                    break;
                default:
                    System.out.println("Please enter a valid case number");
                    break;
            }
        }
    }
}
