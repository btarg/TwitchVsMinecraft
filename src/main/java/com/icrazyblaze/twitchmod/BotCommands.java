package com.icrazyblaze.twitchmod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.icrazyblaze.twitchmod.util.TickHandler;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.server.FMLServerHandler;



public class BotCommands {
	
	public static boolean oresExplode = false;
	
	public static Block[] oresArray = {Blocks.DIAMOND_ORE, Blocks.REDSTONE_ORE, Blocks.LIT_REDSTONE_ORE ,Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.LAPIS_ORE, Blocks.EMERALD_ORE, Blocks.COAL_ORE};
    
	public static List<Block> oresList = Arrays.asList(oresArray);
	
	
	public static List<EntityPlayerMP> getPlayers() {
		PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
		List<EntityPlayerMP> players = playerList.getPlayers();
		
		return players;
	}
	
	public static EntityPlayerMP player() {
		return getPlayers().get(0);
	}
	
	public static void addSlowness() {
		player().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 5));
	}
	
	public static void addHunger() {
		player().addPotionEffect(new PotionEffect(MobEffects.HUNGER, 400, 255));
	}
	
	public static void addSpeed() {
		player().addPotionEffect(new PotionEffect(MobEffects.SPEED, 400, 10));
	}
	
	public static void addPoison() {
		player().addPotionEffect(new PotionEffect(MobEffects.POISON, 400, 0));
	}
	
	public static void addNausea() {
		player().addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 400, 0));
	}
	
	public static void setOnFire() {
        
		player().setFire(10);
	}
	
	public static void killPlayer() {
        
		Main.logger.info(player());
		
		player().setDead();
	
	}
	
	public static void deathTimer() {
		TickHandler.secondsLeft = 60;
		TickHandler.secondsTicks = 0;
		TickHandler.killTimer = true;
	}
	
	
	public static void floorIsLava() {	
		
		double xpos = player().posX;
		double ypos = player().posY;
		double zpos = player().posZ;

		
		BlockPos bpos = new BlockPos(xpos, ypos - 1, zpos);
		
		player().world.setBlockState(bpos, Blocks.LAVA.getDefaultState(), 2);
	}
	
	public static void waterBucket() {
		double xpos = player().posX;
		double ypos = player().posY;
		double zpos = player().posZ;

		
		BlockPos bpos = new BlockPos(xpos, ypos, zpos);
		
		player().world.setBlockState(bpos, Blocks.WATER.getDefaultState(), 2);
	}
	
	public static void spawnAnvil() {	
        
		
		double xpos = player().posX;
		double ypos = player().posY;
		double zpos = player().posZ;

		
		BlockPos bpos = new BlockPos(xpos, ypos + 16, zpos);
		
		player().world.setBlockState(bpos, Blocks.ANVIL.getDefaultState(), 2);
	}
	
	public static void spawnCreeper() {	
		
        Vec3d lookVector = player().getLookVec();
        
        double dx = player().posX - (lookVector.x * 4);
        double dz = player().posZ - (lookVector.z * 4);
		
		Entity ent = new EntityCreeper(player().world);
		ent.setPosition(dx, player().posY, dz);

		player().world.spawnEntity(ent);
	}
	
	public static void spawnFireball() {	
		
        Vec3d lookVector = player().getLookVec();
        
        double dx = player().posX + (lookVector.x * 2);
        double dz = player().posZ + (lookVector.z * 2);
        
		Entity ent = new EntityLargeFireball(player().world);
		ent.setPosition(dx, player().posY + player().getEyeHeight(), dz);
		
		
		ent.addVelocity(lookVector.x * 2, lookVector.y * 2, lookVector.z * 2);
		

		player().world.spawnEntity(ent);
	}
	
	public static void spawnLightning() {	
		
		double xpos = player().posX;
		double ypos = player().posY;
		double zpos = player().posZ;

		player().world.spawnEntity(new EntityLightningBolt(player().world, xpos, ypos, zpos, false));
	}
	
	
	public static void breakBlock() {
		
        int range = 50;
		
        
        Vec3d lookVector = player().getLookVec();
        Vec3d posVector = new Vec3d(player().posX, player().posY + player().getEyeHeight(), player().posZ);
        
        
        RayTraceResult rayTrace = player().world.rayTraceBlocks(posVector, lookVector.scale(range).add(posVector));
        BlockPos bpos = rayTrace.getBlockPos();
        
        
        player().world.destroyBlock(bpos, false);
		
	}
	

	
	@SubscribeEvent
	public void explode(BreakEvent event) {
		
		Block thisBlock = event.getState().getBlock();
		
		
		if (!oresList.contains(thisBlock)) {
			return;
		}
		
		else if (oresExplode) {
			event.getWorld().createExplosion(null, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), 5, true);
			oresExplode = false;
		}
		
	}
	
}