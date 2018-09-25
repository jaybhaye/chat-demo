package com.test.grpc.chat.repositories;

import com.test.grpc.chat.types.Client;

public interface ClientRepository {
	public void addClient(Client client);
	public Client getClient(String userId);
}
