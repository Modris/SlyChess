package com.modris.model;

import org.springframework.stereotype.Component;

@Component
public class ClientObject{

	private String fen;
	private String userId;
	private int chosenElo; // SimpleMessageConverter only supports String, byte[] and Serializable payloads,
	
	public ClientObject() {}
	
	public ClientObject(String fen, String userId, int chosenElo) {
		this.fen = fen;
		this.userId = userId;
		this.chosenElo = chosenElo;
	}

	
	@Override
	public String toString() {
		return "Fen [fen=" + fen + ", userId=" + userId + ", chosenElo=" + chosenElo + "]";
	}


	public int getChosenElo() {
		return chosenElo;
	}

	public void setChosenElo(int chosenElo) {
		this.chosenElo = chosenElo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFen() {
		return fen;
	}

	public void setFen(String fen) {
		this.fen = fen;
	}
	
	
}
