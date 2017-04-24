/*
This is the server program that acts as the model for the pong game.
*/
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.*;

public class server
{
	private static int leftWins = 0, rightWins = 0;  //integers keeping track of the number of wins by the right player and left player
	private static int ballX = 450, ballY = 250;
	private static double ballVelX = (Math.random()*2) - 1; //ball's x axis velocity
	private static double ballVelY = (Math.random()*2) - 1; //ball's y axis velocity
	private static intHolder lRectY = new intHolder(0);
	private static intHolder rRectY = new intHolder(0);
	private static String score = leftWins + " - " + rightWins;
	
	
	public static void main(String[] args)
	{
		if(ballVelX == 0)
		{
			ballVelX = 0.1;
		}
		if(ballVelY == 0)
		{
			ballVelY = 0.1;
		}
		//boolean updateBall = true;
		// sets ball velocity to intended values
		try
		{
			/* first the server waits for connections from two clients and then stops listening 
				the first connection is assigned the left player and the the second is assigned as the right*/
			System.out.println("Waiting for client connections on port 5555.");
			ServerSocket serverSock = new ServerSocket(5555);
			Socket connectionOne = serverSock.accept();
			Socket connectionTwo = serverSock.accept();
			DataOutputStream clientOutput1 = new DataOutputStream(connectionOne.getOutputStream());
			DataOutputStream clientOutput2 = new DataOutputStream(connectionTwo.getOutputStream());
			
			clientReciever left = new clientReciever(connectionOne, lRectY);
			clientReciever right = new clientReciever(connectionTwo, rRectY);
			Thread lRecieve = new Thread(left);
			Thread rRecieve = new Thread(right);
			
			clientOutput1.writeBytes("start\n"); //tells client1 to start sending data
			clientOutput2.writeBytes("start\n"); //tells client2 to start sending data
			
			BufferedReader clientInput1 = new BufferedReader(new InputStreamReader(connectionOne.getInputStream()));
			BufferedReader clientInput2 = new BufferedReader(new InputStreamReader(connectionTwo.getInputStream()));
			
			clientInput1.readLine(); //make sure client1 is ready to receive data
			lRecieve.start();
			clientInput2.readLine(); //make sure client2 is ready to receive data
			rRecieve.start();
			
			boolean newGame = false; //variable to check if a score has been made on an iteration
			while(true)
			{
				if(ballX == 0)
				{
					if(ballY < lRectY.getValue() || ballY > lRectY.getValue() + 150)
					{
						rightWins++;
						resetValues();
						newGame = true;
					}
					else
					{
						ballVelX = -ballVelX;
					}
				}
				else if(ballX == 876)
				{
					if(ballY < rRectY.getValue() || ballY > rRectY.getValue() + 150)
					{
						leftWins++;
						resetValues();
						newGame = true;
					}
					else
					{
						ballVelX = -ballVelX;
					}
				}
				
				if(ballY == 0 || ballY == 500)
				{
					ballVelY = -ballVelY;
				}
				
				if(newGame)
				{
					newGame = false;
				}
				else //if(updateBall)
				{
					if(ballVelX >= 1)
					{
						ballX++;
						ballVelX = 0.1;
					}
					else if(ballVelX <= -1)
					{
						ballX--;
						ballVelX = -0.1;
					}
					else if(ballVelX > 0)
					{
						ballVelX += 0.1;
					}
					else if(ballVelX < 0)
					{
						ballVelX -= 0.1;
					}
					
					if(ballVelY >= 1)
					{
						ballY++;
						ballVelY = 0.1;
					}
					else if(ballVelY <= -1)
					{
						ballY--;
						ballVelY = -0.1;
					}
					else if(ballVelY > 0)
					{
						ballVelY += 0.1;
					}
					else if(ballVelY < 0)
					{
						ballVelY -= 0.1;
					}
				}
				if(connectionOne == null || connectionTwo == null) //exit condition
				{
					break;
				}
				
				if(lRectY.getValue() < 0)
				{
					lRectY.setValue(0);
				}
				else if(lRectY.getValue() > 350)
				{
					lRectY.setValue(350);
				}

				if(rRectY.getValue() < 0)
				{
					rRectY.setValue(0);
				}
				else if(rRectY.getValue() > 350)
				{
					rRectY.setValue(350);
				}

				//broadcast variables to client in order:
				//ballX, ballY, lRectY, lRectR, score
				clientOutput1.writeBytes(ballX + "\n");
				clientOutput2.writeBytes(ballX + "\n");
				clientOutput1.writeBytes(ballY + "\n");
				clientOutput2.writeBytes(ballY + "\n");
				clientOutput1.writeBytes(lRectY.getValue() + "\n");
				clientOutput2.writeBytes(lRectY.getValue() + "\n");
				clientOutput1.writeBytes(rRectY.getValue() + "\n");
				clientOutput2.writeBytes(rRectY.getValue() + "\n");
				clientOutput1.writeBytes(score + "\n");
				clientOutput2.writeBytes(score + "\n");
			}	
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
	}
	public static void resetValues()
	{
		ballX = 450;
		ballY = 250;
		ballVelX = (Math.random()*2) - 1; //ball's x axis velocity
		ballVelY = (Math.random()*2) - 1; //ball's y axis velocity
		intHolder lRectY = new intHolder(0);
		intHolder rRectY = new intHolder(0);
		score = leftWins + " - " + rightWins;
		if(ballVelX == 0)
		{
			ballVelX = 0.1;
		}
		if(ballVelY == 0)
		{
			ballVelY = 0.1;
		}	
	}	
}