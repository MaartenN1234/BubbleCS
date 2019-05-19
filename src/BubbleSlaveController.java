import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class BubbleSlaveController extends Thread {
	private Map<BubbleState, BubbleState> meetInMiddleForward;
	private Queue<BubbleState> queueIn;
	private List<BubbleState> queueOut;

	public final static int CONTROLLER_PORT = 5550;
	String [] hosts = {"localhost:5551", "localhost:5552"};
	BubbleState[] sentStates = new BubbleState[hosts.length];

	
	public BubbleSlaveController(Map<BubbleState, BubbleState> meetInMiddleForward){
		this.meetInMiddleForward = meetInMiddleForward;
		queueIn  = new ArrayBlockingQueue<BubbleState>(1000);
		queueOut = new ArrayList<BubbleState>();
		setDaemon(true);
		start();
	}
	
	public void submitMiddleState(BubbleState state){
		while(!queueIn.offer(state)){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
	}
	public synchronized List<BubbleState> getNewFinalStates(){
		List<BubbleState> result = queueOut;
		queueOut = new ArrayList<BubbleState>();
		return result;
	}
	
	@SuppressWarnings("resource")
	public void run(){
		(new SendDaemon()).start();
		ServerSocket ss;
		try {
			ss = new ServerSocket(CONTROLLER_PORT);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		while (true){
			Socket s;
			try {
				s = ss.accept();
				(new RecieveThread(s)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private void callBack(BubbleState state, byte[] moves) {
		BubbleState finalState = state;
		for(byte move : moves){
			BubbleState.applyMove(finalState, move);
		}
		
		synchronized(this){
			meetInMiddleForward.put(state, finalState);
			queueOut.add(finalState);
		}
	}


	private Socket getWorkerSocket(int i) {
		String host = hosts[i].split(":")[0];
		int    port = Integer.parseInt(hosts[i].split(":")[1]);
		try {
			return new Socket(host, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	private class SendDaemon extends Thread{
		SendDaemon(){
			setDaemon(true);
		}
		public void run(){
			while(true){
				for (int i = 0; i<hosts.length; i++){
					if (sentStates[i] == null){
						Socket s = getWorkerSocket(i);
						if (s!= null){
							BubbleState nextWork = queueIn.poll();
							if (nextWork != null){
								try{
									System.out.println("sending");
									ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
									oos.writeObject(new Integer(i));
									oos.writeObject(nextWork.state);
									oos.flush();
									oos.close();
									s.close();
									sentStates[i] = nextWork;
								} catch(IOException e){
									e.printStackTrace();
								} finally {
									if (!s.isClosed())
										try {
											s.close();
										} catch (IOException e) {
											e.printStackTrace();
										}
								}
							} else {
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}
	
	private class RecieveThread extends Thread{
		Socket s;
		RecieveThread(Socket s){
			this.s = s;
		}
		public void run(){
			try{
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
		
				Object o1 = ois.readObject();
				Object o2 = ois.readObject();
				int i = (Integer) o1;
				callBack(sentStates[i], (byte []) o2);
				sentStates[i] = null;
				ois.close();
				s.close();
			} catch (IOException | ClassNotFoundException e){
				e.printStackTrace();
			}
		}
	}
}
