package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * 
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {

	private T result;
	private boolean done; //Volatile is something GPT gave me - says its critical here

	/**
	 * This should be the the only public constructor in this class.
	 */
	public Future() {
		this.done = false;
		this.result = null;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     * @return return the result of type T if it is available, if not wait until it is available.
     * 	       
     */
	public synchronized T get() {
		//TODO: implement this.
		while (!this.done)
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		return result;
	}

	/**
     * Resolves the result of this Future object.
     */
	public synchronized void resolve (T result) {
		//TODO: implement this.
		if(!done){
			this.result = result;
			this.done = true;
			notifyAll();
		}

	}
	
	/**
     * @return true if this object has been resolved, false otherwise
     */
	public synchronized boolean isDone() {
		//TODO: implement this.
		return this.done;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     * @param timeout 	the maximal amount of time units to wait for the result.
     * @param unit		the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not, 
     * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
     *         elapsed, return null.
     */
		public synchronized T get(long timeout, TimeUnit unit) {
			if (this.done) {
				return result;  // Return immediately if already resolved
			}

			long millisTimeout = unit.toMillis(timeout);  // Convert timeout to milliseconds
			long endTime = System.currentTimeMillis() + millisTimeout;  // Calculate end time

			while (!this.done && millisTimeout > 0) {
				try {
					wait(millisTimeout);  // Wait for the specified duration
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();  // Preserve interrupt status
					return null;  // Return null if interrupted
				}
				millisTimeout = endTime - System.currentTimeMillis();  // Update remaining time
			}

			return this.done ? result : null;  // Return result if done, else null
		}

	}


