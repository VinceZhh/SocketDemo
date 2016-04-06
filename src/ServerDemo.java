import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerDemo {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService es = Executors.newCachedThreadPool();
		Vector<ClientThread> list = new Vector<ClientThread>();
		// 端口号从1024-65535
		try {
			ServerSocket ss = new ServerSocket(8000);
			System.out.println("服务器已启动，正等待链接。。。");
			while (true) {
				Socket socket = ss.accept();// 等待客户端链接
				ClientThread c = new ClientThread(socket, list);
				es.execute(c);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class ClientThread implements Runnable {
		private Socket socket;
		private Vector<ClientThread> list;
		private String name;
		ObjectOutputStream out;

		public ClientThread(Socket socket, Vector<ClientThread> list) {
			list.add(this);
			this.list = list;
			this.socket = socket;
		}

		public void run() {
			try {
				System.out.println("客户端的ip为" + socket.getInetAddress().getHostAddress());
				out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

				while (true) {
					Info info = (Info) in.readObject();
					if (info.getType() == 0) {
						name = info.getFrom();
						Info welcome = new Info();
						welcome.setInfo("welcome" + name);
						out.writeObject(welcome);
					} else if(info.getType()==1){
						for(int i=0;i<list.size();i++){
							ClientThread ct = list.get(i);
							if(ct.name.equals(info.getTO())){
								ct.out.writeObject(info);
								break;
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
