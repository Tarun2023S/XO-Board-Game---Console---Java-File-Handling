package App;

import java.util.*;

import App.GameManager.Log;

public class GameManager {
	private int currentSecond = 0;
	private int thresholdDrawTimeLimit = 30;
	public int getThresholdDrawTimeLimit() {
		return thresholdDrawTimeLimit;
	}

	public void setThresholdDrawTimeLimit(int thresholdDrawTimeLimit) {
		this.thresholdDrawTimeLimit = thresholdDrawTimeLimit;
	}
	private boolean isTimeLimitExceeded = false;
    public void setCurrentSecond(int currentSecond) {
		this.currentSecond = currentSecond;
	}
    private static int sizeOfXOBoard = 3;
    
	public static int getSizeOfXOBoard() {
		return sizeOfXOBoard;
	}

	public void setSizeOfXOBoard(int sizeOfXOBoard) {
		this.sizeOfXOBoard = sizeOfXOBoard;
		setThresholdDrawTimeLimit(sizeOfXOBoard * 10);
		System.out.println("Threshold: "+getThresholdDrawTimeLimit());
		xoArray = new int[sizeOfXOBoard][sizeOfXOBoard];
		System.out.println("sizeOfB: "+getSizeOfXOBoard());
	}
	
	public void setSizeOfXOBoard() {
		Scanner sc = new Scanner(System.in);
		int newSizeOfTheBoard = sizeOfXOBoard;
		boolean validInputChoice = false;
		while(!validInputChoice) {
			try {
				System.out.println("Enter the size of the board to play with ..");
				newSizeOfTheBoard = sc.nextInt();
				if(newSizeOfTheBoard<=2) {
					System.out.println(" Please enter an valid size for the board..");
					continue;
				}
				validInputChoice = true;
			}
			catch(Exception e) {
				System.out.println("\nPlease enter an valid input number..");
				sc.nextLine();
			}
		}
		setSizeOfXOBoard(newSizeOfTheBoard);
		playerRWiseMarked = new int[getSizeOfXOBoard()];
		playerCWiseMarked = new int[getSizeOfXOBoard()];
		botRWiseMarked = new int[getSizeOfXOBoard()];
		botCWiseMarked = new int[getSizeOfXOBoard()];
		System.out.println("sizeOfB: "+getSizeOfXOBoard());
	}

	static class Log {
        public Player getP1() {
			return p1;
		}
		public void setP1(Player p1) {
			this.p1 = p1;
		}
		public Player getP2() {
			return p2;
		}
		public void setP2(Player p2) {
			this.p2 = p2;
		}
		public Player getWhoWon() {
			return whoWon;
		}
		public void setWhoWon(Player whoWon) {
			this.whoWon = whoWon;
		}
		
		private Player p1, p2, whoWon;
        private boolean isDraw = false;
        
        public boolean isDraw() {
			return isDraw;
		}
		public void setDraw(boolean isDraw) {
			this.isDraw = isDraw;
		}
		public Log(Player p1, Player p2, Player whoWon) {
            this.p1 = p1;
            this.p2 = p2;
            this.whoWon = whoWon;
        }
    }

    Map<Integer, Integer> playerWinMap;
    Set<Player> listOfPlayers;
    static Stack<Log> stackHistory;
    static Map<String, ScoreCard> playerScoreCardMap;
    private FileManager fileManager;
    private BotMove botMove;
    public GameManager() {
        playerWinMap = new HashMap<>();
        listOfPlayers = new HashSet<>();
        stackHistory = new Stack<Log>();
        fileManager = new FileManager();
        botMove = new BotMove();
        playerScoreCardMap = new HashMap();
        
        playerRWiseMarked = new int[getSizeOfXOBoard()];
		 playerCWiseMarked = new int[getSizeOfXOBoard()];
		 botRWiseMarked = new int[getSizeOfXOBoard()];
		 botCWiseMarked = new int[getSizeOfXOBoard()];
    }
    
    
    private static int[][] xoArray = new int[sizeOfXOBoard][sizeOfXOBoard];

    private Player checkIfPlayerExists(String name) {
        Player t = null;
        for (Player p : listOfPlayers) {
            if (p.getName().equals(name)) {
                t = p;
                return t;
            }
        }
        return t;
    }

    void playGame(boolean dualWithBot) {
        Scanner sc = new Scanner(System.in);
        GameManager.playerScoreCardMap = fileManager.deserialize();
//        System.out.println(" Enter 1 -> To continue with default size of the board ("+getSizeOfXOBoard()+"), (or)\n Enter 2 -> To set the size of the board");
//        int choice = 0;
//        boolean validChoice = false;
//        while(!validChoice) {
//        	try {
//        		if(sc.hasNextInt()) {
//        			choice = sc.nextInt();
//        			 if (choice == 1) {
//        				 break;
//                     } else if(choice==2) {
//                    	 setSizeOfXOBoard();
//                         break;
//                     }
//                     else {
//                         System.out.println("Invalid choice. Please enter 1 or 2.");
//                     }
//        		}
//        		else {
//        			sc.next(); // Consume the invalid input
//                    System.out.println("Invalid input. Please enter a number.");
//        		}
//        	} catch (Exception e) {
//                System.out.println("Please try again.");
//                sc.next(); // Consume any remaining invalid input
//            }
//        }
        int n = -1;
        int choice = 0;
        boolean validChoice = false;

        while (!validChoice) {
            try {
                System.out.println("1. Timed Game");
                System.out.println("2. Untimed Game\n");
                System.out.println("Please select an option\n");

                if (sc.hasNextInt()) {
                    choice = sc.nextInt();

                    if (choice >= 1 && choice <= 2) {
                        validChoice = true;
                    } else {
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                    }
                } else {
                    sc.next(); // Consume the invalid input
                    System.out.println("Invalid input. Please enter a number.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred. Please try again.");
                sc.next(); // Consume any remaining invalid input
            }
        }

        
        System.out.println("Enter player 1 name\n");
        String player1 = sc.next();

        Player p1 = checkIfPlayerExists(player1);
        if (p1 == null) {
            p1 = new Player(player1);
            listOfPlayers.add(p1);
        }

        String player2 = "";
        if(!dualWithBot) {
        	 System.out.println("Enter player 2 name\n");
             player2 = sc.next();
        }
        else {
        	player2 = "BOT 2.0";
        }
       
        while(!dualWithBot && player1.equals(player2)) {
        	System.out.println("\nTHE TWO PLAYERS PLAYING, MUST HAVE DIFFERENT NAMES..\n");
        	System.out.println("Please try entering an Different name..\n");
        	player2 = sc.next();
        }
        Player p2 = checkIfPlayerExists(player2);
        if (p2 == null) {
            p2 = new Player(player2);
            listOfPlayers.add(p2);
        }
        System.out.println("P2: "+p2);
       
        startGame(p1, p2, choice, dualWithBot);
    }
    
    protected int getCurrentSecond() {
    	return currentSecond;
    }
    protected void startTimer() throws InterruptedException {
//    	int currentSecond = 0;
    	for (int seconds = 0; seconds <= thresholdDrawTimeLimit; seconds++) {
//            System.out.println("Timer: " + seconds + " seconds");
            currentSecond = seconds;
            if(isTimeLimitExceeded==true) {
            	Thread.currentThread().interrupt();
            	isTimeLimitExceeded = false;
            	System.out.println("TLE Get out: "+isTimeLimitExceeded);
            	return;
            }
            if(currentSecond >= thresholdDrawTimeLimit) {
            	Thread.currentThread().interrupt();
            	isTimeLimitExceeded = false;
            	throw new InterruptedException();
//            	throw new TimeLimitExceededException("Time Limit Exceeded from Start Timer");
            }
            try {
                // Sleep for 1 second (1000 milliseconds)
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            	System.out.println("Sllepp interrupetd");
            	return;
//                e.printStackTrace();
            }
        }
    	
//    	return currentSecond;
    }
    protected void startGame(Player p1, Player p2, int choice, boolean dualWithBot) {
        Scanner sc = new Scanner(System.in);
        isTimeLimitExceeded = false;
        showMatrix();
//        System.out.println("PS Size: "+playerScoreCardMap.size()+", "+playerScoreCardMap.get(p1.getName()));
        Thread t1 = null;
        if(choice==1) {
        	Runnable r = new Runnable() {
    			public void run() {
    				try {
						startTimer();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println(e.getMessage());
						isTimeLimitExceeded = true;
//						return;
					}
    			}
    		};
        	t1 = new Thread(r);
        	t1.start();        	
            
        }
        String undoChoice = "";
        int m = xoArray.length;
        int totalMoves = m * m;
        int minMovesToWin = (m + m) - 2;
        for (int i = 0; i < totalMoves; i++) {
            Player currentPlayer = (i % 2 == 0) ? p1 : p2;
           
            
            if((!dualWithBot) || (dualWithBot && i%2==0)) {
            	System.out.println("\n\tEnter the choice for " + currentPlayer.getName() +
                        " (As in coordinates => r,c )\n'U' -> FOR UNDO the Previous play");
            }

            int row = 0;
            int col = 0;
            boolean validInput = false;

            String coordinate = "";
            while (!validInput) {
                try {
                	if(isTimeLimitExceeded) {
                		markDraw(p1, p2);
                		throw new TimeLimitExceededException("Time Limit Exceeded from validInput LOOP");
                	}
                	if(dualWithBot && i%2!=0) {
                		int[] res = botMove.getBotMove(xoArray, 2, (i >= minMovesToWin-2));
                		if(res[0]==-1 ||res[1]==-1) {
                			System.out.println("All cells are marked, Its a draw");
                			markDraw(p1, p2);
                			return;
                		}
                		coordinate = res[0] + ","+res[1];
                		System.out.println("BOT's Move is: "+coordinate);
                	}
                	else {
                		coordinate = sc.next();
                	}
//                    System.out.println("Cood: "+coordinate);
                    if((i>0 && "U".equalsIgnoreCase(coordinate)) && ((!dualWithBot) || (dualWithBot && i%2==0))) {
                    	if(!undoChoice.isEmpty()) {
                    		row = Integer.valueOf(undoChoice.split(",")[0]);
                            col = Integer.valueOf(undoChoice.split(",")[1]);
                    		xoArray[row][col] = 0;
                    		i--;
                    		validInput = false;
                    		showMatrix();
                    		continue;
                    	}
                    }
                    row = Integer.valueOf(coordinate.split(",")[0]);
                    col = Integer.valueOf(coordinate.split(",")[1]);

                    validInput = isValid(row, col);
                    if (!validInput) {
                        System.out.println("\tPlease select a valid choice.");
                    }
                    else {
                    	System.out.println("XO I: "+i);
                        undoChoice = coordinate;
                        xoArray[row][col] = (i % 2 == 0) ? 1 : 2;
                        showMatrix();
                        
                        if(i>=xoArray.length-1) {
                        	int botSymbol = (i%2==0)? 1: 2;
                        	int antiBotSymbol = (botSymbol==1) ? 2: 1;
                        	markRowNColWiseCheck(xoArray, botSymbol);
                        	int markedCount = isAllMarked(botSymbol);
                        	int antiMarkedCount = isAllMarked(antiBotSymbol);
                        	System.out.println("MArked Count: "+markedCount);
                        	System.out.println("Anti-MArked Count: "+antiMarkedCount);
                            if(markedCount>=2*xoArray.length && antiMarkedCount>=2*xoArray.length) {
                            	System.out.println("Inside mC");
                            	if(xoArray[0][0]!=botSymbol && xoArray[0][xoArray.length-1]!=botSymbol) {
                            		markDraw(p1, p2);
                            		return;
                            	}
                            	boolean leftDiagCheck = false, rightDiagCheck = false;
                            	leftDiagCheck = leftDiagCheckHelper(botSymbol, xoArray);
                            	rightDiagCheck = rightDiagCheckHelper(botSymbol, xoArray);
                            	if(leftDiagCheck && rightDiagCheck) {
                            		markDraw(p1, p2);
                            		return;
                            	}
                            }
                        }
                    }
                } 
                catch(TimeLimitExceededException e) {
                	isTimeLimitExceeded = false;
                	System.out.println(isTimeLimitExceeded+" , "+e.getMessage());
                	clearXOBoard();
                	setSizeOfXOBoard(3);
                	System.out.println("Why return from?");
                	return;
                }
                catch (Exception e) {
                	System.out.println("BOT's Move is: "+coordinate);
                    System.out.println("\tInvalid input. Please enter coordinates in the format 'r,c'.");
                }
            }
            
            if(choice==1) {
            	if(getCurrentSecond()>=getThresholdDrawTimeLimit()) {
            		System.out.println("TIME LEFT: 0");
            		isTimeLimitExceeded = false;
            		markDraw(p1, p2);
            		return;
            	}
            	else {
            		System.out.println("Cuurent Second: "+getCurrentSecond());
            		System.out.println("TIME LEFT: "+(getThresholdDrawTimeLimit() - getCurrentSecond()));
            	}
            }
            boolean isFirstPlayer = (i%2)==0;
            if (i >= minMovesToWin && check(row, col, isFirstPlayer)) {
            	if(choice==1) {
            		t1.interrupt();
            	}
                Player loss1 = null;
                if (currentPlayer.getName().equals(p1.getName())) {
                	loss1 = p2;
                    int loss = p2.getTotalLosses();
                    System.out.println("Loss of p2: "+loss);
                    if(!playerScoreCardMap.containsKey(p2.getName())) 
                    	p2.setTotalLosses(loss + 1);
                    else 
                    	p2.setTotalLosses(playerScoreCardMap.get(p2.getName()).getNumberOfLosses());
                    int winForP1 = p1.getTotalWins();
                    if(!playerScoreCardMap.containsKey(p1.getName())) 
                    	p1.setTotalWins(winForP1+1);
                    else
                    	p1.setTotalWins(playerScoreCardMap.get(p1.getName()).getNumberOfWins());
                } else if (currentPlayer.getName().equals(p2.getName())) {
                	 loss1 = p1;
                     int loss = p1.getTotalLosses();
                     System.out.println("Loss of p1: "+loss);
                     if(!playerScoreCardMap.containsKey(p1.getName())) 
                    	 p1.setTotalLosses(loss + 1);
                     else
                    	 p1.setTotalLosses(playerScoreCardMap.get(p1.getName()).getNumberOfLosses());
                     int winForP2 = p2.getTotalWins();
                     if(!playerScoreCardMap.containsKey(p2.getName())) 
                    	 p2.setTotalWins(winForP2+1);
                     else 
                     	p2.setTotalWins(playerScoreCardMap.get(p2.getName()).getNumberOfWins());
                }
                System.out.println(currentPlayer.getName() + " wins");
                
//                if(choice==1) {   ////// NT NT
////                	if(getCurrentSecond()>=30) {
////                		isTimeLimitExceeded = false;
////                		System.out.println("TIME LEFT: 0 "+isTimeLimitExceeded);
////                		markDraw(p1, p2);
////                		return;
////                	}
////                	else {
////                		System.out.println("TIME LEFT: "+(getThresholdDrawTimeLimit() - getCurrentSecond()));
////                	}
//                }

                if (p2.getName().equals(currentPlayer.getName())) {
                    stackHistory.push(new Log(currentPlayer, p1, currentPlayer));
                } else {
                    stackHistory.push(new Log(currentPlayer, p2, currentPlayer));
                }   
                System.out.println("\n\n");    ////// NT NT
                
                //If p1 scoreCard already exists
                if(playerScoreCardMap.containsKey(p1.getName())) {
                	checkIfAlreadyExists(p1, loss1);
                }
                
                else  {
                	checkIfDoesNotExists(p1, loss1);
                }
              //If p2 scoreCard already exists
                if(playerScoreCardMap.containsKey(p2.getName())) {
                	checkIfAlreadyExists(p2, loss1);
                }
                else {
                	checkIfDoesNotExists(p2, loss1);
                }
//                System.out.println(playerScoreCardMap.get(p1.getName()));
//                System.out.println(playerScoreCardMap.get(p2.getName()));
                fileManager.clearFile();
                fileManager.serialize(playerScoreCardMap);
//                setSizeOfXOBoard(3); //After win, setting the default Board size (3*3)
                clearXOBoard();
//                showMatrix();
                isTimeLimitExceeded = false;
                return;
            }

        }
        markDraw(p1, p2);
    }

    private void markDraw(Player p1, Player p2) {
    	if(getCurrentSecond()>=getThresholdDrawTimeLimit()) {
    		System.out.println(" TIME LIMIT EXCEEDED\n");
    	}
    	System.out.println("\n It's a DRAW !!");
    	if(playerScoreCardMap.containsKey(p1.getName())) {
    		ScoreCard sc = playerScoreCardMap.get(p1.getName());
    		sc.setNumberOfDraws(sc.getNumberOfDraws()+1);
//    		System.out.println("New Draw for p1: "+sc.getNumberOfDraws());
    		playerScoreCardMap.put(p1.getName(), sc);
    	}
    	if(playerScoreCardMap.containsKey(p2.getName())) {
    		ScoreCard sc = playerScoreCardMap.get(p2.getName());
    		sc.setNumberOfDraws(sc.getNumberOfDraws()+1);
//    		System.out.println("New Draw for p2: "+sc.getNumberOfDraws());
    		playerScoreCardMap.put(p2.getName(), sc);
    	}
    	if(!playerScoreCardMap.containsKey(p1.getName())) {
    		playerScoreCardMap.put(p1.getName(), new ScoreCard(p1.getName(), p1.getTotalWins(), p1.getTotalLosses(), p1.getTotalDraws()+1));
    	}
    	if(!playerScoreCardMap.containsKey(p2.getName())) {
    		playerScoreCardMap.put(p2.getName(), new ScoreCard(p2.getName(), p2.getTotalWins(), p2.getTotalLosses(), p2.getTotalDraws()+1));
    	}
    	
        clearXOBoard();
        Log g = new Log(p1, p2, null);
        g.setDraw(true);
        stackHistory.push(g);
        
        fileManager.clearFile();
        fileManager.serialize(playerScoreCardMap);
        isTimeLimitExceeded = false;
//        setSizeOfXOBoard(3);
        return;
    }
    
    private void checkIfAlreadyExists(Player pn, Player loss1) {
    	ScoreCard previousScore = playerScoreCardMap.get(pn.getName());
    	int existingTotWins = previousScore.getNumberOfWins();
    	int existingLosses = previousScore.getNumberOfLosses(); //
    	int existingDraws = previousScore.getNumberOfDraws();
    	//If p1 loss
    	if(pn.getName() == loss1.getName()) {
//        	System.out.println(existingTotWins+", "+existingLosses+",LOSSp1 "+existingDraws);
        	playerScoreCardMap.put(pn.getName(), new ScoreCard(pn.getName(), existingTotWins, existingLosses+1, existingDraws));
        	pn.setTotalLosses(pn.getTotalLosses());
    	}
    	//If p1 wins
    	else {
//    		System.out.println(existingTotWins+", "+existingLosses+",WINp1 "+existingDraws);
        	playerScoreCardMap.put(pn.getName(), new ScoreCard(pn.getName(), existingTotWins+1, existingLosses, existingDraws));
        	pn.setTotalWins(pn.getTotalWins());
//        	System.out.println("Extrac check for pn loss: "+pn.getTotalLosses());
    	}
    }
    
    private void checkIfDoesNotExists(Player pn, Player loss1) {
    	ScoreCard newScoreCard = new ScoreCard(pn.getName(), pn.getTotalWins(), pn.getTotalLosses(), pn.getTotalDraws());
    	if(pn.getName() == loss1.getName()) {
        	playerScoreCardMap.put(pn.getName(), newScoreCard);
        	pn.setTotalLosses(pn.getTotalLosses());
    	}
    	else {
    		playerScoreCardMap.put(pn.getName(), newScoreCard);
        	pn.setTotalWins(pn.getTotalWins());
    	}
    }
    //TO CHECK, IF THE (r,c) IS WITHIN THE RANGE
    private boolean isValid(int row, int col) {
        return row >= 0 && row < xoArray.length && col >= 0 && col < xoArray.length && xoArray[row][col] == 0;
    }

    private boolean check(int row, int col, boolean isFirst) {
    	// Check the rows from left - ri8
    	int countR = 0;
    	int m = xoArray.length;
    	for(int i=0;i<m-1;i++) {
    		if(isFirst && xoArray[row][i]==1) {
    			if(xoArray[row][i] == xoArray[row][i+1]) {
        			countR++;
        		}
    		}
    		else if(!isFirst&& xoArray[row][i]==2) {
    			if(xoArray[row][i] == xoArray[row][i+1]) {
        			countR++;
        		}
    		}
    		else {
    			break;
    		}
    	}
    	if(countR==m-1) return true;
    	
        // Check the columns from top - bottom
    	int countC = 0;
    	for(int i=0;i<m-1;i++) {
    		if(isFirst && xoArray[i][col]==1) {
    			if(xoArray[i][col] == xoArray[i+1][col] && xoArray[i][col] != 0 && xoArray[i+1][col] != 0) {
        			countC++;
        		}
    		}
    		else if(!isFirst&& xoArray[i][col]==2) {
    			if(xoArray[i][col] == xoArray[i+1][col] && xoArray[i][col] != 0 && xoArray[i+1][col] != 0) {
        			countC++;
        		}
    		}
    		else {
    			break;
    		}
    		
    	}
    	if(countC==m-1) return true;

        // Check the diagonals from cross or mid
    	int leftDiag=0, rightDiag=0;
    	int r1=0, c1=0;
    	while(r1<m-1 && c1<m-1) {
    		if(isFirst && xoArray[r1][c1]==1 && xoArray[r1][c1]==xoArray[r1+1][c1+1]) {
    			leftDiag++;
    			r1++;
    			c1++;
    		}
    		else if(!isFirst && xoArray[r1][c1]==2 && xoArray[r1][c1]==xoArray[r1+1][c1+1]) {
    			leftDiag++;
    			r1++;
    			c1++;
    		}
    		else {
    			break;
    		}
    	}
 	
    	if(leftDiag==m-1) return true;
    	
    	r1=0;
    	c1=m-1;
    	while(r1<m-1 && c1>0) {
    		if(isFirst && xoArray[r1][c1]==1 && xoArray[r1][c1]==xoArray[r1+1][c1-1]) {
    			rightDiag++;
    			r1++;
    			c1--;
    		}
    		else if(!isFirst && xoArray[r1][c1]==2 && xoArray[r1][c1]==xoArray[r1+1][c1-1]) {
    			rightDiag++;
    			r1++;
    			c1--;
    		}
    		else {
    			break;
    		}
    	}
    	if(rightDiag==m-1) return true;
        return false;
    }

    
    private void showMatrix() {
    	System.out.println("Size of Board: "+getSizeOfXOBoard());
    	System.out.println("r: "+xoArray.length+", c: "+xoArray[0].length);
//        System.out.println("\n\t  0   1   2");
//        System.out.println("\t +---+---+---+");
        
//        System.out.println();
        System.out.println("\n");
        System.out.print("\t ");
        for(int i=0;i<getSizeOfXOBoard();i++) {
        	System.out.print("  "+i+" ");
        }
        System.out.println();
        System.out.print("\t +");
        for(int i=0;i<getSizeOfXOBoard();i++) {
        	System.out.print("---+");
        }
        System.out.println();
        for (int i = 0; i < xoArray.length; i++) {
            System.out.print("\t" + i + "|");
            for (int j = 0; j < xoArray[i].length; j++) {
                if (xoArray[i][j] == 1) {
                    System.out.print(" X |");
                } else if (xoArray[i][j] == 2) {
                    System.out.print(" O |");
                } else {
                    System.out.print("   |");
                }
            }
//            System.out.println("\n\t +---+---+---+");
            System.out.println();
            System.out.print("\t +");
            for(int j=0;j<getSizeOfXOBoard();j++) {
            	System.out.print("---+");
            }
            System.out.println();
        }
    }

    //Clearing the Board
    private void clearXOBoard() {
        for (int i = 0; i < xoArray.length; i++) {
            for (int j = 0; j < xoArray[i].length; j++) {
                xoArray[i][j] = 0;
            }
            playerRWiseMarked[i] = 0;
            playerCWiseMarked[i] = 0;
            botRWiseMarked[i] = 0;
            botCWiseMarked[i] = 0;
        }
    }
    
    protected void printScoreCard() {
        String[] headers = {"PLAYER NAME", "NO OF WINS", "NO OF LOSSES", "NO OF DRAWS"};
        int[] columnWidths = {15, 20, 20, 20};
        // Deserialize ScoreCards
        playerScoreCardMap = fileManager.deserialize();
//        fileManager.clearFile();
        if (playerScoreCardMap == null || playerScoreCardMap.isEmpty()) {
            System.out.println("No data available");
            return;
        }

//        System.out.println("sizeOFMAp: " + playerScoreCardMap.size());

        // PREPARE DATA FOR THE TABLE
        String[][] data = new String[playerScoreCardMap.size()][4];
        int idx = 0;

        for (String name : playerScoreCardMap.keySet()) {
            String val1 = name;
            int val2 = playerScoreCardMap.get(name).getNumberOfWins();
            int val3 = playerScoreCardMap.get(name).getNumberOfLosses();
            int val4 = playerScoreCardMap.get(name).getNumberOfDraws();
            data[idx][0] = val1;
            data[idx][1] = String.valueOf(val2);
            data[idx][2] = String.valueOf(val3);
            data[idx][3] = String.valueOf(val4);
            idx++;
        }

        // PRINTING THE TABLE
        Main.printTable(headers, columnWidths, data);
    }

    // TO VIEW ALL THE PLAYERS
    void printAllPlayers() {
        System.out.println("\t" + Main.printBoundary(6) + "\t\t" + Main.printBoundary(10));
        System.out.println("\t  ID" + "\t\t  NAME");
        System.out.println("\t" + Main.printBoundary(6) + "\t\t" + Main.printBoundary(10));
        if (listOfPlayers.size() == 0) {
            System.out.println("\t  THERE ARE NO PLAYERS AVAILABLE\t\t\n");
            return;
        }
        for (Player p : listOfPlayers) {
            System.out.println("\t  " + p.getPlayerId() + "\t\t  " + p.getName());
        }
    }

    // TO GET A PARTICULAR PLAYER BY NAME
    protected Player getPlayer(String playerName) {
        clearXOBoard();
        Player currentPlayer = null;
        while (currentPlayer == null) {
            for (Player p : listOfPlayers) {
                if (p.getName().equals(playerName)) {
                    currentPlayer = p;
                    break;
                }
            }
            if (currentPlayer == null) {
                System.out.println("\n\tPlease enter a valid name..\n");
            }
        }
        return currentPlayer;
    }
    
    
    static int isAllMarked(int symbol) {
		 int antiSym = (symbol==2) ? 1: 2;
		 int rowCount=0, colCount=0;
		 for(int i=0;i<playerRWiseMarked.length;i++) {
			 if(symbol==2) {
				 if(playerRWiseMarked[i]==1) {
					 rowCount++;
				 }
				 if(playerCWiseMarked[i]==1) {
					 colCount++;
				 }
			 }
			 else if(symbol==1) {
				 if(botRWiseMarked[i]==1) {
					 rowCount++;
				 }
				 if(botCWiseMarked[i]==1) {
					 colCount++;
				 }
			 }
		 }
		 return rowCount+colCount;
	 }
    
    static int[] playerRWiseMarked;
	static int[] playerCWiseMarked;
	 
	static int[] botRWiseMarked;
	static int[] botCWiseMarked;
    static boolean leftDiagCheckHelper(int symbol, int[][] board) {
		 int i=0, j=0;
		 while(i<board.length && j<board[0].length) {
			 if(board[i][j]!=symbol && board[i][j]!=0) {
				 return true;
			 }
			 i++;
			 j++;
		 }
		 return false;
	 }
	 
	 static boolean rightDiagCheckHelper(int symbol, int[][] board) {
		 int i=0, j=board.length-1;
		 while(i<board.length && j>=0) {
			 if(board[i][j]!=symbol && board[i][j]!=0) {
				 return true;
			 }
			 i++;
			 j--;
		 }
		 return false;
	 }
	 
	 static void markRowNColWiseCheck(int[][] board, int symbol) {
		 for(int i=0;i<board.length;i++) {
			 for(int j=0;j<board[i].length;j++) {
				 if(board[i][j]==1) {
//	    	        	System.out.println("In player wise check..");
	    	        	playerRWiseMarked[i] = 1;
	    	        	playerCWiseMarked[j] = 1;
	    	        }
	    	        if(board[i][j]==2) {
	    	        	botRWiseMarked[i] = 1;
	    	        	botCWiseMarked[j] = 1;
	    	        }
			 }
		 }
	 }
}

class TimeLimitExceededException extends Exception {
	public TimeLimitExceededException(String msg) {
		super(msg);
	}
}

