package com.Sudoku.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface ServiceAsync {

	void test(AsyncCallback<Void> callback);

	void isSudoku(int[][] s, AsyncCallback<com.Sudoku.shared.Sudoku> callback);

	void fuerzaBruta(int[][] s, AsyncCallback<com.Sudoku.shared.Sudoku> callback);

	void aproximacion(int[][] s,
			AsyncCallback<com.Sudoku.shared.Sudoku> callback);

	void mmmm(int[][] s, AsyncCallback<com.Sudoku.shared.Sudoku> callback);

}
