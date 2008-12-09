package vidis.ui.mvc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import vidis.ui.events.IVidisEvent;
import vidis.ui.events.JobAppend;
import vidis.ui.events.jobs.IJob;
import vidis.ui.mvc.api.AController;
import vidis.ui.mvc.api.Dispatcher;

/**
 * a job controller that takes jobs and executes them
 * @author Dominik
 *
 */
public class JobController extends AController {
	private static Logger logger = Logger.getLogger(JobController.class);
	
	private BlockingQueue<Runnable> jobs = new ArrayBlockingQueue<Runnable>( 150 );
	private ThreadPoolExecutor executer = new ThreadPoolExecutor( 3, 3, 30, TimeUnit.SECONDS, jobs);
	
	public JobController() {
		registerEvent( IVidisEvent.AppendJob, IVidisEvent.CleanDoneJobs );
		// register the job remover job :-P
		Dispatcher.forwardEvent( new JobAppend(new IJob() {
			private double executeEvery = 5000;
			private double last = System.currentTimeMillis();
			public boolean mustExecuteUniquely() {
				return true;
			}
			public void run() {
				if(System.currentTimeMillis() - last < executeEvery) {
					// execute a cleanup
					Dispatcher.forwardEvent( IVidisEvent.CleanDoneJobs );
					last = System.currentTimeMillis();
				}
			}
		}) );
	}
	
	private Map<IJob, Future<?>> futureJobs = new HashMap<IJob, Future<?>>();
	private List<IJob> futureJobsToRemove = new LinkedList<IJob>();
	
	private void removeDoneFutureJobs() {
		for(Entry<IJob, Future<?>> e : futureJobs.entrySet()) {
			if(e.getValue().isDone()) {
				// report it
				logger.info("Finished job: " + e.getKey());
				// clean done jobs
				futureJobsToRemove.add(e.getKey());
			}
		}
		// remove unused futures
		for(IJob tmp : futureJobsToRemove)
			futureJobs.remove(tmp);
		futureJobsToRemove.clear();
	}
	
	@Override
	public void handleEvent(IVidisEvent event) {
		switch ( event.getID() ) {
			case IVidisEvent.CleanDoneJobs:
				removeDoneFutureJobs();
			break;
			case IVidisEvent.AppendJob:
				// first of all do some burocrazy: clean done jobs
				Dispatcher.forwardEvent(IVidisEvent.CleanDoneJobs);
				// now handle the new job
				if ( event instanceof JobAppend ) {
					IJob j = ((JobAppend)event).getJob();
					boolean submit = true;
					if( j.mustExecuteUniquely() ) {
						// check futures
						for(Entry<IJob, Future<?>> e : futureJobs.entrySet()) {
							if(e.getKey().getClass().equals(j.getClass())) {
								// cancel old job
								if(e.getValue().cancel(true)) {
									// job cancelled, fine
								} else {
									// could not be cancelled, do not submit new job
									submit = false;
								}
							}
						}
					}
					if(submit) {
						Future<?> fj = executer.submit(j);
						futureJobs.put(j, fj);
					}
				} else
					logger.warn("received a job that I cannot handle; claims to be append job event but the class is not compatible!");
				break;
		}
	}
}
