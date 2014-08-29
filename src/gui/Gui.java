package gui;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import datasets.DataSetLoader;
import model.Model;
public class Gui extends JFrame {
  public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
  private static final class BuildGui implements Runnable {
    Model m;BuildGui(Model m){this.m=m;}
    public void run() {
      final Gui g = new Gui();
      g.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      g.getRootPane().setLayout(new BorderLayout());
      JPanel p=new Canvas(m);
      g.getRootPane().add(p,BorderLayout.CENTER);
      g.pack();
      g.setVisible(true);
      scheduler.scheduleAtFixedRate(
        new Runnable(){public void run() {g.repaint();}},
        500,25, TimeUnit.MILLISECONDS);
      }
    }
  private static final class MainLoop implements Runnable {
    Model m;MainLoop(Model m){this.m=m;}
    public void run() {
      try{
        while(true){
          long ut=System.currentTimeMillis();
          m.step();
          ut=System.currentTimeMillis()-ut;//used time
          long sleepTime=25-ut;
          if(sleepTime>1){ Thread.sleep(sleepTime);}
          }
        }
      catch(Throwable t){
        t.printStackTrace();
        System.exit(0);//not a perfect solution
        }
      }
    }
  public static void main(String[] args) {
    Model m=DataSetLoader.getRegularGrid(100, 800, 40);//Try those configurations
    //Model m=DataSetLoader.getRandomRotatingGrid(100, 800, 40);
    //Model m=DataSetLoader.getRandomSet(100, 800, 1000);
    //Model m=DataSetLoader.getRandomGrid(100, 800, 30);
    scheduler.schedule(new MainLoop(m), 500, TimeUnit.MILLISECONDS);
    SwingUtilities.invokeLater(new BuildGui(m));
    }
  }