package socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

/**
 * �����ҷ����
 * @author ta
 *
 */
public class Server {
	/*
	 * �����ڷ���˵�ServerSocket
	 * ����������
	 * 1:��ϵͳ�������˿ڣ��ͻ��˾���ͨ��
	 *   ����˿������˳��������ӵġ�
	 *   
	 * 2:�����ö˿ڣ����ͻ���ͨ���ö˿�������
	 *   ��������ʱ���Զ�����һ��Socket��ͨ��
	 *   ���Socket��ͻ��˽������ݽ�����  
	 */
	private ServerSocket server;
	/*
	 * ���������ڱ�������ClientHandler�ڲ�
	 * ��Ӧ�ͻ��˵���������Ա�㲥��Ϣ��
	 * 
	 * �����ڲ�����Է������Ӧ�ⲿ������ԣ�
	 * �Դ�������Server�ж�������飬���е�
	 * �ڲ���ClientHandler�����Կ�����������
	 * ����ЩClientHandler��Ҫ��������ݴ���
	 * ������鼴�ɡ�
	 */
//	private PrintWriter[] allOut = {};
	private Collection<PrintWriter> allOut
				= new ArrayList<PrintWriter>();
	
	public Server() {
		try {
			/*
			 * ʵ����ServerSocket��ͬʱ��ϵͳ
			 * �������˿ڣ��ö˿ڲ�����ϵͳ
			 * ���е�����Ӧ�ó�����ͬ�������
			 * �׳���ַ��ռ�õ��쳣
			 */
			System.out.println("�������������...");
			server = new ServerSocket(8088);
			System.out.println("������������!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		try {
			/*
			 * ServerSocket��һ����Ҫ�ķ���:
			 * Socket accept()
			 * �÷�����һ���������������ú�������
			 * ����"��ס��"����ʱ��ʼ��������˿ڵ�
			 * ���ͻ��˵����ӡ���ô���ͻ���ͨ���˿�
			 * ��������ʱ��accept�᷵��һ��Socket��
			 * ͨ����Socket�Ϳ�����ս������ӵĿͻ�
			 * �˽��н�����
			 */
			while(true) {
				System.out.println("�ȴ��ͻ�������...");
				Socket socket = server.accept();
				System.out.println("һ���ͻ���������!");
				//����һ���߳�������ÿͻ���
				ClientHandler handler 
					= new ClientHandler(socket);
				Thread t = new Thread(handler);
				t.start();
			}	
				
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
	/**
	 * ���߳���������ָ����Socket��Ӧ�Ŀͻ���
	 * �������ݽ���
	 * @author ta
	 *
	 */
	private class ClientHandler 
						implements Runnable{
		private Socket socket;
		
		//��¼��ǰ�ͻ��˵ĵ�ַ��Ϣ
		private String host;
		
		public ClientHandler(Socket socket) {
			this.socket = socket;
			/*
			 * ͨ��Socket��ȡԶ�˼������ַ
			 * ��Ϣ(���ڷ���˶��ԣ�Զ�˾���
			 * �ͻ�����)
			 */
			InetAddress address = socket.getInetAddress();
			host = address.getHostAddress();
		}
		
		public void run() {
			System.out.println("������һ���̴߳���ͻ�����Ϣ!");
			PrintWriter pw = null;
			try {
				/*
				 * ͨ��Socket��ȡ����������ȡ�ͻ��˷���
				 * ����������
				 */
				InputStream in 
					= socket.getInputStream();
				InputStreamReader isr
					= new InputStreamReader(in,"UTF-8");
				BufferedReader br
					= new BufferedReader(isr);
				/*
				 * ͨ��Socket��ȡ����������ÿͻ���
				 * ������Ϣ
				 */
				OutputStream out 
					= socket.getOutputStream();
				OutputStreamWriter osw
					= new OutputStreamWriter(out,"UTF-8");
				BufferedWriter bw
					= new BufferedWriter(osw);
				
				pw	= new PrintWriter(bw,true);
				/*
				 * ����ǰClientHandler��Ӧ�ͻ��˵�
				 * ��������뵽allOut�����С��Ա���
				 * ��ClientHandler�ڽ�����Ϣ�����
				 * ����Ϣ���͸���ǰ�ͻ��ˡ�
				 */
				synchronized (allOut) {
//					//1�ȶ�allOut��������
//					allOut = Arrays.copyOf(allOut, allOut.length+1);
//					//2����ǰ��pw���뵽�������һ��λ����
//					allOut[allOut.length-1] = pw;
					
					allOut.add(pw);
				}
				
				String message = null;
				while((message = br.readLine())!=null) {
					System.out.println(host+"˵:"+message);
					//����Ϣ���͸���ǰ�ͻ���
//					pw.println("�ͻ���˵:"+message);
					/*
					 * ����allOut����Ҫ�������̶߳Ը�����
					 * ����ɾ���⡣
					 */
					synchronized (allOut) {
						//����allOut������Ϣ���͸����пͻ���
						for(PrintWriter o : allOut) {
							o.println(host+"˵:"+message);
						}
					}
					
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				//����ͻ��˶Ͽ����Ӻ�Ĳ���
				synchronized (allOut) {
					//����ǰ�ͻ��˵������pw��allOut������ɾ��
//					for(int i=0;i<allOut.length;i++) {
//						if(allOut[i]==pw) {
//							//�����һ��Ԫ�ط�������
//							allOut[i] = allOut[allOut.length-1];
//							//����
//							allOut = Arrays.copyOf(allOut, allOut.length-1);
//							break;
//						}
//					}
					allOut.remove(pw);
				}
				
				
				//��socket�ر�
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}








