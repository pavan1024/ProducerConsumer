package com.epam;

import java.util.List;
import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("rawtypes")
@Slf4j
class Producer implements Callable {

	private final List<Integer> sharedList;
	private static final int MAX_SIZE = 3;

	public Producer(List<Integer> sharedList) {
		this.sharedList = sharedList;
	}

	@Override
	public Object call() {
		for (int i = 0; i < 10; i++) {
			try {
				produce(i);
				log.info("Produced: " + i);
			} catch (InterruptedException ex) {
				log.warn(Producer.class.getName());
				Thread.currentThread().interrupt();
			}
		}
		return true;
	}

	private void produce(int i) throws InterruptedException {
		while (sharedList.size() == MAX_SIZE) {
			synchronized (sharedList) {
				log.info("The sharedList is full " + Thread.currentThread().getName() + " is waiting , size: "
						+ sharedList.size());
				sharedList.wait();
			}
		}
		synchronized (sharedList) {
			sharedList.add(i);
			sharedList.notifyAll();
		}
	}
}