package com.epam;

import java.util.List;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("rawtypes")
class Producer implements Callable {
	private static final Logger LOGGER = LogManager.getLogger(Producer.class);
	private final List<Integer> sharedList;
	private static final int MAX_SIZE = 3;

	public Producer(List<Integer> sharedList) {
		this.sharedList = sharedList;
	}

	@Override
	public Object call() {
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(100);
				produce(i);
				LOGGER.info("Produced: " + i);
			} catch (InterruptedException ex) {
				LOGGER.warn(Producer.class.getName());
				Thread.currentThread().interrupt();
			}
		}
		return true;
	}

	private void produce(int i) throws InterruptedException {
		while (sharedList.size() == MAX_SIZE) {
			synchronized (sharedList) {
				LOGGER.info("The sharedList is full " + Thread.currentThread().getName() + " is waiting , size: "
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