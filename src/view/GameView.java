package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import model.GA;
import model.Puzzle;
import model.PuzzleGenerator;
import model.PuzzleTemplate;
import model.SinglePointCrossover;
import model.TwoPointCrossover;
import model.UniformCrossover;

public class GameView extends JFrame{
	public static JPanel chartPanel;
	public static JFreeChart chart;
	public static DefaultCategoryDataset dataset;
	
	public static Puzzle puzzle;
	
	public JTextField populationSizeField, mutationRateField, generationsField;
	
	private String[] puzzleTypes = {"Santa", "Tea", "Random"};
	private String[] crossoverTypes = {"One-point", "Two-point", "Uniform crossover"};
	public JComboBox<String> puzzleComboBox = new JComboBox<>(puzzleTypes);
	public JComboBox<String> crossoverComboBox = new JComboBox<>(crossoverTypes);
	
	public static GameGrid gameGrid;
	
	private Font font = new Font("Segoe UI Light", Font.PLAIN, 17);
	private JLabel label = new JLabel();
	public static JLabel lbStatus, lbGene;
	
	private Random rd = new Random();
	
	public GameView() {	
		setTitle("NONOGRAM SOLVER");
				
		setLayout(new BorderLayout());
		JPanel leftPanel = createLeftPanel();
		add(leftPanel, BorderLayout.CENTER);
		JPanel rightPanel = createRightPanel();
		add(rightPanel, BorderLayout.EAST);
	
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private JPanel createLeftPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		JProgressBar pgbProgress = new JProgressBar();
		pgbProgress.setBackground(Color.WHITE);
		pgbProgress.setForeground(Color.BLACK);
		pgbProgress.setString("0");
		JPanel pnlGrid = new JPanel();
		panel.setLayout(new GridLayout(1, 1));
		
		gameGrid = new GameGrid(pnlGrid, pgbProgress);	
		puzzle = PuzzleGenerator.genPuzzle(PuzzleTemplate.SANTA);
		gameGrid.loadPuzzle(puzzle);
		gameGrid.drawGrid();
		gameGrid.clearGrid();
		panel.add(pnlGrid);				
		return panel;
	}
	
	private JPanel createRightPanel() {
		JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(5, 1));
        JPanel temp = new JPanel();
        TitledBorder titleBorder1 = new TitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), "Configuration");
        titleBorder1.setTitleFont(font);
        temp.setBorder(titleBorder1);
        temp.setLayout(new GridLayout(2, 1));
        
        label = new JLabel("Puzzle type:");
        label.setFont(font);
        formPanel.add(label);
        puzzleComboBox.setFont(font);
        formPanel.add(puzzleComboBox);
        
        label = new JLabel("Population size:");
        label.setFont(font);
        formPanel.add(label);
        populationSizeField = new JTextField();
        populationSizeField.setText(GA.population_size + "");
        customTextfield(populationSizeField);
        formPanel.add(populationSizeField);
        
        label = new JLabel("Crossover type:");
        label.setFont(font);
        formPanel.add(label);
        crossoverComboBox.setFont(font);
        formPanel.add(crossoverComboBox);
        
        label = new JLabel("Mutation chance:");
        label.setFont(font);
        formPanel.add(label);
        mutationRateField = new JTextField();
        customTextfield(mutationRateField);
        mutationRateField.setText(GA.mutation_rate + "");
        formPanel.add(mutationRateField);
        
        label = new JLabel("Generation number:");
        label.setFont(font);
        formPanel.add(label);
        generationsField = new JTextField();
        customTextfield(generationsField);
        generationsField.setText(GA.generation_number + "");
        formPanel.add(generationsField);
        
        JPanel btnPanel = new JPanel();
        JButton newButton = new JButton("New");
        JButton solveButton = new JButton("Solve");
        customButton(newButton);
        customButton(solveButton);
        btnPanel.add(newButton);
        btnPanel.add(solveButton);
        temp.add(formPanel);
        temp.add(btnPanel);
        rightPanel.add(temp, BorderLayout.NORTH);
        JPanel chartPanel = createChartPanel();
        rightPanel.add(chartPanel, BorderLayout.CENTER);
        
        solveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// load ppSize
				Integer populationSize = Integer.parseInt(populationSizeField.getText());
				GA.setPopulation_size(populationSize);

				// load crossover
				String crossType = (String) crossoverComboBox.getSelectedItem();
				if (crossType != null) {
					switch (crossType.toUpperCase()) {
					case "ONE-POINT":
						GA.setReproduceStrategy(new SinglePointCrossover());
						break;
					case "TWO-POINT":
						GA.setReproduceStrategy(new TwoPointCrossover());
						break;
					case "UNIFORM CROSSOVER":
						GA.setReproduceStrategy(new UniformCrossover());
						break;
					}
				}
				// mutation chance
				double mutation = Double.parseDouble(mutationRateField.getText());
				GA.setMutation_rate(mutation);

				// gene
				int generations = Integer.parseInt(generationsField.getText());
				GA.setGeneration_number(generations);
				dataset.clear();
				lbGene.setText("");
				lbStatus.setText("");
				new Thread(() -> {
		            puzzle.solveWithGA();
		        }).start();
			}
		});
        
        newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// load puzzle type
				String puzzleType = (String) puzzleComboBox.getSelectedItem();
				if (puzzleType != null) {
					switch (puzzleType.toUpperCase()) {
					case "RANDOM":
						puzzle = PuzzleGenerator.genPuzzle(25, 25, rd.nextInt(250, 270));
						gameGrid.clearGrid();
						break;
					case "TEA":
						puzzle = PuzzleGenerator.genPuzzle(PuzzleTemplate.TEA);
						gameGrid.loadPuzzle(puzzle);
						gameGrid.clearGrid();
						break;
					case "SANTA":
						puzzle = PuzzleGenerator.genPuzzle(PuzzleTemplate.SANTA);
						gameGrid.loadPuzzle(puzzle);
						gameGrid.clearGrid();
						break;
					}
				}
				lbGene.setText("");
				lbStatus.setText("");
				dataset.clear();
				setPuzzle(puzzle);
				gameGrid.clearPanel();
				gameGrid.loadPuzzle(puzzle);
				gameGrid.drawGrid();
				gameGrid.clearGrid();
			}
		});     
        return rightPanel;
	}
	
	private JPanel createChartPanel() {
		chartPanel = new JPanel();
		chartPanel.setLayout(new BorderLayout());
		JPanel statusPanel = new JPanel();
		label = new JLabel("Status: ");
		label.setFont(font);
		statusPanel.add(label);
		lbStatus = new JLabel();
		lbStatus.setFont(font);
		statusPanel.add(lbStatus);
		JPanel genePanel = new JPanel();
		label = new JLabel("Generation: ");
		label.setFont(font);
		genePanel.add(label);
		lbGene = new JLabel();
		lbGene.setFont(font);
		genePanel.add(lbGene);
		TitledBorder border = new TitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), "Information");
		border.setTitleFont(font);
		chartPanel.setBorder(border);
		dataset = new DefaultCategoryDataset();
        CategoryDataset data = dataset;
        
        chart = ChartFactory.createLineChart(
        		"",
                "Number of generations",              
                "Fitness function value evaluation",              
                data,              
                PlotOrientation.VERTICAL,
                true,                  
                true,
                false
        );
        JPanel temp = new JPanel();
        temp.setLayout(new GridLayout(2, 1));
        temp.add(statusPanel);
        temp.add(genePanel);
        ChartPanel pannel = new ChartPanel(chart);
        pannel.setPreferredSize(new Dimension(560, 370));
        chartPanel.add(temp, BorderLayout.NORTH);
        chartPanel.add(pannel, BorderLayout.CENTER);
        return chartPanel;        
	}
	
	public static void addChartData(int fitness, String title, int gene) {
        dataset.addValue(fitness, title, "" + gene);
    }
	
	private void customButton(JButton button) {
		button.setFocusable(false);
        button.setFont(font);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBackground(Color.WHITE);
	}
	
	private void customTextfield(JTextField textfield) {
		textfield.setFont(font);
        textfield.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        textfield.setBackground(Color.WHITE);
        textfield.setPreferredSize(new Dimension(200, 30));
	}
		
	public static void setPuzzle(Puzzle puzzle) {
		GameView.puzzle = puzzle;
	}	

	public static void setDataset(DefaultCategoryDataset dataset) {
		GameView.dataset = dataset;
	}
}
