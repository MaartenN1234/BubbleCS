import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BubbleSlaveNode {
	
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		ServerSocket ss = null;
		while(true){
			try {
				ss = new ServerSocket(port);
				
				long counter=0;
				while(true){
					Socket s = null;
					try{
						s = ss.accept();
						InetAddress controllerHostAdress = s.getInetAddress();
						
						ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
						Object o1 = ois.readObject();
						Object o2 = ois.readObject();
						ois.close();
						s.close();
						Integer slaveID = (Integer) o1;
						byte[] st = (byte[]) o2;
						System.out.println((counter++) +"received");
						
						
						BubbleState state = new BubbleState(st);
						BubbleState best  = BubbleSolver.getOptimalHeuristic(state, false);
						System.out.println((counter++) +"optimized");
						
						s = new Socket (controllerHostAdress, BubbleSlaveController.CONTROLLER_PORT);
						ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
						oos.writeObject(slaveID);
						oos.writeObject(best.getMoves());
						oos.flush();
						oos.close();
						s.close();
						System.out.println((counter++) +"sent");
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} finally{
						if (s != null && !s.isClosed())
							s.close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				if (ss != null && !ss.isClosed())
					try {
						ss.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}
}
