import java.io.BufferedReader; 
import java.io.BufferedWriter; 
import java.io.DataInputStream; 
import java.io.InputStreamReader; 
import java.io.DataOutputStream; 
//import java.io.OutputStreamWriter; 
//import java.net.InetAddress; 
import java.net.Socket; 
import java.io.File; 
import java.io.FileOutputStream; 
import java.util.Scanner; 
class client 
{ 
public static void main(String args[])throws Exception 
{ 
String address=""; 
Scanner sc= new Scanner(System.in); 
System.out.println("eneter server address"); 
address=sc.nextLine(); 
Socket s=new Socket(address,5000); 
DataInputStream din=new DataInputStream(s.getInputStream()); 
DataOutputStream dout=new DataOutputStream(s.getOutputStream()); 
BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); 
System.out.println("snd get to start"); 
String str="",filename=""; 
try 
{ 
while(!str.equals("start")) 
str=br.readLine(); 
dout.writeUTF(str); 
dout.flush(); 
filename=din.readUTF(); 
System.out.println("receiveing file:"+filename); 
filename="client"+filename; 
System.out.println("saving as file"+filename); 
long sz=Long.parseLong(din.readUTF()); 
System.out.println("filesize:"+(sz/(1024))+"KB"); 
byte b[]=new byte[1024]; 
System.out.println("receiving file"); 
FileOutputStream fos=new FileOutputStream(new File(filename),true); 
long bytesRead; 
do 
{ 
bytesRead=din.read(b,0,b.length); 
fos.write(b,0,b.length); 
} 
while(!(bytesRead<1024)); 
System.out.println("completed"); 
fos.close(); 
dout.close(); 
s.close(); 
} 
catch(Exception e) 
{ 
//do nothing; 
} 
} 
} 
Server side: 
import java.io.InputStreamReader; 
import java.io.DataInputStream; 
import java.io.DataOutputStream; 
import java.net.Socket; 
import java.io.File; 
import java.io.FileInputStream; 
import java.net.ServerSocket; 
import java.util.Scanner; 
class Server 
{ 
public static void main(String args[])throws Exception 
{ 
String filename; 
System.out.println("eneter filename"); 
Scanner sc= new Scanner(System.in); 
filename=sc.nextLine(); 
sc.close(); 
while(true) 
{ 
ServerSocket ss =new ServerSocket(5000); 
System.out.println("wait fr req"); 
Socket s=ss.accept(); 
System.out.println("connected with" +s.getInetAddress().toString()); 
DataInputStream din=new DataInputStream(s.getInputStream()); 
DataOutputStream dout=new DataOutputStream(s.getOutputStream()); 
try 
{ 
String str=""; 
str=din.readUTF(); 
System.out.println("send get tok "); 
if(!str.equals("stop")) 
{ 
System.out.println("sendinf file:"+filename); 
dout.writeUTF(filename); 
dout.flush(); 
File f=new File(filename); 
FileInputStream fin=new FileInputStream(f); 
long sz=(int)f.length(); 
byte b[]=new byte[1024]; 
int read; 
dout.writeUTF(Long.toString(sz)); 
dout.flush(); 
System.out.println("size:"+sz); 
System.out.println("buffer size:"+ss.getReceiveBufferSize()); 
while((read=fin.read(b))!=-1) 
{ 
dout.write(b,0,read); 
dout.flush(); 
} 
fin.close(); 
System.out.println("ok"); 
dout.flush(); 
} 
dout.writeUTF("stop"); 
System.out.println("sendi complete"); 
dout.flush(); 
} 
catch(Exception e) 
{ 
e.printStackTrace(); 
System.out.println("erroe occuerrd"); 
} 
din.close(); 
s.close(); 
ss.close(); 
} 
} 
}
