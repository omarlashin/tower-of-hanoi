package toh;
import javax.swing.*;

public class TOH {
  
  public static void main(String[] args) {
    try{
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      GameFrame frame = new GameFrame();
      frame.setVisible(true);
    }
    catch(ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex){
      JOptionPane.showMessageDialog(null, "Failed to start the game.", "Unknown Error", JOptionPane.ERROR_MESSAGE);
    }
  }
  
}