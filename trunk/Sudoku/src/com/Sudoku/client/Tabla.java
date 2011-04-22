package com.Sudoku.client;

import com.Sudoku.shared.Sudoku;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Tabla extends DecoratorPanel implements KeyUpHandler,
		ClickHandler, MouseOverHandler, MouseOutHandler {

	private ClientController controller;
	private ServiceAsync server;
	private HorizontalPanel panel;

	private VerticalPanel sudoku;
	private Grid grid;
	private Celda[][] celdas;

	private VerticalPanel botones;
	private Button fuerzabruta;
	private Button aproximacion;
	private Button mmmm;

	public Tabla(ClientController controller) {
		this.controller = controller;
		server = controller.getServer();
		panel = new HorizontalPanel();
		panel.setSize("924px", "668px");
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.setSpacing(15);

		this.setStyleName("Ventana");
		this.setWidget(panel);
		this.setSize("924px", "668px");

		celdas = new Celda[10][10];
		sudoku = new VerticalPanel();
		sudoku.setSize("534px", "534px");
		sudoku.setStyleName("Sudoku");
		grid = new Grid(10, 10);
		grid.setStyleName("Grid");
		grid.setCellSpacing(8);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				celdas[i][j] = new Celda(i, j);
				celdas[i][j].setSize("50px", "50px");
				if (i == 9) {
					if (j == 9) {
						celdas[i][j].setStyleName("Boton");
						celdas[i][j].setEnabled(false);
					} else {
						celdas[i][j].setStyleName("Boton");
						celdas[i][j].setText(Integer.toString(j + 1));
						celdas[i][j].setEnabled(false);
					}
				} else if (j == 9) {
					if (i == 9) {
						celdas[i][j].setStyleName("Boton");
						celdas[i][j].setEnabled(false);
					} else {
						celdas[i][j].setStyleName("Boton");
						celdas[i][j].setText(Integer.toString(i + 1));
						celdas[i][j].setEnabled(false);
					}
				} else {
					celdas[i][j].setStyleName("Celda");
					celdas[i][j].addKeyUpHandler(this);
					celdas[i][j].addMouseOverHandler(this);
					celdas[i][j].addMouseOutHandler(this);
				}
				grid.setWidget(i, j, celdas[i][j]);
			}
		}
		sudoku.add(grid);
		panel.add(sudoku);

		botones = new VerticalPanel();
		fuerzabruta = new Button("Fuerza Bruta");
		aproximacion = new Button("Aproximación");
		mmmm = new Button("Ahmmmm");

		botones.setSize("200px", "645px");
		botones.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		botones.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		botones.setStyleName("Sudoku");

		fuerzabruta.setSize("160px", "160px");
		fuerzabruta.setStyleName("Boton");
		aproximacion.setSize("160px", "160px");
		aproximacion.setStyleName("Boton");
		mmmm.setSize("160px", "160px");
		mmmm.setStyleName("Boton");

		botones.add(fuerzabruta);
		botones.add(aproximacion);
		botones.add(mmmm);

		fuerzabruta.addClickHandler(this);
		aproximacion.addClickHandler(this);
		mmmm.addClickHandler(this);

		panel.add(botones);

	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		TextBox celda = (TextBox) event.getSource();
		try {
			int i = Integer.parseInt(celda.getText().trim());
			if (i > 0 && i < 10) {
				celda.addStyleName("CeldaLlena");
			} else {
				celda.setText("");
			}
		} catch (NumberFormatException ex) {
			celda.setText("");
		}

		int[][] s = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (celdas[i][j].getText().length() > 0) {
					s[i][j] = Integer.parseInt(celdas[i][j].getText());
				} else {
					s[i][j] = 0;
				}

			}
		}
		if (celda.getText().length() == 0) {
			celda.removeStyleName("CeldaLlena");
			return;
		}
		server.isSudoku(s, new AsyncCallback<com.Sudoku.shared.Sudoku>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(com.Sudoku.shared.Sudoku result) {
				if (result == null) {
					sudoku.addStyleName("Error");
					setEnableButtons(false);
				} else {
					sudoku.removeStyleName("Error");
					setEnableButtons(true);
				}

			}
		});

	}

	@Override
	public void onClick(ClickEvent event) {
		int[][] data = new int[9][9];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				data[i][j] = Integer.parseInt(celdas[i][j].getText());
			}
		}
		if (event.getSource().equals(fuerzabruta)) {
			server.fuerzaBruta(data,
					new AsyncCallback<com.Sudoku.shared.Sudoku>() {

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
						}

						@Override
						public void onSuccess(Sudoku result) {
							setResultado(result);
						}
					});
		} else if (event.getSource().equals(mmmm)) {
			server.mmmm(data, new AsyncCallback<com.Sudoku.shared.Sudoku>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}

				@Override
				public void onSuccess(Sudoku result) {
					setResultado(result);
				}
			});
		} else if (event.getSource().equals(aproximacion)) {
			server.aproximacion(data,
					new AsyncCallback<com.Sudoku.shared.Sudoku>() {

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
						}

						@Override
						public void onSuccess(Sudoku result) {
							setResultado(result);
						}
					});

		}

	}

	private com.Sudoku.shared.Sudoku makeSudoku() {
		int[][] data = new int[9][9];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				data[i][j] = Integer.parseInt(celdas[i][j].getText());
			}
		}
		return new com.Sudoku.shared.Sudoku(data);
	}

	private void setEnableButtons(boolean b) {
		mmmm.setEnabled(b);
		fuerzabruta.setEnabled(b);
		aproximacion.setEnabled(b);
	}

	private class Celda extends TextBox {
		int x, y;

		public Celda(int x, int y) {
			this.x = x;
			this.y = y;
		}

	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		Celda celda = (Celda) event.getSource();
		for (int i = 0; i < 9; i++) {
			celdas[celda.x][i].addStyleName("CeldaMuestra");
		}
		for (int i = 0; i < 9; i++) {
			celdas[i][celda.y].addStyleName("CeldaMuestra");
		}

		int k = 0, l = 0, m = 0, n = 0;
		if (celda.x < 3) {
			k = 0;
			l = 3;
		} else if (celda.x < 6) {
			k = 3;
			l = 6;
		} else if (celda.x < 9) {
			k = 6;
			l = 9;
		}

		if (celda.y < 3) {
			m = 0;
			n = 3;
		} else if (celda.y < 6) {
			m = 3;
			n = 6;
		} else if (celda.y < 9) {
			m = 6;
			n = 9;
		}

		for (int i = k; i < l; i++) {
			for (int j = m; j < n; j++) {
				celdas[i][j].addStyleName("CeldaMuestra");
			}
		}

	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		Celda celda = (Celda) event.getSource();
		for (int i = 0; i < 9; i++) {
			celdas[celda.x][i].removeStyleName("CeldaMuestra");
		}
		for (int i = 0; i < 9; i++) {
			celdas[i][celda.y].removeStyleName("CeldaMuestra");
		}

		int k = 0, l = 0, m = 0, n = 0;
		if (celda.x < 3) {
			k = 0;
			l = 3;
		} else if (celda.x < 6) {
			k = 3;
			l = 6;
		} else if (celda.x < 9) {
			k = 6;
			l = 9;
		}

		if (celda.y < 3) {
			m = 0;
			n = 3;
		} else if (celda.y < 6) {
			m = 3;
			n = 6;
		} else if (celda.y < 9) {
			m = 6;
			n = 9;
		}

		for (int i = k; i < l; i++) {
			for (int j = m; j < n; j++) {
				celdas[i][j].removeStyleName("CeldaMuestra");
			}
		}
	}

	private void setResultado(Sudoku result) {
		// TODO Auto-generated method stub

	}

}
