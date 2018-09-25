package com.test.grpc.chat.repositories;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.test.grpc.chat.types.Client;

public class ClientRepositoryImpl implements ClientRepository {

	private static ClientRepositoryImpl INSTANCE = new ClientRepositoryImpl();
	private Map<String, Client> clients;
	private ClientRepositoryImpl() {
		clients = new ConcurrentHashMap<String, Client>();
	}
	
	public static ClientRepositoryImpl getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void addClient(Client client) {
		clients.put(client.getClientId(), client);
	}

	@Override
	public Client getClient(String clientId) {
		return clients.get(clientId);
	}
}
