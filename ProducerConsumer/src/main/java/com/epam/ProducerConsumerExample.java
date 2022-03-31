package com.epam;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ProducerConsumerExample {
	public static void main(String[] args) throws Exception {
		List<Integer> numList = new ArrayList<>();
		Callable<?> producer = new Producer(numList);
		Callable<?> consumer = new Consumer(numList);
		FutureTask<?> futureProducer = new FutureTask<>(producer);
		FutureTask<?> futureConsumer = new FutureTask<>(consumer);

		Thread t1 = new Thread(futureProducer);
		t1.start();
		Thread t2 = new Thread(futureConsumer);
		t2.start();
	}
}
