package App;

import java.util.Arrays;

public class BotMove {
	 private static boolean isLeftDiagonalMarked = false;
	 private static boolean isRightDiagonalMarked = false;

	 public BotMove() {
		 int n = GameManager.getSizeOfXOBoard();
	 }
	 int[] getBotMove(int[][] board, int botSymbol, boolean startCheckForOneMoveToWin) {
		 
        int[] result = new int[]{-1, -1};
        
        // Check for a winning move for the bot
        if(startCheckForOneMoveToWin) {
        	System.out.println("In check 1");
        	result = checkWinningMove2(board, botSymbol);
        }
        
        if (startCheckForOneMoveToWin && result[0] == -1 && result[1] == -1) {
            // If no winning move, check for a blocking move for the player
            int playerSymbol = (botSymbol == 1) ? 2 : 1;
            result = checkWinningMove2(board, playerSymbol);
        }
//        GameManager.markRowNColWiseCheck(board, botSymbol);
        System.out.println("PlayerRwiseCheck: "+Arrays.toString(GameManager.playerRWiseMarked));
        System.out.println("PlayerCwiseCheck: "+Arrays.toString(GameManager.playerCWiseMarked));
        // If no winning or blocking move, choose the adjacent max consecutive 2's available in an empty cell
        //and it should be a non blocked cell
       
        if (result[0] == -1 || result[1] == -1) {
        	result = getNextToMaxConsecutiveTwosWithNonBlockedCell(board, 2);
        }
        //Else check same for the player position
        if (result[0] == -1 || result[1] == -1) {
        	result = getNextToMaxConsecutiveTwosWithNonBlockedCell(board, 1);
        }
        
        // If no winning or blocking move, choose the adjacent max consecutive 1's available in an empty cell
        if (result[0] == -1 || result[1] == -1) {
        	result = getNextToMaxConsecutiveTwos(board, 1);
        }
        
        // If no winning or blocking move, choose the adjacent max consecutive 2's available in an empty cell
        if (result[0] == -1 || result[1] == -1) {
        	result = getNextToMaxConsecutiveTwos(board, 2);
        }
        
        //Checking for draw positions
        GameManager.markRowNColWiseCheck(board, botSymbol);
        int antiBotSymbol = (botSymbol==1) ? 2: 1;
        int markedCount = GameManager.isAllMarked(botSymbol);
        int antiMarkedCount = GameManager.isAllMarked(antiBotSymbol);
        if(markedCount>=2*board.length && antiMarkedCount>=2*board.length) {
        	if(board[0][0]!=botSymbol && board[0][board.length-1]!=botSymbol) {
        		return new int[] {-1, -1};
        	}
        	boolean leftDiagCheck = false, rightDiagCheck = false;
        	leftDiagCheck = GameManager.leftDiagCheckHelper(botSymbol, board);
        	rightDiagCheck = GameManager.rightDiagCheckHelper(botSymbol, board);
        	if(leftDiagCheck && rightDiagCheck) return new int[] {-1, -1};
        }
        
        
        // If no winning or blocking move, choose the first available empty cell
        if (result[0] == -1 || result[1] == -1) {
        	System.out.println("Deep arr: "+Arrays.deepToString(board));
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                	System.out.println("In nowinnolossMove");
//                	System.out.println("b[i][j]: "+board[i][j]);
                    if (board[i][j] == 0) {
                    	System.out.println("In bb[0]: "+board[0]);
                    	System.out.println("In bb[0]: "+board[1]);
                    	result[0] = i;
                    	result[1] = j;
//                    	isLeftDiagonalMarked = (i==j)? true: false;
                    	return result;
                    }
                }
            }
        }
        for(int[] r: board) {
        	System.out.println(Arrays.toString(r));
        }
        
        return result;
    }
	
	 private int[] checkWinningMove2(int[][] board, int symbol) {
	    	int consec = 0, noOfSymbols = 0, missing = -1, gap = -1;
	    	int res[] = new int[2];
	    	res[0] = -1;
	    	res[1] = -1;
	    	int expectedNumberOfSymbols = board[0].length - 1;
	    	
	    	// ROW WISE CHECK
	    	for (int i = 0; i < board.length; i++) {
	    	    consec = 0;
	    	    noOfSymbols = 0;
	    	    missing = -1;
	    	    gap = -1;

	    	    // ROW WISE CHECK GAP
	    	    for (int j = 0; j < board[i].length; j++) {
	    	        if (board[i][j] == symbol) {
	    	            noOfSymbols++;
	    	        } else if (board[i][j] == 0) {
	    	            gap = j;
	    	        }
	    	    }

	    	    if (noOfSymbols == expectedNumberOfSymbols && gap != -1) {
	    	        System.out.println("IN gap sym");
	    	        if (board[i][gap] == 0) {
	    	            res[0] = i;
	    	            res[1] = gap;
//	    	            System.out.println("Res: Through Rgap1 " + Arrays.toString(res));
	    	            return res;
	    	        }
	    	    }
	    	}

	    	// COLUMN WISE CHECK
	    	for (int j = 0; j < board[0].length; j++) {
	    	    consec = 0;
	    	    noOfSymbols = 0;
	    	    missing = -1;
	    	    gap = -1;

	    	    // COLUMN WISE CHECK GAP
	    	    for (int i = 0; i < board.length; i++) {
	    	        if (board[i][j] == symbol) {
	    	            noOfSymbols++;
	    	        } else if (board[i][j] == 0) {
	    	            gap = i;
	    	        }
	    	    }

	    	    if (noOfSymbols == expectedNumberOfSymbols && gap != -1) {
	    	        System.out.println("IN gap sym");
	    	        if (board[gap][j] == 0) {
	    	            res[0] = gap;
	    	            res[1] = j;
	    	            return res;
	    	        }
	    	    }
	    	}

	    	// Check diagonals
	        int r=0, c=0;
	        noOfSymbols = 0;
	        int totalRows = board.length, totalCols = board[0].length;
	        int[] miss = {-1, -1};
	        System.out.println("Diag wise Check");
	        while(r<totalRows && c<totalCols) {
	        	if(board[r][c]==symbol) {
	        		noOfSymbols++;
	        	}
	        	else {
	        		miss[0] = r;
	        		miss[1] = c;
	        	}
	        	if(board[r][c]==1) isLeftDiagonalMarked = true;
	        	r++;
	        	c++;
	        }
	        if (noOfSymbols == expectedNumberOfSymbols && miss[0] != -1) {
		        System.out.println("IN gap sym");
		        if (board[miss[0]][miss[1]] == 0) {
		            return miss;
		        }
		    }
	        
	        r=0;
	        c=board.length-1;
	        noOfSymbols = 0;
	        miss[0] = -1;
	        miss[1] = -1;
	        System.out.println("Diag wise Check 2");
	        while(r>=0 && c>=0) {
	        	if(board[r][c]==symbol) {
	        		noOfSymbols++;
	        		System.out.println("r: "+r+", c: "+c);
	        	}
	        	else {
	        		miss[0] = r;
	        		miss[1] = c;
	        	}
	        	if(board[r][c]==1) isRightDiagonalMarked = true;
	        	r++;
	        	c--;
	        }
	        
	        System.out.println("noOfSym: "+noOfSymbols);
	        if (noOfSymbols == expectedNumberOfSymbols && miss[0] != -1) {
		        System.out.println("IN gap sym");
		        if (board[miss[0]][miss[1]] == 0) {
		            return miss;
		        }
		    }
	        return res;
	    }
	 
	 private static int[] getNextToMaxConsecutiveTwosWithNonBlockedCell(int[][] board, int symbol) {
		    int maxConsecutiveTwos = -1;
		    int[] result = new int[] {-1, -1};
		    int[] maxConsecSym = new int[1];
		    int antiPos = (symbol==2) ? 1: 2;
		 // Iterate over the board to find the cell with the maximum consecutive 2's which the !symbol has not marked
		    for (int i = 0; i < board.length; i++) {
		        for (int j = 0; j < board[0].length; j++) {
		        	if(symbol==2) {
		        		 if(board[i][j] == 0 && GameManager.playerRWiseMarked[i]==0 && GameManager.playerCWiseMarked[j]==0) {
				            	countConsecutiveTwos2(board, i, j, symbol, result, maxConsecSym);
				            }
		        	}
		        	else if(symbol==1) {
		        		 if(board[i][j] == 0 && GameManager.botRWiseMarked[i]==0 && GameManager.botCWiseMarked[j]==0) {
				            	countConsecutiveTwos2(board, i, j, symbol, result, maxConsecSym);
				            }
		        	}
		        }
		    }

		    return result;
		}
	 
	 private static int[] getNextToMaxConsecutiveTwos(int[][] board, int symbol) {
		    int maxConsecutiveTwos = -1;
		    int[] result = new int[] {-1, -1};
		    int[] maxConsecSym = new int[1];

		    // Iterate over the board to find the cell with the maximum consecutive 2's
		    for (int i = 0; i < board.length; i++) {
		        for (int j = 0; j < board[0].length; j++) {
		            if (board[i][j] == 0) {
		            	countConsecutiveTwos2(board, i, j, symbol, result, maxConsecSym);
		            }
		        }
		    }

		    return result;
		}

	 private static void countConsecutiveTwos2(int[][] board, int row, int col, int symbol, int[] pos, int[] maxConecSym) {
			int tempR = row, tempC = col, r = 0, c = 0, totalRowCount=0, totalColCount=0;
			int consecSym=0;
			int leftDiag=0, rightDiag=0;
			
			//Column wise check from the current idx
			while(tempR-1>=0 && board[tempR-1][col]==2) {
				tempR--;
				c++;
			}
			tempC = col;
			while(tempR+1<board.length && board[tempR][col]==2) {
				tempR++;
				c++;
			}
			totalColCount = Math.max(totalColCount, c);

			consecSym = Math.max(consecSym, Math.max(totalRowCount, totalColCount));
			if(consecSym > maxConecSym[0]) {
				pos[0] = row;
				pos[1] = col;
				maxConecSym[0] = consecSym;
			}
			
			//Row wise check from the current idx
			while(tempC-1>=0 && board[row][tempC-1]==2) {
				tempC--;
				r++;
			}
			tempC = col;
			while(tempC+1<board.length && board[row][tempC+1]==2) {
				tempC++;
				r++;
			}
			totalRowCount = Math.max(totalRowCount, r);
			
			
			
			//If its a part of the diagonals
			int[] miss = new int[] {-1, -1};
			if(row==col && !isLeftDiagonalMarked) {
				tempR=0;
				tempC=0;
		        System.out.println("Diag wise Check 2");
		        while(tempR<board.length && tempC<board.length) {
		        	if(board[tempR][tempC]==symbol) {
		        		leftDiag++;
		        	}
		        	else if(board[tempR][tempR]==0){
		        		miss[0] = tempR;
		        		miss[1] = tempC;
		        	}
		        	tempR++;
		        	tempC++;
		        }
		        if(leftDiag > maxConecSym[0] && miss[0]!=-1) {
		        	pos[0] = miss[0];
					pos[1] = miss[1];
					maxConecSym[0] = leftDiag;
				}
			}
			
			
			if(row+col==board.length-1 && !isRightDiagonalMarked) {
				tempR=0;
				tempC=board.length-1;
				miss = new int[] {-1, -1};
				while(tempR<board.length && tempC==0) {
		        	if(board[tempR][tempC]==symbol) {
		        		rightDiag++;
//		        		System.out.println("r: "+r+", c: "+c);
		        	}
		        	else if(board[tempR][tempC]==0){
		        		miss[0] = tempR;
		        		miss[1] = tempC;
		        	}
		        	tempR++;
		        	tempC--;
		        }
				if(rightDiag > maxConecSym[0] && miss[0]!=-1) {
		        	pos[0] = miss[0];
					pos[1] = miss[1];
					maxConecSym[0] = rightDiag;
				}
			}
			if(pos[0]==-1 || pos[1]==-1) {
				pos[0] = row;
				pos[1] = col;
			}
		}
}

