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
		int i = 0;
		while (i < 10) {
			try {
				log.info("Consumed: " + consume());
				i++;
				Thread.sleep(100);
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
			sharedList.notify();
			return (Integer) sharedList.remove(0);
		}
	}
}
