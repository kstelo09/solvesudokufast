package com.Sudoku.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("s")
public interface Service extends RemoteService {
	void test() throws IllegalArgumentException;

	com.Sudoku.shared.Sudoku isSudoku(int [][] s);
}
