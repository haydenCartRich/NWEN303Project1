package tests;

import java.util.concurrent.ExecutionException;

import model.AbstractModel;
import model.Model;
import model.ModelParallel;
import datasets.DataSetLoader;

public class EfficiencyTest {
	static final int TEST_FRAME_LIMIT = 1000;
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		/**Record execution time of each configuration in parallel*/
		AbstractModel m=DataSetLoader.getRegularGrid(100, 800, 40, new ModelParallel());
		long RegularGridParallelTime =System.currentTimeMillis();
		for(int i = 0; i < TEST_FRAME_LIMIT; i++){
			m.step();
		}
		RegularGridParallelTime=System.currentTimeMillis()-RegularGridParallelTime;//used time
		
		m=DataSetLoader.getRandomRotatingGrid(100, 800, 40, new ModelParallel());
		long RandomRotatingGridParallelTime =System.currentTimeMillis();
		for(int i = 0; i < TEST_FRAME_LIMIT; i++){
			m.step();
		}
		RandomRotatingGridParallelTime=System.currentTimeMillis()-RandomRotatingGridParallelTime;//used time
		
		m=DataSetLoader.getRandomSet(100, 800, 1000, new ModelParallel());
		long RandomSetParallelTime =System.currentTimeMillis();
		for(int i = 0; i < TEST_FRAME_LIMIT; i++){
			m.step();
		}
		RandomSetParallelTime=System.currentTimeMillis()-RandomSetParallelTime;//used time
		
		m=DataSetLoader.getRandomGrid(100, 800, 30, new ModelParallel());
		long RandomGridParallelTime =System.currentTimeMillis();
		for(int i = 0; i < TEST_FRAME_LIMIT; i++){
			m.step();
		}
		RandomGridParallelTime=System.currentTimeMillis()-RandomGridParallelTime;//used time
		
		
		
		/**Record execution time of each configuration running sequentially*/
		m=DataSetLoader.getRegularGrid(100, 800, 40, new Model());
		long RegularGridTime =System.currentTimeMillis();
		for(int i = 0; i < TEST_FRAME_LIMIT; i++){
			m.step();
		}
		RegularGridTime=System.currentTimeMillis()-RegularGridTime;//used time
		
		m=DataSetLoader.getRandomRotatingGrid(100, 800, 40, new Model());
		long RandomRotatingGridTime =System.currentTimeMillis();
		for(int i = 0; i < TEST_FRAME_LIMIT; i++){
			m.step();
		}
		RandomRotatingGridTime=System.currentTimeMillis()-RandomRotatingGridTime;//used time
		
		m=DataSetLoader.getRandomSet(100, 800, 1000, new Model());
		long RandomSetTime =System.currentTimeMillis();
		for(int i = 0; i < TEST_FRAME_LIMIT; i++){
			m.step();
		}
		RandomSetTime=System.currentTimeMillis()-RandomSetTime;//used time
		
		m=DataSetLoader.getRandomGrid(100, 800, 30, new Model());
		long RandomGridTime =System.currentTimeMillis();
		for(int i = 0; i < TEST_FRAME_LIMIT; i++){
			m.step();
		}
		RandomGridTime=System.currentTimeMillis()-RandomGridTime;//used time
		
		
		/**Calculate efficiency and improvement*/
		long totalParallelTime = RegularGridParallelTime + RandomRotatingGridParallelTime + RandomSetParallelTime + RandomGridParallelTime;
		long totalSequentialTime = RegularGridTime + RandomRotatingGridTime + RandomSetTime + RandomGridTime;
		long averageParallelTime = totalParallelTime/4;
		long averageSequentialTime = totalSequentialTime/4;
		long RegularGridImprovement = RegularGridTime - RegularGridParallelTime;
		long RandomRotatingGridImprovement = RandomRotatingGridTime - RandomRotatingGridParallelTime;
		long RandomSetImprovement = RandomSetTime - RandomSetParallelTime;
		long RandomGridImprovement = RandomGridTime - RandomGridParallelTime;
		long averageImprovement = (totalSequentialTime - totalParallelTime) / 4;

		/**report results to System.out*/
		System.out.println("Total Sequential Execution Time: " + totalSequentialTime);
		System.out.println("Total Parallel Execution Time: " + totalParallelTime);
		System.out.println("\nAverage Sequential Execution Time: " + averageSequentialTime);
		System.out.println("Average Parallel Execution Time: " + averageParallelTime);
		System.out.println("\nTime saved by using parallel for each configuration:");
		System.out.println("Regular Grid: " + RegularGridImprovement);
		System.out.println("Random Rotating Grid: " + RandomRotatingGridImprovement);
		System.out.println("Random Set: " + RandomSetImprovement);
		System.out.println("Random Grid: " + RandomGridImprovement);
		System.out.println("Average time saved over all four configurations: " + averageImprovement);
		
	}
}
