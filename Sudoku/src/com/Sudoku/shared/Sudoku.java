package com.Sudoku.shared;

import java.io.Serializable;
import java.util.Stack;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Sudoku implements Serializable {

	private static final long serialVersionUID = 8375667867474065406L;

	/**
	 * Arreglo bidimencional de enteros que representa el estado actual del
	 * Sudoku de forma simplificada para hacerlo persistente.
	 */
	@Persistent
	private int sudoku[][];

	/**
	 * Arreglo bidimencinal de celdas. Sobre estas se hacen los calculos.
	 */
	private Stack<Stack<Celda>> celdas;

	/**
	 * Arreglo de Sudokus que representan las posibles soluciones a el mismo.
	 * este es usado en el m�todo solve()
	 */
	private Stack<Sudoku> sudokus;

	/**
	 * Contructor por defecto, crea un sudoku vacio.
	 */
	public Sudoku() {
		celdas = new Stack<Stack<Celda>>();
		sudoku = new int[9][9];
		sudokus = new Stack<Sudoku>();

		for (int i = 0; i < 9; i++) {
			celdas.push(new Stack<Celda>());
			for (int j = 0; j < 9; j++) {
				sudoku[i][j] = 0;
				celdas.peek().push(new Celda());
			}
		}
	}

	/**
	 * Contructor
	 * 
	 * @param s
	 *            arreglo bidimencional de 9 * 9 enteros, con valores de 1 al 9
	 *            para las casillas resuletas y 0 para las casillas por
	 *            resolver.
	 */
	public Sudoku(int[][] s) {
		celdas = new Stack<Stack<Celda>>();
		sudoku = new int[9][9];
		sudokus = new Stack<Sudoku>();

		for (int i = 0; i < 9; i++) {
			celdas.push(new Stack<Celda>());
			for (int j = 0; j < 9; j++) {
				celdas.peek().push(new Celda());
			}
		}
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s[i].length; j++) {
				if (s[i][j] != 0) {
					setCelda(i, j, s[i][j]);
				}
			}
		}
	}

	/**
	 * Fija un valor en la celda i,j. Esto significa que la celda ha sido
	 * resuelta y ese nuevo valor puede ser utilizado para recalcular el resto
	 * del Sudoku.
	 * 
	 * @param i
	 *            indice de fila
	 * @param j
	 *            indice de columna
	 * @param valor
	 *            valor entero del 1 al 9 que se va a fijar en la celda
	 *            descrita.
	 */
	public void setCelda(int i, int j, int valor) {
		if (i > -1 && i < 10 && j > -1 && j < 10 && valor > 0 && valor < 11) {
			sudoku[i][j] = valor;
			celdas.get(i).get(j).setValor(valor);
		}
	}

	/**
	 * M�todo que revisa todas las celdas no resuletas del Sudoku para ver que
	 * valor les es posible tomar. Analiza la celda por columna, fila y cuadro.
	 */
	private void setPosibles() {
		// Checar por filas
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (celdas.get(i).get(j).isSet()) {
					int valor = celdas.get(i).get(j).getValor();
					for (int k = 0; k < 9; k++) {
						celdas.get(i).get(k).setNotPosible(valor);
					}
				}
			}
		}

		// Checa por columnas
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (celdas.get(j).get(i).isSet()) {
					int valor = celdas.get(j).get(i).getValor();
					for (int k = 0; k < 9; k++) {
						celdas.get(k).get(i).setNotPosible(valor);

					}
				}
			}
		}

		// Checa por cuadros
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (celdas.get(i).get(j).isSet()) {
					int k = 0, l = 0, m = 0, n = 0;
					if (i < 3) {
						k = 0;
						l = 3;
					} else if (i < 6) {
						k = 3;
						l = 6;
					} else if (i < 9) {
						k = 6;
						l = 9;
					}

					if (j < 3) {
						m = 0;
						n = 3;
					} else if (j < 6) {
						m = 3;
						n = 6;
					} else if (j < 9) {
						m = 6;
						n = 9;
					}
					int valor = celdas.get(i).get(j).getValor();
					int a = k, b = m;
					for (k = a; k < l; k++) {
						for (m = b; m < n; m++) {
							celdas.get(k).get(m).setNotPosible(valor);
						}
					}

				}
			}
		}

	}

	/**
	 * Regresa los valores de las celdas para su f�cil visualizaci�n.
	 */
	@Override
	public String toString() {
		String retorno = "";
		for (int i = 0; i < 9; i++) {
			if (i % 3 == 0) {
				retorno += "-------------------------------\n";
			}
			for (int j = 0; j < 9; j++) {
				if (j % 3 == 0) {
					retorno += "|";
				}
				retorno += " ";

				retorno += sudoku[i][j];
				retorno += " ";
			}
			retorno += "|\n";

		}
		retorno += "-------------------------------\n";
		return retorno;
	}

	/**
	 * 
	 * @return un String con los valores que pueden tomar las celdas que no han
	 *         sido resueltas.
	 */
	public String toStringPosibles() {
		String retorno = "";
		for (int i = 0; i < 9; i++) {
			if (i % 3 == 0) {
				retorno += "----------------------------------------------------------------------------------------------\n";
			}
			for (int j = 0; j < 9; j++) {
				if (j % 3 == 0) {
					retorno += "|";
				}
				if (!celdas.get(i).get(j).isSet()) {
					Celda celda = celdas.get(i).get(j);
					boolean[] posibles = celda.getPosibles();
					for (int k = 0; k < posibles.length; k++) {
						if (posibles[k]) {
							retorno += k + 1;
						} else {
							retorno += "-";
						}
					}
				} else {
					retorno += "---------";
				}
				retorno += " ";
			}
			retorno += "|\n";
		}
		retorno += "----------------------------------------------------------------------------------------------\n";
		return retorno;
	}

	/**
	 * Revisa si el Sudoku ha sido resuelto.
	 * 
	 * @return en caso de que el sudoku se halla resuleto satisfactoriamente
	 *         true, de lo contrario false.
	 */
	public boolean isSolved() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (!celdas.get(i).get(j).isSet()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Resuelve el sudoku por un metodo "humano"
	 * 
	 * @param max
	 *            el n�mero m�ximo de iteraciones
	 * @return en caso de que el sudoku halla sido satisfecho true, false de los
	 *         contrario.
	 */
	public boolean solve(int max) {
		int mmm = 0;
		Sudoku s;
		while (!isSolved()) {
			if (mmm >= max) {
				return false;
			}

			s = clone();

			setPosibles();

			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					Celda celda = celdas.get(i).get(j);
					if (!celda.isSet()) {
						if (celda.getNumberOfPossibleSolutions() == 1) {
							celda.setValor(celda.getSinglePosibleSolution());
							setPosibles();
							sudoku[i][j] = celda.getValor();
						}
					}
				}
			}
			boolean condition = false;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					Celda celda = celdas.get(i).get(j);
					if (!celda.isSet()) {
						if (celda.getNumberOfPossibleSolutions() == 1) {
							condition = true;
						}
					}
				}
			}
			if (!condition) {

				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						analizeCelda(i, j);
					}
				}

			}
			mmm++;
			if (s.equals(this)) {
				if (isSolvable()) {
					int min = Integer.MAX_VALUE, w = 0, v = 0;
					Celda celda;
					for (int i = 0; i < 9; i++) {
						for (int j = 0; j < 9; j++) {
							celda = celdas.get(i).get(j);
							if (!celda.isSet()) {
								if (celda.getNumberOfPossibleSolutions() < min) {
									min = celda.getNumberOfPossibleSolutions();
									w = i;
									v = j;
								}
							}
						}
					}
					celda = celdas.get(w).get(v);
					for (int i = 0; i < 9; i++) {
						if (celda.getPosibles()[i]) {
							sudokus.push(clone());
							sudokus.peek().setCelda(w, v, i + 1);
						}
					}
					while (!sudokus.isEmpty()) {
						sudokus.peek().solve(max - mmm);
						if (sudokus.peek().isSolved()) {
							copyFrom(sudokus.pop());
							return true;
						} else {
							sudokus.pop();
						}
					}

				} else {
					return false;
				}

			}
		}
		return true;
	}

	/**
	 * Copia los velores de las celdas del sudoku que recibe como parametro a
	 * este.
	 * 
	 * @param s
	 *            El Sudoku del cual se copiaran los valores de las celdas.
	 */
	protected void copyFrom(Sudoku s) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Celda c = s.celdas.get(i).get(j);
				if (c.isSet()) {
					setCelda(i, j, c.getValor());
				}
			}
		}
		setPosibles();
	}

	/**
	 * @return regresa un nuevo objeto de la clase Sudoku con las mismas celdas
	 *         que este.
	 */
	protected Sudoku clone() {
		Sudoku retorno = new Sudoku();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Celda celda = celdas.get(i).get(j);
				if (celda.isSet()) {
					retorno.setCelda(i, j, celda.getValor());
				}
			}
		}
		return retorno;
	}

	/**
	 * 
	 * @param s
	 *            El Sudoku a comparar
	 * @return true en caso de que las celdas de ambos Sudokus sean iguales, de
	 *         lo contrario false.
	 */
	public boolean equals(Sudoku s) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {

				if (celdas.get(i).get(j).getValor() != s.celdas.get(i).get(j)
						.getValor()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Analiza la celda con los indices indicados como parametros. Revisa la
	 * fila, columna y cuadro y determina si es la �nica celda que puede tomar
	 * algun valor. En caso de que ninguna otra celda pueda tomar dicho valor
	 * entonses se fija como resultado.
	 * 
	 * @param a
	 *            indice de columna
	 * @param b
	 *            indice de fila
	 */
	private void analizeCelda(int a, int b) {
		Celda celda = celdas.get(a).get(b);
		if (!celda.isSet()) {
			int[] pos = new int[9];
			for (int i = 0; i < 9; i++) {
				if (i != b) {
					Celda c = celdas.get(a).get(i);
					if (!c.isSet()) {
						for (int k = 0; k < 9; k++) {
							if (c.getPosibles()[k]) {
								pos[k]++;
							}
						}
					}
				}
			}

			for (int i = 0; i < 9; i++) {
				if (i != a) {
					Celda c = celdas.get(i).get(b);
					if (!c.isSet()) {
						for (int k = 0; k < 9; k++) {
							if (c.getPosibles()[k]) {
								pos[k]++;
							}
						}
					}
				}
			}

			int k = 0, l = 0, m = 0, n = 0;
			if (a < 3) {
				k = 0;
				l = 3;
			} else if (a < 6) {
				k = 3;
				l = 6;
			} else if (a < 9) {
				k = 6;
				l = 9;
			}

			if (b < 3) {
				m = 0;
				n = 3;
			} else if (b < 6) {
				m = 3;
				n = 6;
			} else if (b < 9) {
				m = 6;
				n = 9;
			}

			for (int i = k; i < l; i++) {
				for (int j = m; j < n; j++) {
					if (i != a && j != b) {
						Celda c = celdas.get(i).get(j);
						if (!c.isSet()) {
							for (int q = 0; q < 9; q++) {
								if (c.getPosibles()[q]) {
									pos[q]++;
								}
							}
						}
					}
				}
			}
			for (int q = 0; q < 9; q++) {
				if (pos[q] == 0 && celda.getPosibles()[q]) {
					celda.setValor(q + 1);
					sudoku[a][b] = q + 1;
					setPosibles();
					return;
				}
			}

		}
	}

	/**
	 * Revisa si el Sudoku tiene una soluci�n viable con los valores de cedas
	 * actuales.
	 * 
	 * @return true en caso de que el sudoku pueda ser a�n resuleto, false de lo contrario. 
	 */
	public boolean isSolvable() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (!celdas.get(i).get(j).isSet()) {
					if (celdas.get(i).get(j).getNumberOfPossibleSolutions() == 0) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
