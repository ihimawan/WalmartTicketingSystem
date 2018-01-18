package com.walmart.ticketing.util;

import java.util.ArrayList;
import java.util.List;

import com.walmart.ticketing.model.Seat;
import com.walmart.ticketing.util.Factor.FactorPair;

public class SeatSolver {

	public static List<Coordinate> getBestSeats(int numOfSeats, int maxVer, int maxHor, List<Seat> availableSeats)
			throws Exception {
		
		if (numOfSeats == 0) {
			throw new Exception("0 number of seats cannot be booked.");
		}

		if (numOfSeats > availableSeats.size()) {
			throw new Exception("Unable to get best seats. There are only " + availableSeats.size()
					+ " available while " + numOfSeats + " is requested.");
		}

		boolean[][] venueLayout = new boolean[maxVer][maxHor];

		for (Seat seat : availableSeats) {
			venueLayout[seat.getRow()][seat.getColumn()] = true;
		}

		List<FactorPair> proposedDimensions = (new Factor(numOfSeats)).getFactorPairs();

		for (FactorPair proposedDimension : proposedDimensions) {
			System.out.println(proposedDimension.hSpread + " " + proposedDimension.vSpread + " " + !(proposedDimension.hSpread >= maxHor && proposedDimension.vSpread >= maxVer));
			if (!(proposedDimension.hSpread >= maxHor && proposedDimension.vSpread >= maxVer)) {
				List<Coordinate> result = placeSeatGroup(venueLayout, proposedDimension.hSpread, proposedDimension.vSpread);
				if (result != null) { //if proposed dimension fits into the venue, then return. Otherwise, keep looking
					return result;
				}
			}
		}
		
		//Not found using our algorithm.
		throw new Exception("Sorry, unable to get best seats using our algorithm.. The party would need to be separated.");
	}

	private static List<Coordinate> placeSeatGroup(boolean[][] venueLayout, int hSpread, int vSpread) {

		int maxRow = venueLayout.length;
		int maxCol = venueLayout[0].length;

		List<Coordinate> result = null;

		for (int rowNum = 0; rowNum < maxRow; rowNum++) {

			System.out.println("ROW - " + rowNum);
			
			// if vertical spread is not going to fit, don't bother to keep looking.
			if (rowNum + vSpread > maxRow) {
				break;
			}

			int firstAvailCol = firstAvailableColNumInRow(venueLayout, rowNum);
			
			System.out.println("Firstavailcol-" + firstAvailCol);
			System.out.println(firstAvailCol != -1 && firstAvailCol + hSpread <= maxCol);

			// if there is first available column in row (row is not full) && if horizontal spread will fit
			if (firstAvailCol != -1 && firstAvailCol + hSpread <= maxCol) {

				List<Coordinate> horizontalCoor = getHorizontalSpread(venueLayout, hSpread, rowNum, firstAvailCol);

				// if unable to find required spots on horizontal spread, move on to next row;
				// if able then try to find vertical spread
				if (horizontalCoor != null) {

					System.out.println(horizontalCoor);
					
					List<Coordinate> verticalCoor = new ArrayList<Coordinate>();
					for (Coordinate coor : horizontalCoor) {
						List<Coordinate> verticalCoorPiece = getVerticalSpread(venueLayout, vSpread-1, coor.getColumn(),
								coor.getRow()+1);
						
						System.out.println("vert-" + verticalCoorPiece);

						// the vertical spread is disturbed. move on to next row
						if (verticalCoorPiece == null) {
							break;
						} else {
							verticalCoor.addAll(verticalCoorPiece);
						}
					}

					// if both horizontal spread and vertical spread is undisturbed
					result = (new ArrayList<Coordinate>());
					result.addAll(horizontalCoor);
					result.addAll(verticalCoor);

					return result;
				}
			}
		}

		return null;
	}

	private static List<Coordinate> getHorizontalSpread(boolean[][] venueLayout, int hSpread, int rowNum,
			int firstAvailCol) {

		List<Coordinate> res = new ArrayList<>();

		for (int colNum = firstAvailCol; colNum < firstAvailCol + hSpread; colNum++) {
			if (!venueLayout[rowNum][colNum]) {
				return null;
			}
		}

		for (int colNum = firstAvailCol; colNum < firstAvailCol + hSpread; colNum++) {
			// OTHERWISE, add [rowNum, firstAvailCol]...[rowNum, firstAvailCol+hSpread]
			System.out.println("ColNum-" + rowNum + " " + colNum);
			res.add(new Coordinate(rowNum, colNum));
		}

		System.out.println(res);
		return res;
	}

	private static List<Coordinate> getVerticalSpread(boolean[][] venueLayout, int vSpread, int colNum, int rowStart) {

		List<Coordinate> res = new ArrayList<>();

		for (int rowNum = rowStart; rowNum < rowStart + vSpread; rowNum++) {
			// disturbed vertical spread
			if (!venueLayout[rowNum][colNum]) {
				return null;
			}
		}

		for (int rowNum = rowStart; rowNum < rowStart + vSpread; rowNum++) {
			// OTHERWISE, add [rowNum, colNum]...[rowNum+vSpread, colNum]
			System.out.println("RowNum-" + new Coordinate(rowNum, colNum));
			res.add(new Coordinate(rowNum, colNum));
		}

		return res;
	}

	private static int firstAvailableColNumInRow(boolean[][] venueLayout, int rowNum) {
		boolean[] row = venueLayout[rowNum];
		for (int i = 0; i < row.length; i++) {
			if (row[i] == true) {
				return i;
			}
		}
		return -1;
	}

//	private static List<Coordinate> allocateContiguosly(boolean[][] venueLayout, int num) {
//		return null;
//
//	}
}

class Factor {

	class FactorPair {
		int vSpread; // potential vertical spread
		int hSpread; // potential horizontal spread

		public FactorPair(int vSpread, int hSpread) {
			super();
			this.hSpread = hSpread;
			this.vSpread = vSpread;
		}
	}

	private List<FactorPair> factorPairs = new ArrayList<>();

	public Factor(int num) {
		
		if (num == 0) {
			factorPairs.add(new FactorPair(0, 0));
			return;
		}

		for (int i = 1; i <= num; i++) {
			if (num % i == 0 && num / i != 1) {
				factorPairs.add(new FactorPair(i, num / i));
			}
		}
	}

	public List<FactorPair> getFactorPairs() {
		return factorPairs;
	}

	public void setFactorPairs(List<FactorPair> factorPairs) {
		this.factorPairs = factorPairs;
	}

}
