package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import model.AbstractModel;
import model.DrawableParticle;
import model.Model;
import model.Particle;

public class Canvas extends JPanel{
	AbstractModel m; Canvas(AbstractModel m2){this.m=m2;}
	@Override public void paint(Graphics gg){
		Graphics2D g=(Graphics2D)gg;
		g.setBackground(Color.DARK_GRAY);
		g.clearRect(0, 0, getWidth(),getHeight());
		for(DrawableParticle p: m.pDraw){p.draw(g);}
	}
	@Override public Dimension getPreferredSize(){
		return new Dimension((int)AbstractModel.size, (int)AbstractModel.size);
	}
}
