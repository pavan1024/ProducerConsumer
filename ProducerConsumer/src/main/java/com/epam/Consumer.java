package com.epam;

import java.util.List;
import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("rawtypes")
@Slf4j
class Consumer implements Callable {
	List<Integer> sharedList = null;

	public Consumer(List<Integer> sharedList) {
		this.sharedList = sharedList;
	}

	public Object call() {
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(10);
				log.info("Consumed: " + consume());
			} catch (InterruptedException ex) {
				log.warn(Consumer.class.getName());
				Thread.currentThread().interrupt();
			}
		}
		return true;
	}

	private int consume() throws InterruptedException {
		while (sharedList.isEmpty()) {
			synchronized (sharedList) {
				log.info("The shared list is empty " + Thread.currentThread().getName() + " is waiting , size: "
						+ sharedList.size());
				sharedList.wait();
			}
		}
		synchronized (sharedList) {
			sharedList.notifyAll();
			return sharedList.remove(0);
		}
	}
}
