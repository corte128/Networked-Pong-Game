import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.Integer;

//this class continuously checks for mouse positions sent from given client
//and updates that clients corresponding paddle loctaion
public class clientReciever implements Runnable
{
	private Socket connectionSock = null; //client socket passed to clientReciever instance
	private intHolder paddleY; //variable used to pass paddle y coordinate by reference from server.java
	public clientReciever(Socket connectionSock, intHolder paddleY)
	{
		this.connectionSock = connectionSock;
		this.paddleY = paddleY;
	}
	public void run() //method for Runnable interface
	{
		try
		{
			BufferedReader clientInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
			while(true)
			{
				String clientText = clientInput.readLine();
				if(clientText != null)
				{
					//set value of int in paddleY object to value sent by client
					//this value is then broadcasted in server to both clients
					paddleY.setValue(Integer.parseInt(clientText) - 75); 
				}
				/*
				else
				{
				   // Connection was lost
				   System.out.println("Closing connection for socket " + connectionSock);
				   connectionSock.close();
				   break;
				}	
				*/
			}
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
	}			
}