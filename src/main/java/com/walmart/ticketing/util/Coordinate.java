package com.walmart.ticketing.util;

public class Coordinate {

	int row;
	int column;
	
	public Coordinate(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	
	public String toString() {
		return "col=" + column + ";row=" + row;
	}
	
	
	
}
