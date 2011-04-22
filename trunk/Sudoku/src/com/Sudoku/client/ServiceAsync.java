package com.Sudoku.client;

import com.Sudoku.shared.Sudoku;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface ServiceAsync {

	void test(AsyncCallback<Void> callback);

	void isSudoku(int[][] s, AsyncCallback<Sudoku> callback);

}
