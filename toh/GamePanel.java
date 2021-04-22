package toh;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GamePanel extends JPanel implements Runnable {
  public int level;
  private final Image base;
  private final Pole poles[];
  private Thread solver;
  private boolean locked;
  private Pole source;
  private Disk target;
  private boolean solving;

  public GamePanel() {
    setBackground(Color.WHITE);
    base = new ImageIcon(getClass().getResource("resources/base.png")).getImage();
    poles = new Pole[3];
    for (int i = 0; i < poles.length; i++)
      poles[i] = new Pole((i + 1) * 189);
    level = 3;
    locked = false;
    source = null;
    solving = false;
    start();
    addMouseListener(new MouseAdapter(){
      @Override
      public void mousePressed(MouseEvent e){
        if(solving){
          stopSolving();
          start();
        }
        for(Pole pole : poles)
          if(pole.isWithin(e.getPoint()) && (!pole.isEmpty())){
            locked = true;
            source = pole;
            target = source.popDisk();
            break;
          }
      }
      
      @Override
      public void mouseReleased(MouseEvent e){
        if(locked){
          boolean isOutlier = true;
          for(Pole pole : poles)
            if(pole.isWithin(e.getPoint())){
              isOutlier = false;
              if(pole.isCompatible(target.size)){
                pole.pushDisk(target);
                if(poles[2].isDone(level)){
                  repaint();
                  JOptionPane.showMessageDialog(null, "Well Done!", "You Won", JOptionPane.INFORMATION_MESSAGE);
                  start();
                }
              }
              else
                source.pushDisk(target);
              break;
            }
          if(isOutlier)
            source.pushDisk(target);
          locked = false;
          source = null;
          target = null;
          repaint();
        }
      }
    });
    addMouseMotionListener(new MouseMotionAdapter(){
      @Override
      public void mouseDragged(MouseEvent e){
        if(locked){
          target.x = e.getX();
          target.y = e.getY();
          repaint();
        }
      }
    });
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(base, 0, 280, null);
    for (int i = 0; i < poles.length; i++)
      poles[i].paint(g);
    if(target != null)
      target.paint(g);
  }

  public void start(){
    for(int i = 0; i < poles.length; i++)
      poles[i].empty();
    target = null;
    poles[0].fill(level);
    repaint();
  }
  
  private void move(int n, int source, int destination, int auxiliary) throws InterruptedException{
    if(n == 1){
      target = poles[source].popDisk();
      target.y = 0;
      repaint();
      Thread.sleep(300);
      target.x = poles[destination].x + 7;
      repaint();
      Thread.sleep(300);
      poles[destination].pushDisk(target);
      target = null;
      repaint();
      Thread.sleep(300);
    }
    else{
      move(n - 1, source, auxiliary, destination);

      target = poles[source].popDisk();
      target.y = 0;
      repaint();
      Thread.sleep(300);
      target.x = poles[destination].x + 7;
      repaint();
      Thread.sleep(300);
      poles[destination].pushDisk(target);
      target = null;
      repaint();
      Thread.sleep(300);

      move(n - 1, auxiliary, destination, source);
    }
  }
  
  @Override
  public void run(){
    try{
      move(level, 0, 2, 1);
    }
    catch(InterruptedException ex){}
  }
  
  public void solve(){
    stopSolving();
    start();
    solving = true;
    solver = new Thread(this);
    solver.start();
  }
  
  public void stopSolving(){
    if(solving){
      solver.stop();
      solving = false;
    }
  }
}

class Pole {
  public final int x;
  private final Image pole;
  private final ArrayList<Disk> disks;

  public Pole(int x) {
    pole = new ImageIcon(getClass().getResource("resources/pole.png")).getImage();
    this.x = x;
    disks = new ArrayList<>(8);
  }

  public void paint(Graphics g) {
    g.drawImage(pole, x, 0, null);
    for (int i = 0; i < disks.size(); i++){
      disks.get(i).x = x + 7;
      disks.get(i).y = 280 - (disks.size() - i) * 30;
      disks.get(i).paint(g);
    }
  }

  public void fill(int level) {
    for (int i = 0; i < level; i++)
      disks.add(new Disk(i + 1));
  }
  
  public boolean isWithin(Point p){
    boolean a = (p.x >= (x - 68)) && (p.x <= (x + 82));
    boolean b = (p.y >= 0) && (p.y <= 280);
    return a && b;
  }
  
  public boolean isEmpty(){
    return disks.isEmpty();
  }
  
  public boolean isCompatible(int size){
    if(isEmpty())
      return true;
    return size < disks.get(0).size;
  }
  
  public boolean isDone(int level){
    return disks.size() == level;
  }
  
  public Disk popDisk(){
    Disk target = disks.get(0);
    disks.remove(0);
    return target;
  }
  
  public void pushDisk(Disk target){
    disks.add(0, target);
  }
  
  public void empty(){
    disks.clear();
  }
}

class Disk {
  public final int size;
  public int x = 196;
  public int y;
  private final Image disk;
  private final int width;

  public Disk(int size) {
    this.size = size;
    disk = new ImageIcon(getClass().getResource("resources/" + size + ".png")).getImage();
    width = disk.getWidth(null);
  }

  public void paint(Graphics g){
    g.drawImage(disk, x - (width / 2), y, null);
  }
}