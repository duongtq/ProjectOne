package client;

import static client.PlayerStorage.players;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.esotericsoftware.kryonet.Listener;

public class ClientProgram2 extends Listener {

	private static Player player = new Player();
	private static Network network = new Network();

	public static void main(String[] args) throws Exception {
		Display.setDisplayMode(new DisplayMode(512,512));
		Display.create();

		//Init GL
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Display.getWidth(), 0, Display.getHeight(), -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		network.connect();

		while(!Display.isCloseRequested()){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();

			update();
			render();

			Display.update();
			Display.sync(60);
		}

		Display.destroy();
		System.exit(0);
	}

	private static void update() {
		player.update();

		//Update position
		if(player.networkPosition.x != player.position.x){
			//Send the player's X value
			PacketUpdateX packet = new PacketUpdateX();
			packet.x = player.position.x;
			network.client.sendUDP(packet);

			player.networkPosition.x = player.position.x;
		}
		if(player.networkPosition.y != player.position.y){
			//Send the player's Y value
			PacketUpdateY packet = new PacketUpdateY();
			packet.y = player.position.y;
			network.client.sendUDP(packet);

			player.networkPosition.y = player.position.y;
		}
	}

	private static void render(){
		//Render player
		GL11.glColor3f(1,1,1);
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2f(player.position.x, player.position.y+8);
		GL11.glVertex2f(player.position.x+8, player.position.y-8);
		GL11.glVertex2f(player.position.x-8, player.position.y-8);
		GL11.glEnd();

		//Render other players
		GL11.glColor3f(1, 1, 0);
		GL11.glBegin(GL11.GL_TRIANGLES);
		for(MPPlayer mpPlayer : players.values()){
			GL11.glVertex2f(mpPlayer.getX(), mpPlayer.getY()+8);
			GL11.glVertex2f(mpPlayer.getX()+8, mpPlayer.getY()-8);
			GL11.glVertex2f(mpPlayer.getX()-8, mpPlayer.getY()-8);
		}
		GL11.glEnd();
	}
}
