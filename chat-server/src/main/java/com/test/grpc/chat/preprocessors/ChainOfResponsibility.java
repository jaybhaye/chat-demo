package com.test.grpc.chat.preprocessors;

import com.test.grpc.chat.types.exceptions.ChainProcessingException;

public class ChainOfResponsibility<T> {
	
	ChainOfResponsibilityNode<T> head;
	Object monitor = new Object();
	
	public synchronized void addNode(ChainOfResponsibilityNode<T> node) {
		if (head == null) {
			head = node;
			return;
		}

		ChainOfResponsibilityNode<T> tmp = head;
		while (tmp.next != null) {
			tmp = tmp.next;
		}
	}
	
	public void execute(T t) throws ChainProcessingException {
		if(head == null) {
			throw new ChainProcessingException("No nodes added to this chain.");
		}
		head.process(t);
	}
}
