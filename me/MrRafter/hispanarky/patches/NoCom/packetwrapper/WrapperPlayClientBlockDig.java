package me.MrRafter.hispanarky.patches.NoCom.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers.Direction;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerDigType;

public class WrapperPlayClientBlockDig extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Client.BLOCK_DIG;
	
	public WrapperPlayClientBlockDig() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}
	
	public WrapperPlayClientBlockDig(PacketContainer packet) {
		super(packet, TYPE);
	}
	
	/**
	 * Retrieve Location.
	 * <p>
	 * Notes: block position
	 *
	 * @return The current Location
	 */
	public BlockPosition getLocation() {
		return handle.getBlockPositionModifier().read(0);
	}
	
	/**
	 * Set Location.
	 *
	 * @param value - new value.
	 */
	public void setLocation(BlockPosition value) {
		handle.getBlockPositionModifier().write(0, value);
	}
	
	public Direction getDirection() {
		return handle.getDirections().read(0);
	}
	
	public void setDirection(Direction value) {
		handle.getDirections().write(0, value);
	}
	
	/**
	 * Retrieve Status.
	 * <p>
	 * Notes: the action the player is taking against the block (see below)
	 *
	 * @return The current Status
	 */
	public PlayerDigType getStatus() {
		return handle.getPlayerDigTypes().read(0);
	}
	
	/**
	 * Set Status.
	 *
	 * @param value - new value.
	 */
	public void setStatus(PlayerDigType value) {
		handle.getPlayerDigTypes().write(0, value);
	}
}