import java.awt.*;
import javax.swing.*;
import java.lang.Integer;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

//recieves the current positions of all components from the server and updates them on the client JPanel
public class ServerReciever implements Runnable 
{
	Socket connectionSock = null;
	scene game = null;
	boolean noInput = false;
	public ServerReciever(scene game, Socket connectionSock)
	{
		this.game = game;
		this.connectionSock = connectionSock;
	}
	public void run()
	{
		try
		{
			BufferedReader clientInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
			int ballX = 450, ballY = 250; //ball's location
			int lRect = 0, rRect = 0; //the two paddle's locations
			String score = "0 - 0"; //and of course the score
			while(true)
			{
				String clientText = clientInput.readLine();
				if(checkInput(clientText))//checks if clientText is null and if it is noInput is set to true to close connection and the value isn't assigned to the variable
				{
					ballX = Integer.parseInt(clientText);
				}
				
				clientText = clientInput.readLine();
				if(checkInput(clientText))
				{
					ballY = Integer.parseInt(clientText);
				}	
				
				clientText = clientInput.readLine();
				if(checkInput(clientText))
				{
					lRect = Integer.parseInt(clientText);
				}
				
				clientText = clientInput.readLine();
				if(checkInput(clientText))
				{
					rRect = Integer.parseInt(clientText);
				}
				
				clientText = clientInput.readLine();
				if(checkInput(clientText))
				{
					score = clientText;
				}
				
				if(noInput)
				{
					// Connection was lost
				   System.out.println("Closing connection for socket " + connectionSock);
				   connectionSock.close();
				   break;
				}
				game.changeBallPosition(ballX, ballY);
				game.moveLeftPaddle(lRect);
				game.moveRightPaddle(rRect);
				game.modifyScore(score);
			}
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
	}				
	public boolean checkInput(String clientText)
	{
		if(clientText != null)
		{
			return true;
		}
		else
		{
			noInput = true;
			return false;
		}
	}
}	
