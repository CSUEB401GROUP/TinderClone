package com.tinderclone.client.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Stack;
import com.tinderclone.common.entity.DataValue;
import com.tinderclone.client.Global;

public class DataRequest {
	
	private String server;

	private Integer port;
	private Stack<DataValue> req_msg = new Stack<DataValue>();
	
	
	private Socket socket;
	private ObjectOutputStream request;
	private ObjectInputStream response;
	private long lastbeat;
	
	public DataRequest() {
		super();
		this.server = Global.SERVER_URL;
		this.port = Global.SERVOER_PORT;
		Global.lastbeat = System.currentTimeMillis();
		try{
			//Establish socket connections for I/O operations
			this.socket = new Socket(this.server, this.port);
			this.request = new ObjectOutputStream(this.socket.getOutputStream());
			this.response = new ObjectInputStream(this.socket.getInputStream());

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public DataResponse send() {
		return this.send(null);
	}
	
	public DataResponse send(Object object) {
		DataResponse response = null;
		try {
			this.request.writeObject(req_msg);
			if (object != null) {
				this.request.writeObject(object);
			}
			Stack<DataValue> recv_msg = null;
			recv_msg = (Stack<DataValue>) this.response.readObject();
			if (recv_msg != null) {
				response = new DataResponse(recv_msg);
			}
		} catch(ClassNotFoundException e) {
			System.out.println(e);
		} catch(UnknownHostException e) {
			System.out.println(e);
		} catch(IOException e) {
			System.out.println(e);
		} catch(Exception e) {
			System.out.println(e);
		} finally {
			try {
				this.request.close();
				this.response.close();
				this.socket.close();
			} catch(Exception e) {
				System.out.println(e);
			}
		}
		// close the connection
		return response;
	}
	
	public Object send2Object() {
		Object result = null;
		DataResponse response = null;
		try {
			this.request.writeObject(req_msg);
			System.out.println("Connected");
			Stack<DataValue> recv_msg = null;
			recv_msg = (Stack<DataValue>) this.response.readObject();
			if (recv_msg != null) {
				response = new DataResponse(recv_msg);
				if (response.isSuccess()) {
					result = this.response.readObject();
				}
			}
		} catch(ClassNotFoundException e) {
			System.out.println(e);
		} catch(UnknownHostException e) {
			System.out.println(e);
		} catch(IOException e) {
			System.out.println(e);
		} catch(Exception e) {
			System.out.println(e);
		} finally {
			try {
				this.request.close();
				this.response.close();
				this.socket.close();
			} catch(Exception e) {
				System.out.println(e);
			}
		}
		// close the connection
		return result;
	}
	
	public void setServer(String server) { this.server = server; }

	public void setPort(Integer port) { this.port = port; }
	
	public void addValue(String name, String value) {
		DataValue param = new DataValue(name, value);
		this.req_msg.push(param);
	}
	
}
