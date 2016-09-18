import java.net.* ;
import java.io.* ;
public class EchoThread implements Runnable{
	private Socket client = null ;
	public EchoThread(Socket client){
		this.client = client ;
	}
	public void run(){
		BufferedReader buf = null ;	// ����������
		PrintStream out = null ;		// ��ӡ��������
		try{
			out = new PrintStream(client.getOutputStream()) ;
			// ׼�����տͻ��˵�������Ϣ
			buf = new BufferedReader(new InputStreamReader(client.getInputStream())) ;
			boolean flag = true ;	// ��־λ����ʾ����һֱ���ղ���Ӧ��Ϣ
			while(flag){
				String str = buf.readLine() ;		// ���տͻ��˷��͵�����
				if(str==null||"".equals(str)){	// ��ʾû������
					flag = false ;	// �˳�ѭ��
				}else{
					if("bye".equals(str)){	// ������������Ϊbye��ʾ����
						flag = false ;
					}else{
						//out.println("ECHO : " + str) ;	// ��Ӧ��Ϣ
						StringBuffer sb=new StringBuffer(str);
						out.println(sb.reverse());
						
					}
				}
			}
			client.close() ;
		}catch(Exception e){}
		
	}
};