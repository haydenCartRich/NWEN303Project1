package tests;

import static org.junit.Assert.*;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import model.AbstractModel;
import model.Model;
import model.ModelParallel;
import model.Particle;

import org.junit.Test;

import datasets.DataSetLoader;

public class CorrectnessTest {

	final int TEST_FRAME_LIMIT = 500;
	
	@Test
	public void testRegularGrid() throws InterruptedException, ExecutionException {
		if(!compareParallelSequential(DataSetLoader.getRegularGrid(100, 800, 40, new Model()), 
				DataSetLoader.getRegularGrid(100, 800, 40, new ModelParallel()))) fail("Regular Grid models were not equal after " + TEST_FRAME_LIMIT + " frames.");
	}
	
	@Test
	public void testRandomGrid() throws InterruptedException, ExecutionException {
		if(!compareParallelSequential(DataSetLoader.getRandomGrid(100, 800, 40, new Model()), 
				DataSetLoader.getRandomGrid(100, 800, 40, new ModelParallel()))) fail("Random Grid models were not equal after " + TEST_FRAME_LIMIT + " frames.");
	}
	
	@Test
	public void testRandomSet() throws InterruptedException, ExecutionException {
		if(!compareParallelSequential(DataSetLoader.getRandomSet(100, 800, 40, new Model()), 
				DataSetLoader.getRandomSet(100, 800, 40, new ModelParallel()))) fail("Random Set models were not equal after " + TEST_FRAME_LIMIT + " frames.");
	}
	
	@Test
	public void testRandomRotatingGrid() throws InterruptedException, ExecutionException {
		if(!compareParallelSequential(DataSetLoader.getRandomRotatingGrid(100, 800, 40, new Model()), 
				DataSetLoader.getRandomRotatingGrid(100, 800, 40, new ModelParallel()))) fail("Random Rotating Grid models were not equal after " + TEST_FRAME_LIMIT + " frames.");
	}
	
	@Test
	public void testBothSequential() throws InterruptedException, ExecutionException {
		if(!compareParallelSequential(DataSetLoader.getRandomRotatingGrid(100, 800, 40, new Model()), 
				DataSetLoader.getRandomRotatingGrid(100, 800, 40, new Model()))) fail("Test failed on two sequential models after" + TEST_FRAME_LIMIT + " frames. There must be some other variance than my parallel code.");
	}
	
	
	private boolean compareParallelSequential (AbstractModel m1, AbstractModel m2) throws InterruptedException, ExecutionException{
		
		//make steps on models
		for(int i = 0; i < TEST_FRAME_LIMIT; i++){
			m1.step();
			m2.step();
		}
		
		//if lists are different sizes we can fail right away
		if(m1.p.size() != m2.p.size()) return false;
		
		
		for(Particle pPar : m1.p){
			boolean foundMatch = false;
			for(Particle pSeq : m2.p){
				if(compareParticles(pPar,pSeq)){
					foundMatch = true;//so we know you were successful
					m2.p.remove(pSeq);//remove so another particle from m1 can't match to it (in case they're the same)
					break;//next particle of m1
				}
			}
			if(!foundMatch) return false;//no same particle exists so return false
		}
		
		return true;
	}


	//checks all the fields of both particles are the same and returns true. Otherwise returns false.
	private boolean compareParticles(Particle p1, Particle p2) {
		if(p1.mass == p2.mass && p1.speedX == p2.speedX && p1.speedY == p2.speedY && p1.x == p2.x && p1.y == p2.y) return true;
		return false;
	}
	
	
	
}
