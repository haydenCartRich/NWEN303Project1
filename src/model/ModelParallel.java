package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



public class ModelParallel extends AbstractModel {
	// Create a cached thread pool
	ExecutorService pool = Executors.newCachedThreadPool();
	public void step() throws InterruptedException, ExecutionException {
		final Map<Particle, Future<?>> processedParticles = new HashMap<Particle, Future<?>>(); //map of the Particle to the future changed particle
		final ModelParallel model = this; //have to store in this scope as can't access Model as this in the Runnable 

		//call interact on each particle
		for(final Particle p:model.p){
		processedParticles.put(p, pool.submit(new Runnable() {
			@Override
			public void run() {
					p.interact(model);//call interact in a separate thread for each particle in Model
			}
		}));
		}

		//wait for futures
		for (Particle p : processedParticles.keySet()) {
				processedParticles.get(p).get();
		}
		
		//merge the particles
		mergeParticles(); 
		/*I believe this is a bug in the system as mergeParticles should be calculated after 
		 * move. This is because we should calculate the speed and gravitational pull of a particle and
		 * then move that particle as one 'atomic' operation. We can then merge the particles together at the
		 * end so in the next step their now combined speed and gravitational pull are calculated as one mass.*/
		
		//move each particle on the model. Not parallel as not worth overhead of move method which just has two statements.
		for(Particle p:this.p) p.move(this);
		
		updateGraphicalRepresentation();
	}
	private void updateGraphicalRepresentation() {
		ArrayList<DrawableParticle> d=new ArrayList<DrawableParticle>();
		Color c=Color.ORANGE;
		for(Particle p:this.p){
			d.add(new DrawableParticle((int)p.x, (int)p.y, (int)Math.sqrt(p.mass),c ));
		}
		this.pDraw=d;//atomic update
	}
	public void mergeParticles(){
		Stack<Particle> deadPs=new Stack<Particle>();
		for(Particle p:this.p){ if(!p.impacting.isEmpty()){deadPs.add(p);}; }
		this.p.removeAll(deadPs);
		while(!deadPs.isEmpty()){
			Particle current=deadPs.pop();
			Set<Particle> ps=getSingleChunck(current);
			deadPs.removeAll(ps);
			this.p.add(mergeParticles(ps));
		}
	}
	private Set<Particle> getSingleChunck(Particle current) {
		Set<Particle> impacting=new HashSet<Particle>();
		impacting.add(current);
		while(true){
			Set<Particle> tmp=new HashSet<Particle>();
			for(Particle pi:impacting){tmp.addAll(pi.impacting);}
			boolean changed=impacting.addAll(tmp);
			if(!changed){break;}
		}
		//now impacting have all the chunk of collapsing particles
		return impacting;
	}
	public Particle mergeParticles(Set<Particle> ps){
		double speedX=0;
		double speedY=0;
		double x=0;
		double y=0;
		double mass=0;
		for(Particle p:ps){  mass+=p.mass; }
		for(Particle p:ps){
			x+=p.x*p.mass;
			y+=p.y*p.mass;
			speedX+=p.speedX*p.mass;
			speedY+=p.speedY*p.mass;
		}
		x/=mass;
		y/=mass;
		speedX/=mass;
		speedY/=mass;
		return new Particle(mass,speedX,speedY,x,y);
	}
}
