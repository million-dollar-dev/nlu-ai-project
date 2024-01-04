package view;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import model.Puzzle;
import model.PuzzleGenerator;
public abstract class Grid {
	protected JPanel innerPanel;
	protected JPanel padderPanel;
	protected GridBagLayout padderLayout;
	protected GridBagConstraints innerConstraints;

	protected Color gridColour = Color.BLACK;

	protected JTextPane[] colLabelField;
	protected JTextField[] rowLabelField;
	protected Tile[][] tiles;

	protected String[] colLabels;
	protected String[] rowLabels;
	protected int[][] tileStates;

	protected int rowNum;
	protected int colNum;
	protected int maxRowLabelSize;
	protected int maxColLabelSize;
	protected int tileCount = 0;

	protected int tileSize = 25;
	
	protected boolean isLabelsEditable = false;

	public Grid(JPanel padderPanel) {
		this.padderPanel = padderPanel;
	}

	
	public void setGridColour(Color color) { //Change the grid colour
		gridColour = color;
	}

	public void clearPanel() { //Clear the panel 
		padderPanel.remove(innerPanel);
		padderPanel.revalidate();
		padderPanel.repaint();
	}
	
	public void repaintGrid(int[][] state) {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				if (state[i][j] == 1)
					tiles[i][j].setState(1);
				if (state[i][j] == 0)
					tiles[i][j].setState(0);
				tileStates[i][j] = state[i][j];
			}
		}
		
	}
	
	public void loadPuzzle(Puzzle puzzle) { 
		rowNum = puzzle.getNumRows();
		colNum = puzzle.getNumCols();
		colLabels = puzzle.getColLabels();
		rowLabels = puzzle.getRowLabels();
		maxRowLabelSize = puzzle.getMaxRowLabelSize();
		maxColLabelSize = puzzle.getMaxColLabelSize();
		tileCount = puzzle.getTileCount();
		tileStates = PuzzleGenerator.copyArray(puzzle.getTiles());
	}
	

	public void drawGrid() { //Draw a grid based on the current attributes 
		
		//Establish the size of the grid
		int width = colNum + 1;
		int height = rowNum + 1;

		//Create new (2D) arrays for the tiles and label fields
		tiles = new Tile[rowNum][colNum];
		colLabelField = new JTextPane[colNum];
		rowLabelField = new JTextField[rowNum];

		//Format the padder frame with the respective size
		padderLayout = new GridBagLayout();
		padderLayout.columnWidths = new int[] { 0, width * tileSize, 0 };
		padderLayout.rowHeights = new int[] { 0, height * tileSize, 0 };
		padderLayout.columnWeights = new double[] { 1, 0, 1 };
		padderLayout.rowWeights = new double[] { 1, 0, 1 };
		padderPanel.setBackground(Color.WHITE);
		padderPanel.setLayout(padderLayout);

		innerPanel = new JPanel();
		
		GridBagLayout gblInner = new GridBagLayout();

		//Create the column widths array
		int[] gblColWidths = new int[width];
		gblColWidths[0] = maxRowLabelSize * 15;
		for (int i = 1; i < gblColWidths.length; i++) {
			gblColWidths[i] = tileSize;
		}
		gblInner.columnWidths = gblColWidths;

		//Create the row widths array
		int[] gblRowHeights = new int[height];
		gblRowHeights[0] = maxColLabelSize * 20;
		for (int i = 1; i < gblRowHeights.length; i++) {
			gblRowHeights[i] = tileSize;
		}
		gblInner.rowHeights = gblRowHeights;

		//Apply the widths
		gblInner.columnWeights = new double[colNum];
		gblInner.rowWeights = new double[rowNum];

		innerPanel.setLayout(gblInner);

		//Loop through the grid
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {

				GridBagConstraints gbc = new GridBagConstraints();
				gbc.insets = new Insets(0, 0, 0, 0);
				gbc.gridx = c;
				gbc.gridy = r;
				gbc.fill = GridBagConstraints.BOTH;

				if (r == 0 && c == 0) { //Draw an empty rectangle in the top left corner
					JTextArea t = new JTextArea();
					t.setEditable(false);
					drawComponentBorder(t, r, c, height, width, gridColour);
					innerPanel.add(t, gbc);

				} else if (r == 0) { //Draw the column label fields at the top of the grid

					JTextPane t = new JTextPane();
					String label = colLabels[c - 1];
					
					if(!isLabelsEditable) { //If the label is not going to be changed, pad the label so it is bottom-aligned
						while (Puzzle.findVertLength(label) < maxColLabelSize) {
							label = "\n" + label;
						}
					}

					t.setText(label); //Set the label field to the current label stored
					t.setEditable(isLabelsEditable);
					t.setFont(new Font("Monospaced", Font.PLAIN, tileSize / 2));
					t.setForeground(gridColour);

					t.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent e) { //Only allow the appropriate characters
							String allowedChars = "0123456789\n";
							if (!allowedChars.contains("" + e.getKeyChar())) {
								e.consume();

							}
						}
					});

					//Align the text in the center
					StyledDocument doc = t.getStyledDocument();
					SimpleAttributeSet center = new SimpleAttributeSet();
					StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
					doc.setParagraphAttributes(0, doc.getLength(), center, false);

					//Draw the border based on where it is in the grid
					drawComponentBorder(t, r, c, height, width, gridColour);

					//Store the field in the array and add it to the panel
					colLabelField[c - 1] = t;
					innerPanel.add(t, gbc);

				} else if (c == 0) { //Draw the row label fields down the left side of the grid

					JTextField t = new JTextField();
					t.setBackground(Color.WHITE);
					t.setText(rowLabels[r - 1]); 
					t.setEditable(isLabelsEditable);
					t.setFont(new Font("Monospaced", Font.PLAIN, tileSize / 2));
					t.setHorizontalAlignment(SwingConstants.RIGHT);
					t.setForeground(gridColour);

					t.addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent e) { //Only allow appropriate characters
							String allowedChars = "0123456789 ";
							if (!allowedChars.contains("" + e.getKeyChar())) {
								e.consume();
							}

						}
					});
					
					//Draw the border based on the label's position in the grid
					drawComponentBorder(t, r, c, height, width, gridColour);

					//Store the label field in the array and add it to the panel
					rowLabelField[r - 1] = t;
					innerPanel.add(t, gbc);

				} else { //Draw a tile in all other places

					
					//Create a new tile with the proper size, initial state, and give it its position in the grid
					Tile tile = new Tile(tileSize, tileStates[r - 1][c - 1], gridColour, r - 1, c - 1);

					//Draw the border depending on where the tile is in the grid
					drawComponentBorder(tile, r, c, height, width, gridColour);

					tile.addMouseListener(new MouseAdapter() {
						boolean isPressed;

						public void mousePressed(MouseEvent e) {
							isPressed = true;
						}

						public void mouseReleased(MouseEvent e) {
							if (isPressed) {
								if (SwingUtilities.isRightMouseButton(e)) {
									// right button is pressed
									
									rightMouseButtonPressEvent(tile);

								} else {
									// left button pressed

									leftMouseButtonPressEvent(tile);
								}
							}

							mouseButtonPressEvent();

							isPressed = false;
						}

						public void mouseExited(MouseEvent e) {
							isPressed = false;
						}

						public void mouseEntered(MouseEvent e) {
							isPressed = true;
						}

					});

					//Store the tile in the 2D array and add it to the panel
					tiles[r - 1][c - 1] = tile;
					innerPanel.add(tile, gbc);

				}

			}
		}

		//make the constraints of the inner grid panel
		innerConstraints = new GridBagConstraints();
		innerConstraints.insets = new Insets(0, 0, 5, 5);
		innerConstraints.gridx = 1;
		innerConstraints.gridy = 1;
		
		
		//Add the inner panel to the padder panel
		padderPanel.add(innerPanel, innerConstraints);

	}

	public void setGridSize(int rows, int cols) {
		rowNum = rows;
		colNum = cols;
		
		//Create and fill the column and row labels with empty strings
		colLabels = new String[cols];
		for(int i = 0; i < colLabels.length; i++) {
			colLabels[i] = "";
		}
		rowLabels = new String[rows];
		tileStates = new int[rows][cols];
		
	}

	//ACCESSOR METHODS
	public int[][] getTileStates() { 
		return tileStates;
	}

	public String[] getColLabels() {
		return colLabels;
	}

	public String[] getRowLabels() {
		return rowLabels;
	}

	public int getNumRows() {
		return rowNum;
	}

	public int getNumCols() {
		return colNum;
	}

	public int getTileCount() {
		return tileCount;
	}
	
	public JTextPane[] getColLabelField() {
		return colLabelField;
	}
	
	public JTextField[] getRowLabelField() {
		return rowLabelField;
	}


	// ABSTRACT METHODS TO BE DEFINED BY SUBCLASSES
	public abstract void rightMouseButtonPressEvent(Tile tile);

	public abstract void leftMouseButtonPressEvent(Tile tile);

	public abstract void mouseButtonPressEvent();

	public void clearGrid() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j].setState(0);
				tileStates[i][j] = 0;
			}
		}
	}


	public void drawComponentBorder(JComponent comp, int r, int c, int height, int width, Color color) { //Draw the borders depending on the position in the grid
		if (r == 0 && c == 0) {
			comp.setBorder(new MatteBorder(0, 0, 4, 4, gridColour));
		} else if (r == 0) {
			if (c == width - 1) {
				comp.setBorder(new MatteBorder(4, 0, 4, 4, gridColour));

			} else {
				comp.setBorder(new MatteBorder(4, 0, 4, 2, gridColour));
			}

		} else if (c == 0) {
			if (r == height - 1) {
				comp.setBorder(new MatteBorder(0, 4, 4, 4, gridColour));
			} else {
				comp.setBorder(new MatteBorder(0, 4, 2, 4, gridColour));
			}
		} else {
			if (r == height - 1 && c == width - 1) {
				comp.setBorder(new MatteBorder(0, 0, 4, 4, gridColour));
			} else if (c == width - 1) {
				comp.setBorder(new MatteBorder(0, 0, 2, 4, gridColour));
			} else if (r == height - 1) {
				comp.setBorder(new MatteBorder(0, 0, 4, 2, gridColour));
			} else {
				comp.setBorder(new MatteBorder(0, 0, 2, 2, gridColour));
			}

		}

	}


	public void setTileStates(int[][] tileStates) {
		this.tileStates = tileStates;
	}
	
}
