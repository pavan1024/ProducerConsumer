package com.epam;

import java.util.List;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("rawtypes")
class Consumer implements Callable {
	private static final Logger LOGGER = LogManager.getLogger(Consumer.class);
	List<Integer> sharedList = null;

	public Consumer(List<Integer> sharedList) {
		this.sharedList = sharedList;
	}

	public Object call() {
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(300);
				LOGGER.info("Consumed: " + consume());
			} catch (InterruptedException ex) {
				LOGGER.warn(Consumer.class.getName());
				Thread.currentThread().interrupt();
			}
		}
		return true;
	}

	private int consume() throws InterruptedException {
		int res;
		while (sharedList.isEmpty()) {
			synchronized (sharedList) {
				LOGGER.info("The shared list is empty " + Thread.currentThread().getName() + " is waiting , size: "
						+ sharedList.size());
				sharedList.wait();
			}
		}
		synchronized (sharedList) {
			res = sharedList.remove(0);
			sharedList.notifyAll();
			return res;
		}
		
	}
}
