package com.icrazyblaze.twitchmod;

import com.icrazyblaze.twitchmod.gui.MessageboxGui;
import com.icrazyblaze.twitchmod.network.GuiMessage;
import com.icrazyblaze.twitchmod.network.PacketHandler;
import com.icrazyblaze.twitchmod.util.TickHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * This class contains every method used by commands registered in the ChatPicker class.
 * @see com.icrazyblaze.twitchmod.chat.ChatPicker
 */
public class BotCommands {

    public static final Block[] oresArray = {Blocks.DIAMOND_ORE, Blocks.REDSTONE_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.LAPIS_ORE, Blocks.EMERALD_ORE, Blocks.COAL_ORE};
    public static final ResourceLocation[] lootArray = {LootTableList.CHESTS_SIMPLE_DUNGEON, LootTableList.CHESTS_ABANDONED_MINESHAFT, LootTableList.CHESTS_SPAWN_BONUS_CHEST};
    public static String username = null;

    public static boolean oresExplode = false;
    public static boolean placeBedrock = false;

    public static List<Block> oresList = Arrays.asList(oresArray);
    public static List<ResourceLocation> lootlist = Arrays.asList(lootArray);

    public static ArrayList<String> messagesList = new ArrayList<>();


    public static EntityPlayerMP player() {

        PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
        EntityPlayerMP player = playerList.getPlayerByUsername(username);

        if (player == null) {
            player = playerList.getPlayers().get(0);
        }

        return player;

    }


    public static void addSlowness() {
        player().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 5));
    }

    public static void addHunger() {
        player().addPotionEffect(new PotionEffect(MobEffects.HUNGER, 800, 255));
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

    public static void addWeakness() {
        player().addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 400, 1));
    }

    public static void addFatigue() {
        player().addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 400, 0));
    }

    public static void addLevitation() {
        player().addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 200, 1));
    }

    public static void noFall() {
        player().addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 400, 255));
    }

    public static void addRegen() {

        player().addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 400, 1));
        player().addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));

    }

    public static void addJumpBoost() {
        player().addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 400, 2));
    }

    public static void setOnFire() {

        BlockPos bpos = player().getPosition();
        IBlockState bposState = player().world.getBlockState(bpos);

        if (bposState == Blocks.AIR.getDefaultState()) {
            player().world.setBlockState(bpos, Blocks.FIRE.getDefaultState());
        }

        player().setFire(10);

    }

    public static void heavyRain() {

        player().world.getWorldInfo().setRaining(true);
        player().world.getWorldInfo().setThundering(true);

    }

    public static void setDifficulty(EnumDifficulty difficulty) {

        player().getServer().setDifficultyForAllWorlds(difficulty);

    }

    public static void setTime(long time) {

        for (int i = 0; i < player().server.worlds.length; ++i) {

            player().server.worlds[i].setWorldTime(time);

        }

    }

    public static void drainHealth() {

        // Half the player's health
        float halfhealth = player().getHealth() / 2;

        if (halfhealth == 0) {
            killPlayer();
        } else {
            player().setHealth(halfhealth);
        }

    }

    public static void setSpawn() {

        BlockPos bpos = new BlockPos(player().posX, player().posY, player().posZ);

        player().setSpawnPoint(bpos, true);

    }

    public static void killPlayer() {

        player().onKillCommand();

    }

    public static void deathTimer() {

        TickHandler.timerSeconds = 60;
        TickHandler.timerTicks = 0;
        TickHandler.killTimer = true;

    }


    public static void floorIsLava() {

        BlockPos bpos = new BlockPos(player().posX, player().posY - 1, player().posZ);

        player().world.setBlockState(bpos, Blocks.LAVA.getDefaultState());

    }

    public static void waterBucket() {

        BlockPos bpos = player().getPosition();

        player().world.setBlockState(bpos, Blocks.WATER.getDefaultState());

    }

    public static void spawnAnvil() {

        BlockPos bpos = new BlockPos(player().posX, player().posY + 16, player().posZ);

        player().world.setBlockState(bpos, Blocks.ANVIL.getDefaultState());

    }

    public static void spawnCobweb() {

        player().world.setBlockState(new BlockPos(player().posX, player().posY + 1, player().posZ), Blocks.WEB.getDefaultState());
        player().world.setBlockState(new BlockPos(player().posX, player().posY - 1, player().posZ), Blocks.WEB.getDefaultState());

    }

    public static void spawnMobBehind(Entity ent) {

        Vec3d lookVector = player().getLookVec();

        double dx = player().posX - (lookVector.x * 4);
        double dz = player().posZ - (lookVector.z * 4);

        ent.setPosition(dx, player().posY, dz);

        player().world.spawnEntity(ent);

    }

    public static void spawnMob(Entity ent) {

        Vec3d lookVector = player().getLookVec();

        double dx = player().posX + (lookVector.x * 4);
        double dz = player().posZ + (lookVector.z * 4);

        ent.setPosition(dx, player().posY, dz);

        player().world.spawnEntity(ent);

    }

    public static void creeperScare() {

        player().world.playSound(null, player().posX, player().posY, player().posZ, SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.HOSTILE, 1.0F, 1.0F);

    }

    public static void zombieScare() {

        player().world.playSound(null, player().posX, player().posY, player().posZ, SoundEvents.ENTITY_ZOMBIE_AMBIENT, SoundCategory.HOSTILE, 1.0F, 1.0F);

    }

    public static void skeletonScare() {

        player().world.playSound(null, player().posX, player().posY, player().posZ, SoundEvents.ENTITY_SKELETON_AMBIENT, SoundCategory.HOSTILE, 1.0F, 1.0F);

    }

    public static void witchScare() {

        player().world.playSound(null, player().posX, player().posY, player().posZ, SoundEvents.ENTITY_WITCH_AMBIENT, SoundCategory.HOSTILE, 1.0F, 1.0F);

    }

    public static void spawnFireball() {

        Vec3d lookVector = player().getLookVec();

        double dx = player().posX + (lookVector.x * 2);
        double dz = player().posZ + (lookVector.z * 2);

        Entity ent = new EntityLargeFireball(player().world);
        ent.setPosition(dx, player().posY + player().getEyeHeight(), dz);

        ent.setVelocity(lookVector.x * 2, lookVector.y * 2, lookVector.z * 2);

        player().world.spawnEntity(ent);

    }

    public static void spawnLightning() {

        player().world.spawnEntity(new EntityLightningBolt(player().world, player().posX, player().posY, player().posZ, false));

    }


    public static void breakBlock() {

        int range = 50;
        BlockPos bpos;

        Vec3d lookVector = player().getLookVec();
        Vec3d posVector = new Vec3d(player().posX, player().posY + player().getEyeHeight(), player().posZ);

        RayTraceResult rayTrace = player().world.rayTraceBlocks(posVector, lookVector.scale(range).add(posVector));

        if (rayTrace == null || rayTrace.typeOfHit == RayTraceResult.Type.MISS) {
            return;
        }

        bpos = rayTrace.getBlockPos();

        player().world.destroyBlock(bpos, false);

    }

    public static void monsterEgg() {

        int range = 50;
        BlockPos bpos;

        Vec3d lookVector = player().getLookVec();
        Vec3d posVector = new Vec3d(player().posX, player().posY + player().getEyeHeight(), player().posZ);

        RayTraceResult rayTrace = player().world.rayTraceBlocks(posVector, lookVector.scale(range).add(posVector));

        if (rayTrace == null || rayTrace.typeOfHit == RayTraceResult.Type.MISS) {
            return;
        }

        bpos = rayTrace.getBlockPos();

        IBlockState thisBlock = player().world.getBlockState(bpos);

        if (thisBlock.getBlock() == Blocks.STONE) {
            player().world.setBlockState(bpos, Blocks.MONSTER_EGG.getDefaultState());
        } else if (thisBlock.getBlock() == Blocks.STONEBRICK) {
            player().world.setBlockState(bpos, Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONEBRICK));
        } else if (thisBlock == Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY)) {
            player().world.setBlockState(bpos, Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.MOSSY_STONEBRICK));
        } else if (thisBlock == Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED)) {
            player().world.setBlockState(bpos, Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.CRACKED_STONEBRICK));
        } else if (thisBlock == Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED)) {
            player().world.setBlockState(bpos, Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.CHISELED_STONEBRICK));
        }

    }

    public static void spawnGlass() {

        double dx = player().posX;
        double dy = player().posY;
        double dz = player().posZ;

        BlockPos[] positions = {new BlockPos(dx, dy + 2, dz), new BlockPos(dx, dy, dz - 1), new BlockPos(dx, dy + 1, dz - 1), new BlockPos(dx, dy, dz + 1), new BlockPos(dx, dy + 1, dz + 1), new BlockPos(dx - 1, dy, dz), new BlockPos(dx - 1, dy + 1, dz), new BlockPos(dx + 1, dy, dz), new BlockPos(dx + 1, dy + 1, dz), new BlockPos(dx, dy - 2, dz)};

        for (BlockPos bpos : positions) {
            player().world.setBlockState(bpos, Blocks.GLASS.getDefaultState());
        }

    }

    public static void dropItem() { // Thanks Amoo!

        ItemStack currentItem = player().inventory.getCurrentItem();

        if (currentItem != ItemStack.EMPTY) {

            player().dropItem(currentItem, false, true);
            player().inventory.deleteStack(currentItem);

        }

    }

    public static void removeRandom() {

        Random rand = new Random();

        // Delete a random item
        int r = rand.nextInt(player().inventory.getSizeInventory());

        ItemStack randomItem = player().inventory.getStackInSlot(r);

        if (randomItem != ItemStack.EMPTY) {

            player().inventory.deleteStack(randomItem);

        }
        else {

            removeRandom();

        }

    }

    public static void giveRandom() {

        Random rand = new Random();

        // Give the player a random item
        Item item = null;
        int length = Item.REGISTRY.getKeys().toArray().length;
        int r = 0;

        while (r == 0) {
            r = rand.nextInt(length);
        }

        Object select = Item.REGISTRY.getObjectById(r);

        if (select != null) {

            item = (Item) select;

            ItemStack stack = new ItemStack(item);
            stack.setCount(rand.nextInt(stack.getMaxStackSize()));

            // Remove the random item here to prevent an item being removed and no item being given to the player
            removeRandom();

            player().addItemStackToInventory(stack);

        }

    }

    public static void messWithInventory(String sender) {

        if (!player().inventory.isEmpty()) {

            giveRandom();

            // Show chat message
            player().sendMessage(new TextComponentString(TextFormatting.RED + sender + " giveth, and " + sender + " taketh away."));

        }

    }

    public static void renameItem(String name) {

        Random rand = new Random();

        if (!player().inventory.isEmpty()) {

            String newname = name.substring(7);

            ItemStack currentitem = player().inventory.getCurrentItem();

            if (currentitem != ItemStack.EMPTY) {

                currentitem.setStackDisplayName(newname);

            }
            else {

                // Rename a random item in the player's inventory when the player isn't holding anything
                int r = rand.nextInt(player().inventory.getSizeInventory());
                ItemStack randomItem = player().inventory.getStackInSlot(r);

                if (randomItem != ItemStack.EMPTY && !randomItem.getDisplayName().equals(newname)) {

                    randomItem.setStackDisplayName(newname);

                }
                else {

                    renameItem(name);

                }

            }

        }

    }

    public static void dropAll() {

        player().inventory.dropAllItems();

    }

    public static void dismount() {

        if (player().isRiding()) {
            player().dismountRidingEntity();
        }

    }

    public static void showMessagebox(String message) {

        // Cut off the command
        message = message.substring(11);

        // Then trim the string to the proper length (324 chars max)
        message = message.substring(0, Math.min(message.length(), 324));

        PacketHandler.INSTANCE.sendToServer(new GuiMessage(message));

    }

    /*
    This code is run on the client when the GuiMessage packet is received.
    */
    @SideOnly(Side.CLIENT)
    public static void showMessageBoxClient(String message) {

        Minecraft.getMinecraft().displayGuiScreen(new MessageboxGui(message));

    }

    public static void placeSign(String message) {

        // Cut off the command
        message = message.substring(5);

        // Split every 15 characters
        int maxlength = 15;
        String[] splitMessage = message.split("(?<=\\G.{" + maxlength + "})");


        BlockPos bpos = player().getPosition();

        double xpos = player().posX;
        double ypos = player().posY;
        double zpos = player().posZ;

        BlockPos bposBelow = new BlockPos(xpos, ypos - 1, zpos);

        // UPDATE: Signs are now replaced.

        // Rotate the sign to face the player
        int playerFace = MathHelper.floor((double) ((player().rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
        player().world.setBlockState(bpos, Blocks.STANDING_SIGN.getDefaultState().withProperty(BlockStandingSign.ROTATION, Integer.valueOf(playerFace)), 11);

        // Change the sign's text
        TileEntity tileEntity = player().world.getTileEntity(bpos);

        if (tileEntity instanceof TileEntitySign) {

            TileEntitySign sign = (TileEntitySign) tileEntity;
            sign.signText[0] = new TextComponentString(splitMessage[0]);
            sign.signText[1] = new TextComponentString(splitMessage[1]);
            sign.signText[2] = new TextComponentString(splitMessage[2]);
            sign.signText[3] = new TextComponentString(splitMessage[3]);

        }

        // Add a light source below the sign for reading at night (thanks Gaiet)
        player().world.setBlockState(bposBelow, Blocks.GLOWSTONE.getDefaultState());

    }

    public static void placeChest() {

        BlockPos bpos = player().getPosition();
        Block bposBlock = player().world.getBlockState(bpos).getBlock();

        // Make sure we don't replace any chests
        if (bposBlock != Blocks.CHEST || bposBlock != Blocks.TRAPPED_CHEST) {

            player().world.setBlockState(bpos, Blocks.CHEST.getDefaultState());

            TileEntity tileEntity = player().world.getTileEntity(bpos);

            if (tileEntity instanceof TileEntityChest) {

                Random rand = new Random();

                ((TileEntityChest) tileEntity).setLootTable(lootlist.get(rand.nextInt(lootlist.size())), rand.nextLong());
                ((TileEntityChest) tileEntity).fillWithLoot(null);

            }

        }

    }

    public static void addToMessages(String message) {

        String newmsg = message.substring(11);
        messagesList.add(newmsg);

    }

    public static void chooseRandomMessage() {

        if (!messagesList.isEmpty()) {

            Random rand = new Random();
            int r = rand.nextInt(messagesList.size());
            String message = messagesList.get(r);

            messagesList.remove(r);

            r = rand.nextInt(TextFormatting.values().length);

            player().sendMessage(new TextComponentString(TextFormatting.fromColorIndex(r) + message));

        }

    }


    @SubscribeEvent
    public void explodeOnBreak(BreakEvent event) {

        Block thisBlock = event.getState().getBlock();

        if (!oresList.contains(thisBlock)) {
            return;
        } else if (oresExplode) {

            event.getWorld().createExplosion(null, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), 4, true);
            oresExplode = false;

        }

    }

    @SubscribeEvent
    public void bedrockOnBreak(BreakEvent event) {

        BlockPos bpos = event.getPos();

        if (placeBedrock) {

            event.setCanceled(true);
            event.getWorld().setBlockState(bpos, Blocks.BEDROCK.getDefaultState());
            placeBedrock = false;

        }

    }

}