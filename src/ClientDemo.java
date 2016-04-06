import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.text.InternationalFormatter;

public class ClientDemo {
	public static void main(String[] args) throws ClassNotFoundException {
		// 连接服务器
		try {
			Socket socket = new Socket("localhost", 8000);
			System.out.println("连接成功");
			boolean flag = true;
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			Scanner input = new Scanner(System.in);
			System.out.println("请输入姓名");
			String name = input.nextLine();
			Info info = new Info();
			info.setFrom(name);
			info.setType(0);// 表示登陆
			out.writeObject(info);
			info = (Info) in.readObject();// 系统返回消息
			System.out.println(info.getInfo());
			new Thread(new ReadInfoThread(in)).start();
			while (flag) {
				info = new Info();
				System.out.println("to:");
				info.setTO(input.nextLine());
				System.out.println("info");
				info.setInfo(input.nextLine());
				info.setFrom(name);
				info.setType(1);
				//System.out.println(info.getInfo());
				out.writeObject(info);
			}
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class ReadInfoThread implements Runnable {
		private ObjectInputStream in;
		public boolean flag = true;

		public ReadInfoThread(ObjectInputStream in) {
			this.in = in;
			flag = true;
		}

		@Override
		public void run() {
			while (flag) {
				try {
					Info info = (Info) in.readObject();
					System.out.println("-----接收到了信息----");
					System.out.println(info.getInfo());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
