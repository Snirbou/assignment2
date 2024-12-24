package bgu.spl.mics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only one public method (in addition to getters which can be public solely for unit testing) may be added to this class
 * All other methods and members you add the class must be private.
 */
public class MessageBusImpl implements MessageBus {

	private final static MessageBusImpl INSTANCE = new MessageBusImpl();

	private ConcurrentHashMap<MicroService, BlockingQueue<Message>> serviceMap;

	private ConcurrentHashMap<Class<? extends Event>, BlockingQueue<MicroService>> eventSubscribersMap;
	private ConcurrentHashMap<Class<? extends Broadcast>, List<MicroService>> broadcastsSubscribersMap;

	private ConcurrentHashMap<Event<?>, Future<?>> eventFuturesMap;

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {

		eventSubscribersMap.compute(type, (key, existingList) -> {
			if (existingList == null) {
				existingList = new LinkedBlockingQueue<>();
			}
			existingList.add(m);  // Add the microservice to the list
			return existingList;
		});
	}
			@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
			broadcastsSubscribersMap.compute(type, (key, existingList) -> {
				if (existingList == null) {
					existingList = Collections.synchronizedList(new ArrayList<>());
				}
				existingList.add(m);  // Add the microservice to the list
				return existingList;
			});
	}

	@Override
	public <T> void complete(Event<T> e, T result) {

		Future<T> future = (Future<T>) eventFuturesMap.get(e);

		if (future != null) {
			future.resolve(result);  // אם קיים, פותרים את ה-Future
			eventFuturesMap.remove(e);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		//Sends broadcasts to all of the subscribers using round-robin


		List<MicroService> sendToList = broadcastsSubscribersMap.get(b);
		for(MicroService m : sendToList)
		{
			serviceMap.compute(m, (key, msgQ) -> {
				if (msgQ == null) {
					msgQ = new LinkedBlockingQueue<>();
				}
                try {
                    msgQ.put(b);  // Add the microservice to the list
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return msgQ;
			});
		}


	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) throws InterruptedException {

	//Sends event to one of the subscribers using round-robin

		BlockingQueue<MicroService> eventRoundRobinQ = eventSubscribersMap.get(e.getClass());

		if (eventRoundRobinQ == null || eventRoundRobinQ.isEmpty()) {
			// No subscribers for this event type
			return null;
		}

		MicroService getter = eventRoundRobinQ.poll();

		if (getter == null) {
			return null;  // No available subscriber at the moment
		}

		serviceMap.get(getter).put(e); //maybe change to offer
		eventRoundRobinQ.offer(getter); //maybe change to put

		Future<T> future = new Future<>();
		eventFuturesMap.put(e, future);

		return future;
	}

	@Override
	public void register(MicroService m) {
		BlockingQueue<Message> newQ = new LinkedBlockingQueue<>();
		this.serviceMap.putIfAbsent(m, newQ);
	}

	@Override
	public void unregister(MicroService m) {
		this.serviceMap.remove(m);

		//Remove subscription from events

		for(Map.Entry<Class<? extends Event>, BlockingQueue<MicroService>> entry : eventSubscribersMap.entrySet())
		{
			BlockingQueue<MicroService> microservices = entry.getValue();
			microservices.removeIf(m::equals);
		}

		//Remove subscription from broadcasts

		for(Map.Entry<Class<? extends Broadcast>, List<MicroService>> entry : broadcastsSubscribersMap.entrySet())
		{
			List<MicroService> microservices = entry.getValue();
			synchronized (microservices){
				microservices.removeIf(m::equals);
			}
		}

	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {

		try {
			return serviceMap.get(m).take();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw e;
		}

	}

	
	private MessageBusImpl() {

		serviceMap = new ConcurrentHashMap<>();
		eventSubscribersMap = new ConcurrentHashMap<>();
		broadcastsSubscribersMap = new ConcurrentHashMap<>();
		eventFuturesMap = new ConcurrentHashMap<>();

	};

	public static MessageBusImpl getInstance()
	{
		return INSTANCE;
	}



}
