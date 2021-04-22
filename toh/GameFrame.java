package toh;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameFrame extends JFrame {
  private final GamePanel pnlGame;
  private final JPanel pnlControl;
  private final JLabel lblLevel;
  private final JComboBox<Integer> boxLevel;
  private final JButton btnSolve;
  private final JLabel lblMoves;
  private final Dimension screenSize;
  
  public GameFrame(){
    pnlGame = new GamePanel();
    pnlControl = new JPanel();
    lblLevel = new JLabel("Level:");
    boxLevel = new JComboBox<>(new Integer[]{3, 4, 5, 6, 7, 8});
    btnSolve = new JButton("Solve");
    lblMoves = new JLabel("Minimum number of moves: 7");
    screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    init();
  }
  
  private void init(){
    setTitle("TOH Game");
    setResizable(false);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    pnlGame.setPreferredSize(new Dimension(800, 300));
    add(pnlGame);
    add(pnlControl, BorderLayout.NORTH);
    pnlControl.add(lblLevel);
    pnlControl.add(boxLevel);
    pnlControl.add(btnSolve);
    pnlControl.add(lblMoves);
    pack();
    setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
    
    boxLevel.addActionListener((ActionEvent e) -> {
      pnlGame.stopSolving();
      pnlGame.level = (int)boxLevel.getSelectedItem();
      pnlGame.start();
      lblMoves.setText("Minimum number of moves: " + (int)(Math.pow(2, pnlGame.level) - 1));
    });
    
    btnSolve.addActionListener((ActionEvent e) -> {
      pnlGame.solve();
    });
  }
}