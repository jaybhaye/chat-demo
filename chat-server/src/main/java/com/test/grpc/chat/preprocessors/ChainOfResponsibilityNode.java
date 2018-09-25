package com.test.grpc.chat.preprocessors;

import com.test.grpc.chat.types.exceptions.ChainProcessingException;

public abstract class ChainOfResponsibilityNode<T> {
	ChainOfResponsibilityNode<T> next;
	
	public ChainOfResponsibilityNode<T> withNext(ChainOfResponsibilityNode<T> next){
		this.next = next;
		return this;
	}
	
	public void execute(T t) throws ChainProcessingException {
		process(t);
		if (next != null) {
			next.execute(t);
		}
	}
	
	protected abstract void process(T t) throws ChainProcessingException;
}
