package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class AbstractModel {
	//these fields affect both sequential and parallel Models. Had to be abstracted away to allow calls from other methods without messy casting etc
	  public static final double size=900;
	  public static final double gravitationalConstant=0.1;
	  public static final double lightSpeed=10;
	  public static final double timeFrame=1;
	  public List<Particle> p=new ArrayList<Particle>();
	  public volatile List<DrawableParticle> pDraw=new ArrayList<DrawableParticle>();
	  public abstract void step() throws InterruptedException, ExecutionException;
}
