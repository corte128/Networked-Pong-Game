/*
This is a client program that acts as the view and controller for the pong game.
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Integer;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Client
{
	//private static int mouseY;
	public static void main(String[] args)
	{
		JFrame window = new JFrame("Pong");
		window.setSize(900, 500);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		scene game = new scene();
		window.add(game);
		try
		{
			String hostname = "localhost"; //This should be set to the ip address of the server
			Socket connectionSock = new Socket(hostname, 5555);
			DataOutputStream out = new DataOutputStream(connectionSock.getOutputStream());
			BufferedReader clientInput = new BufferedReader(new InputStreamReader(connectionSock.getInputStream()));
			
			clientInput.readLine();
			
			ServerReciever sceneUpdater = new ServerReciever(game, connectionSock);
			Thread recieving = new Thread(sceneUpdater);
			recieving.start();
			
			out.writeBytes("start\n"); //this client is ready to receive data
			
			//handler mouseInterface = new handler();
			//game.addMouseListener(mouseInterface);
			//game.addMouseMotionListener(mouseInterface);
			while(true)
			{
				int mouseY = (int)(MouseInfo.getPointerInfo().getLocation().getY()) - (int)(game.getLocationOnScreen().getY());
				out.writeBytes(mouseY + "\n");
				//out.writeBytes(game.getYOffset() + "\n");
			}	
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
	}
	/*
	private static class handler implements MouseListener //MouseMotionListener
	{
		public void mouseClicked(MouseEvent event)
		{
			mouseY = event.getY();
		}
		public void mouseExited(MouseEvent event)
		{
		}
		public void mousePressed(MouseEvent event)
		{
		}
		public void mouseEntered(MouseEvent event)
		{
		}
		public void mouseReleased(MouseEvent event)
		{
		}
		public void mouseDragged(MouseEvent event)
		{
		}
		public void mouseMoved(MouseEvent event)
		{
			mouseY = event.getY();
		}
	}
	*/
}