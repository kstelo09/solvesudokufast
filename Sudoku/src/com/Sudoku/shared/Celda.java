package com.Sudoku.shared;

import java.io.Serializable;

public class Celda implements Serializable {

	private static final long serialVersionUID = 8379004402745691071L;
	/**
	 * valor que indica si esta celda ha sido resualta y si se le ha fijado
	 * algún valor.
	 */
	private boolean isSet = false;
	/**
	 * Valor que tiene esta celda en caso de que halla sido fijada. Este valor
	 * debe de estar entre 1 y 9.
	 */
	private int valor;
	/**
	 * Arreglo de números booleanos que indica que valores podria tomar esta
	 * celda tomando en cuenta la fila, columna y cuadro en la que se encuentra
	 * en el sudoku. si el valor de posibles[0]= true quiere decir que esta
	 * celda puede tomar el valor 1.
	 */
	private boolean[] posibles;

	/**
	 * Constructor por defecto
	 */
	public Celda() {
		posibles = new boolean[9];
		for (int i = 0; i < 9; i++) {
			posibles[i] = true;
		}
		valor = 0;
	}

	/**
	 * Contructor con valor de entrada
	 * 
	 * @param valor
	 *            el valor a fijar a la celda resuelta.
	 */
	public Celda(int valor) {
		this.valor = valor;
		isSet = true;
		posibles = new boolean[9];
		for (int i = 0; i < 9; i++) {
			posibles[i] = false;
		}
	}

	/**
	 * 
	 * @return true en caso de que la celda este resulta, false de lo contrario.
	 */
	public boolean isSet() {
		return isSet;
	}

	/**
	 * Fija como posible solución para esta celda al número recibido como
	 * parámetro.
	 * 
	 * @param posible
	 *            numero de 1 al 9
	 */
	public void setPosible(int posible) {
		posibles[posible - 1] = true;
	}

	/**
	 * Remueve al número recibido como parámetro como posible solución para esta
	 * celda.
	 * 
	 * @param posible
	 *            numero de 1 al 9
	 */
	public void setNotPosible(int posible) {
		posibles[posible - 1] = false;
	}

	/**
	 * 
	 * @return el valor fijado para esta celda en caso que ya halla sido
	 *         resuleta, de lo contrario 0.
	 */
	public int getValor() {
		return valor;
	}

	/**
	 * Fija el valor resultado para esta celda
	 * 
	 * @param valor
	 *            el resultado numerico del 1 al 9
	 */
	public void setValor(int valor) {
		this.valor = valor;
		isSet = true;
		for (int i = 0; i < 9; i++) {
			posibles[i] = false;
		}
	}

	/**
	 * Obtiene un arreglo de booleanos de 9 elementos en el que si la pocisi�n 0
	 * del arreglo es true entonces 1 es una solución posible para esta celda.
	 * 
	 * @return el arreglo de booleanos descrito
	 */
	public boolean[] getPosibles() {
		return posibles;
	}

	/**
	 * 
	 * @return el número de soluciones posibles para esta celda, 0 en caso de
	 *         que ya este resuelta.
	 */
	public int getNumberOfPossibleSolutions() {
		if (isSet) {
			return 0;
		} else {
			int retorno = 0;
			for (int i = 0; i < 9; i++) {
				if (posibles[i]) {
					retorno++;
				}
			}
			return retorno;
		}
	}

	/**
	 * 
	 * @return en caso de que no este resuelto obtiene la menor de las posibles
	 *         soluciones, de lo contrario 0.
	 */
	public int getSinglePosibleSolution() {
		for (int i = 0; i < 9; i++) {
			if (posibles[i]) {
				return i + 1;
			}
		}
		return 0;
	}

	public boolean equals(Celda c) {
		for (int i = 0; i < 9; i++) {
			if (posibles[i] != c.posibles[i]) {
				return false;
			}
			if (c.valor != valor) {
				return false;
			}
		}
		return false;
	}

	/**
	 * Determina la celda que se recibe como parámetro tiene solo dos soluciones
	 * posibles y son las mismas que esta celda.
	 * 
	 * @param celda
	 *            la celda a la que se va a comparar a esta
	 * @return true en caso de que ambas celdas tengan las mismas posibles
	 *         soluciones y que sean solo dos.
	 */
	boolean isTwin(Celda celda) {
		if (celda.getNumberOfPossibleSolutions() == 2
				&& getNumberOfPossibleSolutions() == 2) {
			for (int i = 0; i < 9; i++) {
				if (posibles[i] != celda.posibles[i]) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	boolean isTriplets(Celda c1, Celda c2) {
		if ((c1.getNumberOfPossibleSolutions() == 3
				|| c2.getNumberOfPossibleSolutions() == 3 || getNumberOfPossibleSolutions() == 3)
				&& c1.getNumberOfPossibleSolutions()
						+ c2.getNumberOfPossibleSolutions()
						+ getNumberOfPossibleSolutions() > 6) {
			int w = 0;
			for (int i = 0; i < 9; i++) {
				if (c1.posibles[i] && c2.posibles[i] && posibles[i]) {
					w += 3;
				} else if (c1.posibles[i] && c2.posibles[i]) {
					w += 2;
				} else if (c2.posibles[i] && posibles[i]) {
					w += 2;
				} else if (c1.posibles[i] && posibles[i]) {
					w += 2;
				}
			}
			if (w > 6) {
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}

	}
}
