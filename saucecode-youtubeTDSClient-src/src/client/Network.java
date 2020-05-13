package client;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class Network extends Listener {

	Client client;

	void connect(){
		client = new Client();
		client.getKryo().register(PacketUpdateX.class);
		client.getKryo().register(PacketUpdateY.class);
		client.getKryo().register(PacketAddPlayer.class);
		client.getKryo().register(PacketRemovePlayer.class);
		client.addListener(this);
		
		client.start();
		try {
			int port = 27960;
			String ip = "localhost";
			client.connect(5000, ip, port, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void received(Connection c, Object o){
		if(o instanceof PacketAddPlayer){
			PacketAddPlayer packet = (PacketAddPlayer) o;
			MPPlayer newPlayer = new MPPlayer();
			PlayerStorage.players.put(packet.id, newPlayer);
			
		}else if(o instanceof PacketRemovePlayer){
			PacketRemovePlayer packet = (PacketRemovePlayer) o;
			PlayerStorage.players.remove(packet.id);
			
		}else if(o instanceof PacketUpdateX){
			PacketUpdateX packet = (PacketUpdateX) o;
			PlayerStorage.players.get(packet.id).setX(packet.x);
			
		}else if(o instanceof PacketUpdateY){
			PacketUpdateY packet = (PacketUpdateY) o;
			PlayerStorage.players.get(packet.id).setY(packet.y);
		}
	}
}
