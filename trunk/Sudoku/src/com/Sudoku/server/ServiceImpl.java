package com.Sudoku.server;

import com.Sudoku.client.Service;
import com.Sudoku.shared.Sudoku;
import com.Sudoku.shared.Sudokus;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ServiceImpl extends RemoteServiceServlet implements Service {

	public void test() throws IllegalArgumentException {

		Sudoku s = new Sudoku(Sudokus.s6);
		s.solve(500);
		System.out.println(s.toString());
		System.out.println(s.toStringPosibles());

	}

	@Override
	public com.Sudoku.shared.Sudoku isSudoku(int[][] s) {
		com.Sudoku.shared.Sudoku w = new com.Sudoku.shared.Sudoku(s);
		
		if (w.isValid()) {
			return w;
		}
		return null;
	}

}
