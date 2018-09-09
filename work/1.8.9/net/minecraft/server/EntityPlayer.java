package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityPlayer extends EntityHuman implements ICrafting {

    private static final Logger bH = LogManager.getLogger();
    private String locale = "en_US";
    public PlayerConnection playerConnection;
    public final MinecraftServer server;
    public final PlayerInteractManager playerInteractManager;
    public double d;
    public double e;
    public final List<ChunkCoordIntPair> chunkCoordIntPairQueue = Lists.newLinkedList();
    public final List<Integer> removeQueue = Lists.newLinkedList();
    private final ServerStatisticManager bK;
    private float bL = Float.MIN_VALUE;
    private float bM = -1.0E8F;
    private int bN = -99999999;
    private boolean bO = true;
    public int lastSentExp = -99999999;
    public int invulnerableTicks = 60;
    private EntityHuman.EnumChatVisibility bR;
    private boolean bS = true;
    private long bT = System.currentTimeMillis();
    private Entity bU = null;
    private int containerCounter;
    public boolean g;
    public int ping;
    public boolean viewingCredits;

    public EntityPlayer(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager) {
        super(worldserver, gameprofile);
        playerinteractmanager.player = this;
        this.playerInteractManager = playerinteractmanager;
        BlockPosition blockposition = worldserver.getSpawn();

        if (!worldserver.worldProvider.o() && worldserver.getWorldData().getGameType() != WorldSettings.EnumGamemode.ADVENTURE) {
            int i = Math.max(5, minecraftserver.getSpawnProtection() - 6);
            int j = MathHelper.floor(worldserver.getWorldBorder().b((double) blockposition.getX(), (double) blockposition.getZ()));

            if (j < i) {
                i = j;
            }

            if (j <= 1) {
                i = 1;
            }

            blockposition = worldserver.r(blockposition.a(this.random.nextInt(i * 2) - i, 0, this.random.nextInt(i * 2) - i));
        }

        this.server = minecraftserver;
        this.bK = minecraftserver.getPlayerList().a((EntityHuman) this);
        this.S = 0.0F;
        this.setPositionRotation(blockposition, 0.0F, 0.0F);

        while (!worldserver.getCubes(this, this.getBoundingBox()).isEmpty() && this.locY < 255.0D) {
            this.setPosition(this.locX, this.locY + 1.0D, this.locZ);
        }

    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("playerGameType", 99)) {
            if (MinecraftServer.getServer().getForceGamemode()) {
                this.playerInteractManager.setGameMode(MinecraftServer.getServer().getGamemode());
            } else {
                this.playerInteractManager.setGameMode(WorldSettings.EnumGamemode.getById(nbttagcompound.getInt("playerGameType")));
            }
        }

    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("playerGameType", this.playerInteractManager.getGameMode().getId());
    }

    public void levelDown(int i) {
        super.levelDown(i);
        this.lastSentExp = -1;
    }

    public void enchantDone(int i) {
        super.enchantDone(i);
        this.lastSentExp = -1;
    }

    public void syncInventory() {
        this.activeContainer.addSlotListener(this);
    }

    public void enterCombat() {
        super.enterCombat();
        this.playerConnection.sendPacket(new PacketPlayOutCombatEvent(this.bs(), PacketPlayOutCombatEvent.EnumCombatEventType.ENTER_COMBAT));
    }

    public void exitCombat() {
        super.exitCombat();
        this.playerConnection.sendPacket(new PacketPlayOutCombatEvent(this.bs(), PacketPlayOutCombatEvent.EnumCombatEventType.END_COMBAT));
    }

    public void t_() {
        this.playerInteractManager.a();
        --this.invulnerableTicks;
        if (this.noDamageTicks > 0) {
            --this.noDamageTicks;
        }

        this.activeContainer.b();
        if (!this.world.isClientSide && !this.activeContainer.a((EntityHuman) this)) {
            this.closeInventory();
            this.activeContainer = this.defaultContainer;
        }

        while (!this.removeQueue.isEmpty()) {
            int i = Math.min(this.removeQueue.size(), Integer.MAX_VALUE);
            int[] aint = new int[i];
            Iterator iterator = this.removeQueue.iterator();
            int j = 0;

            while (iterator.hasNext() && j < i) {
                aint[j++] = ((Integer) iterator.next()).intValue();
                iterator.remove();
            }

            this.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(aint));
        }

        if (!this.chunkCoordIntPairQueue.isEmpty()) {
            ArrayList arraylist = Lists.newArrayList();
            Iterator iterator1 = this.chunkCoordIntPairQueue.iterator();
            ArrayList arraylist1 = Lists.newArrayList();

            Chunk chunk;

            while (iterator1.hasNext() && arraylist.size() < 10) {
                ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) iterator1.next();

                if (chunkcoordintpair != null) {
                    if (this.world.isLoaded(new BlockPosition(chunkcoordintpair.x << 4, 0, chunkcoordintpair.z << 4))) {
                        chunk = this.world.getChunkAt(chunkcoordintpair.x, chunkcoordintpair.z);
                        if (chunk.isReady()) {
                            arraylist.add(chunk);
                            arraylist1.addAll(((WorldServer) this.world).getTileEntities(chunkcoordintpair.x * 16, 0, chunkcoordintpair.z * 16, chunkcoordintpair.x * 16 + 16, 256, chunkcoordintpair.z * 16 + 16));
                            iterator1.remove();
                        }
                    }
                } else {
                    iterator1.remove();
                }
            }

            if (!arraylist.isEmpty()) {
                if (arraylist.size() == 1) {
                    this.playerConnection.sendPacket(new PacketPlayOutMapChunk((Chunk) arraylist.get(0), true, '\uffff'));
                } else {
                    this.playerConnection.sendPacket(new PacketPlayOutMapChunkBulk(arraylist));
                }

                Iterator iterator2 = arraylist1.iterator();

                while (iterator2.hasNext()) {
                    TileEntity tileentity = (TileEntity) iterator2.next();

                    this.a(tileentity);
                }

                iterator2 = arraylist.iterator();

                while (iterator2.hasNext()) {
                    chunk = (Chunk) iterator2.next();
                    this.u().getTracker().a(this, chunk);
                }
            }
        }

        Entity entity = this.C();

        if (entity != this) {
            if (!entity.isAlive()) {
                this.setSpectatorTarget(this);
            } else {
                this.setLocation(entity.locX, entity.locY, entity.locZ, entity.yaw, entity.pitch);
                this.server.getPlayerList().d(this);
                if (this.isSneaking()) {
                    this.setSpectatorTarget(this);
                }
            }
        }

    }

    public void l() {
        try {
            super.t_();

            for (int i = 0; i < this.inventory.getSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);

                if (itemstack != null && itemstack.getItem().f()) {
                    Packet packet = ((ItemWorldMapBase) itemstack.getItem()).c(itemstack, this.world, this);

                    if (packet != null) {
                        this.playerConnection.sendPacket(packet);
                    }
                }
            }

            if (this.getHealth() != this.bM || this.bN != this.foodData.getFoodLevel() || this.foodData.getSaturationLevel() == 0.0F != this.bO) {
                this.playerConnection.sendPacket(new PacketPlayOutUpdateHealth(this.getHealth(), this.foodData.getFoodLevel(), this.foodData.getSaturationLevel()));
                this.bM = this.getHealth();
                this.bN = this.foodData.getFoodLevel();
                this.bO = this.foodData.getSaturationLevel() == 0.0F;
            }

            if (this.getHealth() + this.getAbsorptionHearts() != this.bL) {
                this.bL = this.getHealth() + this.getAbsorptionHearts();
                Collection collection = this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.g);
                Iterator iterator = collection.iterator();

                while (iterator.hasNext()) {
                    ScoreboardObjective scoreboardobjective = (ScoreboardObjective) iterator.next();

                    this.getScoreboard().getPlayerScoreForObjective(this.getName(), scoreboardobjective).updateForList(Arrays.asList(new EntityHuman[] { this}));
                }
            }

            if (this.expTotal != this.lastSentExp) {
                this.lastSentExp = this.expTotal;
                this.playerConnection.sendPacket(new PacketPlayOutExperience(this.exp, this.expTotal, this.expLevel));
            }

            if (this.ticksLived % 20 * 5 == 0 && !this.getStatisticManager().hasAchievement(AchievementList.L)) {
                this.i_();
            }

        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Ticking player");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Player being ticked");

            this.appendEntityCrashDetails(crashreportsystemdetails);
            throw new ReportedException(crashreport);
        }
    }

    protected void i_() {
        BiomeBase biomebase = this.world.getBiome(new BlockPosition(MathHelper.floor(this.locX), 0, MathHelper.floor(this.locZ)));
        String s = biomebase.ah;
        AchievementSet achievementset = (AchievementSet) this.getStatisticManager().b((Statistic) AchievementList.L);

        if (achievementset == null) {
            achievementset = (AchievementSet) this.getStatisticManager().a(AchievementList.L, new AchievementSet());
        }

        achievementset.add(s);
        if (this.getStatisticManager().b(AchievementList.L) && achievementset.size() >= BiomeBase.n.size()) {
            HashSet hashset = Sets.newHashSet(BiomeBase.n);
            Iterator iterator = achievementset.iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();
                Iterator iterator1 = hashset.iterator();

                while (iterator1.hasNext()) {
                    BiomeBase biomebase1 = (BiomeBase) iterator1.next();

                    if (biomebase1.ah.equals(s1)) {
                        iterator1.remove();
                    }
                }

                if (hashset.isEmpty()) {
                    break;
                }
            }

            if (hashset.isEmpty()) {
                this.b((Statistic) AchievementList.L);
            }
        }

    }

    public void die(DamageSource damagesource) {
        if (this.world.getGameRules().getBoolean("showDeathMessages")) {
            ScoreboardTeamBase scoreboardteambase = this.getScoreboardTeam();

            if (scoreboardteambase != null && scoreboardteambase.j() != ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS) {
                if (scoreboardteambase.j() == ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OTHER_TEAMS) {
                    this.server.getPlayerList().a((EntityHuman) this, this.bs().b());
                } else if (scoreboardteambase.j() == ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OWN_TEAM) {
                    this.server.getPlayerList().b((EntityHuman) this, this.bs().b());
                }
            } else {
                this.server.getPlayerList().sendMessage(this.bs().b());
            }
        }

        if (!this.world.getGameRules().getBoolean("keepInventory")) {
            this.inventory.n();
        }

        Collection collection = this.world.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.d);
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreboardObjective scoreboardobjective = (ScoreboardObjective) iterator.next();
            ScoreboardScore scoreboardscore = this.getScoreboard().getPlayerScoreForObjective(this.getName(), scoreboardobjective);

            scoreboardscore.incrementScore();
        }

        EntityLiving entityliving = this.bt();

        if (entityliving != null) {
            EntityTypes.MonsterEggInfo entitytypes_monsteregginfo = (EntityTypes.MonsterEggInfo) EntityTypes.eggInfo.get(Integer.valueOf(EntityTypes.a(entityliving)));

            if (entitytypes_monsteregginfo != null) {
                this.b(entitytypes_monsteregginfo.e);
            }

            entityliving.b(this, this.aW);
        }

        this.b(StatisticList.y);
        this.a(StatisticList.h);
        this.bs().g();
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            boolean flag = this.server.ae() && this.cr() && "fall".equals(damagesource.translationIndex);

            if (!flag && this.invulnerableTicks > 0 && damagesource != DamageSource.OUT_OF_WORLD) {
                return false;
            } else {
                if (damagesource instanceof EntityDamageSource) {
                    Entity entity = damagesource.getEntity();

                    if (entity instanceof EntityHuman && !this.a((EntityHuman) entity)) {
                        return false;
                    }

                    if (entity instanceof EntityArrow) {
                        EntityArrow entityarrow = (EntityArrow) entity;

                        if (entityarrow.shooter instanceof EntityHuman && !this.a((EntityHuman) entityarrow.shooter)) {
                            return false;
                        }
                    }
                }

                return super.damageEntity(damagesource, f);
            }
        }
    }

    public boolean a(EntityHuman entityhuman) {
        return !this.cr() ? false : super.a(entityhuman);
    }

    private boolean cr() {
        return this.server.getPVP();
    }

    public void c(int i) {
        if (this.dimension == 1 && i == 1) {
            this.b((Statistic) AchievementList.D);
            this.world.kill(this);
            this.viewingCredits = true;
            this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(4, 0.0F));
        } else {
            if (this.dimension == 0 && i == 1) {
                this.b((Statistic) AchievementList.C);
                BlockPosition blockposition = this.server.getWorldServer(i).getDimensionSpawn();

                if (blockposition != null) {
                    this.playerConnection.a((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), 0.0F, 0.0F);
                }

                i = 1;
            } else {
                this.b((Statistic) AchievementList.y);
            }

            this.server.getPlayerList().changeDimension(this, i);
            this.lastSentExp = -1;
            this.bM = -1.0F;
            this.bN = -1;
        }

    }

    public boolean a(EntityPlayer entityplayer) {
        return entityplayer.isSpectator() ? this.C() == this : (this.isSpectator() ? false : super.a(entityplayer));
    }

    private void a(TileEntity tileentity) {
        if (tileentity != null) {
            Packet packet = tileentity.getUpdatePacket();

            if (packet != null) {
                this.playerConnection.sendPacket(packet);
            }
        }

    }

    public void receive(Entity entity, int i) {
        super.receive(entity, i);
        this.activeContainer.b();
    }

    public EntityHuman.EnumBedResult a(BlockPosition blockposition) {
        EntityHuman.EnumBedResult entityhuman_enumbedresult = super.a(blockposition);

        if (entityhuman_enumbedresult == EntityHuman.EnumBedResult.OK) {
            PacketPlayOutBed packetplayoutbed = new PacketPlayOutBed(this, blockposition);

            this.u().getTracker().a((Entity) this, (Packet) packetplayoutbed);
            this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
            this.playerConnection.sendPacket(packetplayoutbed);
        }

        return entityhuman_enumbedresult;
    }

    public void a(boolean flag, boolean flag1, boolean flag2) {
        if (this.isSleeping()) {
            this.u().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(this, 2));
        }

        super.a(flag, flag1, flag2);
        if (this.playerConnection != null) {
            this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        }

    }

    public void mount(Entity entity) {
        Entity entity1 = this.vehicle;

        super.mount(entity);
        if (entity != entity1) {
            this.playerConnection.sendPacket(new PacketPlayOutAttachEntity(0, this, this.vehicle));
            this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        }

    }

    protected void a(double d0, boolean flag, Block block, BlockPosition blockposition) {}

    public void a(double d0, boolean flag) {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.locY - 0.20000000298023224D);
        int k = MathHelper.floor(this.locZ);
        BlockPosition blockposition = new BlockPosition(i, j, k);
        Block block = this.world.getType(blockposition).getBlock();

        if (block.getMaterial() == Material.AIR) {
            Block block1 = this.world.getType(blockposition.down()).getBlock();

            if (block1 instanceof BlockFence || block1 instanceof BlockCobbleWall || block1 instanceof BlockFenceGate) {
                blockposition = blockposition.down();
                block = this.world.getType(blockposition).getBlock();
            }
        }

        super.a(d0, flag, block, blockposition);
    }

    public void openSign(TileEntitySign tileentitysign) {
        tileentitysign.a((EntityHuman) this);
        this.playerConnection.sendPacket(new PacketPlayOutOpenSignEditor(tileentitysign.getPosition()));
    }

    private void nextContainerCounter() {
        this.containerCounter = this.containerCounter % 100 + 1;
    }

    public void openTileEntity(ITileEntityContainer itileentitycontainer) {
        this.nextContainerCounter();
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, itileentitycontainer.getContainerName(), itileentitycontainer.getScoreboardDisplayName()));
        this.activeContainer = itileentitycontainer.createContainer(this.inventory, this);
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openContainer(IInventory iinventory) {
        if (this.activeContainer != this.defaultContainer) {
            this.closeInventory();
        }

        if (iinventory instanceof ITileInventory) {
            ITileInventory itileinventory = (ITileInventory) iinventory;

            if (itileinventory.r_() && !this.a(itileinventory.i()) && !this.isSpectator()) {
                this.playerConnection.sendPacket(new PacketPlayOutChat(new ChatMessage("container.isLocked", new Object[] { iinventory.getScoreboardDisplayName()}), (byte) 2));
                this.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect("random.door_close", this.locX, this.locY, this.locZ, 1.0F, 1.0F));
                return;
            }
        }

        this.nextContainerCounter();
        if (iinventory instanceof ITileEntityContainer) {
            this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, ((ITileEntityContainer) iinventory).getContainerName(), iinventory.getScoreboardDisplayName(), iinventory.getSize()));
            this.activeContainer = ((ITileEntityContainer) iinventory).createContainer(this.inventory, this);
        } else {
            this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, "minecraft:container", iinventory.getScoreboardDisplayName(), iinventory.getSize()));
            this.activeContainer = new ContainerChest(this.inventory, iinventory, this);
        }

        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openTrade(IMerchant imerchant) {
        this.nextContainerCounter();
        this.activeContainer = new ContainerMerchant(this.inventory, imerchant, this.world);
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
        InventoryMerchant inventorymerchant = ((ContainerMerchant) this.activeContainer).e();
        IChatBaseComponent ichatbasecomponent = imerchant.getScoreboardDisplayName();

        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, "minecraft:villager", ichatbasecomponent, inventorymerchant.getSize()));
        MerchantRecipeList merchantrecipelist = imerchant.getOffers(this);

        if (merchantrecipelist != null) {
            PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.buffer());

            packetdataserializer.writeInt(this.containerCounter);
            merchantrecipelist.a(packetdataserializer);
            this.playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|TrList", packetdataserializer));
        }

    }

    public void openHorseInventory(EntityHorse entityhorse, IInventory iinventory) {
        if (this.activeContainer != this.defaultContainer) {
            this.closeInventory();
        }

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, "EntityHorse", iinventory.getScoreboardDisplayName(), iinventory.getSize(), entityhorse.getId()));
        this.activeContainer = new ContainerHorse(this.inventory, iinventory, entityhorse, this);
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openBook(ItemStack itemstack) {
        Item item = itemstack.getItem();

        if (item == Items.WRITTEN_BOOK) {
            this.playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(Unpooled.buffer())));
        }

    }

    public void a(Container container, int i, ItemStack itemstack) {
        if (!(container.getSlot(i) instanceof SlotResult)) {
            if (!this.g) {
                this.playerConnection.sendPacket(new PacketPlayOutSetSlot(container.windowId, i, itemstack));
            }
        }
    }

    public void updateInventory(Container container) {
        this.a(container, container.a());
    }

    public void a(Container container, List<ItemStack> list) {
        this.playerConnection.sendPacket(new PacketPlayOutWindowItems(container.windowId, list));
        this.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.inventory.getCarried()));
    }

    public void setContainerData(Container container, int i, int j) {
        this.playerConnection.sendPacket(new PacketPlayOutWindowData(container.windowId, i, j));
    }

    public void setContainerData(Container container, IInventory iinventory) {
        for (int i = 0; i < iinventory.g(); ++i) {
            this.playerConnection.sendPacket(new PacketPlayOutWindowData(container.windowId, i, iinventory.getProperty(i)));
        }

    }

    public void closeInventory() {
        this.playerConnection.sendPacket(new PacketPlayOutCloseWindow(this.activeContainer.windowId));
        this.p();
    }

    public void broadcastCarriedItem() {
        if (!this.g) {
            this.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.inventory.getCarried()));
        }
    }

    public void p() {
        this.activeContainer.b((EntityHuman) this);
        this.activeContainer = this.defaultContainer;
    }

    public void a(float f, float f1, boolean flag, boolean flag1) {
        if (this.vehicle != null) {
            if (f >= -1.0F && f <= 1.0F) {
                this.aZ = f;
            }

            if (f1 >= -1.0F && f1 <= 1.0F) {
                this.ba = f1;
            }

            this.aY = flag;
            this.setSneaking(flag1);
        }

    }

    public void a(Statistic statistic, int i) {
        if (statistic != null) {
            this.bK.b(this, statistic, i);
            Iterator iterator = this.getScoreboard().getObjectivesForCriteria(statistic.k()).iterator();

            while (iterator.hasNext()) {
                ScoreboardObjective scoreboardobjective = (ScoreboardObjective) iterator.next();

                this.getScoreboard().getPlayerScoreForObjective(this.getName(), scoreboardobjective).addScore(i);
            }

            if (this.bK.e()) {
                this.bK.a(this);
            }

        }
    }

    public void a(Statistic statistic) {
        if (statistic != null) {
            this.bK.setStatistic(this, statistic, 0);
            Iterator iterator = this.getScoreboard().getObjectivesForCriteria(statistic.k()).iterator();

            while (iterator.hasNext()) {
                ScoreboardObjective scoreboardobjective = (ScoreboardObjective) iterator.next();

                this.getScoreboard().getPlayerScoreForObjective(this.getName(), scoreboardobjective).setScore(0);
            }

            if (this.bK.e()) {
                this.bK.a(this);
            }

        }
    }

    public void q() {
        if (this.passenger != null) {
            this.passenger.mount(this);
        }

        if (this.sleeping) {
            this.a(true, false, false);
        }

    }

    public void triggerHealthUpdate() {
        this.bM = -1.0E8F;
    }

    public void b(IChatBaseComponent ichatbasecomponent) {
        this.playerConnection.sendPacket(new PacketPlayOutChat(ichatbasecomponent));
    }

    protected void s() {
        this.playerConnection.sendPacket(new PacketPlayOutEntityStatus(this, (byte) 9));
        super.s();
    }

    public void a(ItemStack itemstack, int i) {
        super.a(itemstack, i);
        if (itemstack != null && itemstack.getItem() != null && itemstack.getItem().e(itemstack) == EnumAnimation.EAT) {
            this.u().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(this, 3));
        }

    }

    public void copyTo(EntityHuman entityhuman, boolean flag) {
        super.copyTo(entityhuman, flag);
        this.lastSentExp = -1;
        this.bM = -1.0F;
        this.bN = -1;
        this.removeQueue.addAll(((EntityPlayer) entityhuman).removeQueue);
    }

    protected void a(MobEffect mobeffect) {
        super.a(mobeffect);
        this.playerConnection.sendPacket(new PacketPlayOutEntityEffect(this.getId(), mobeffect));
    }

    protected void a(MobEffect mobeffect, boolean flag) {
        super.a(mobeffect, flag);
        this.playerConnection.sendPacket(new PacketPlayOutEntityEffect(this.getId(), mobeffect));
    }

    protected void b(MobEffect mobeffect) {
        super.b(mobeffect);
        this.playerConnection.sendPacket(new PacketPlayOutRemoveEntityEffect(this.getId(), mobeffect));
    }

    public void enderTeleportTo(double d0, double d1, double d2) {
        this.playerConnection.a(d0, d1, d2, this.yaw, this.pitch);
    }

    public void b(Entity entity) {
        this.u().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(entity, 4));
    }

    public void c(Entity entity) {
        this.u().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(entity, 5));
    }

    public void updateAbilities() {
        if (this.playerConnection != null) {
            this.playerConnection.sendPacket(new PacketPlayOutAbilities(this.abilities));
            this.B();
        }
    }

    public WorldServer u() {
        return (WorldServer) this.world;
    }

    public void a(WorldSettings.EnumGamemode worldsettings_enumgamemode) {
        this.playerInteractManager.setGameMode(worldsettings_enumgamemode);
        this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(3, (float) worldsettings_enumgamemode.getId()));
        if (worldsettings_enumgamemode == WorldSettings.EnumGamemode.SPECTATOR) {
            this.mount((Entity) null);
        } else {
            this.setSpectatorTarget(this);
        }

        this.updateAbilities();
        this.bP();
    }

    public boolean isSpectator() {
        return this.playerInteractManager.getGameMode() == WorldSettings.EnumGamemode.SPECTATOR;
    }

    public void sendMessage(IChatBaseComponent ichatbasecomponent) {
        this.playerConnection.sendPacket(new PacketPlayOutChat(ichatbasecomponent));
    }

    public boolean a(int i, String s) {
        if ("seed".equals(s) && !this.server.ae()) {
            return true;
        } else if (!"tell".equals(s) && !"help".equals(s) && !"me".equals(s) && !"trigger".equals(s)) {
            if (this.server.getPlayerList().isOp(this.getProfile())) {
                OpListEntry oplistentry = (OpListEntry) this.server.getPlayerList().getOPs().get(this.getProfile());

                return oplistentry != null ? oplistentry.a() >= i : this.server.p() >= i;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public String w() {
        String s = this.playerConnection.networkManager.getSocketAddress().toString();

        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(0, s.indexOf(":"));
        return s;
    }

    public void a(PacketPlayInSettings packetplayinsettings) {
        this.locale = packetplayinsettings.a();
        this.bR = packetplayinsettings.c();
        this.bS = packetplayinsettings.d();
        this.getDataWatcher().watch(10, Byte.valueOf((byte) packetplayinsettings.e()));
    }

    public EntityHuman.EnumChatVisibility getChatFlags() {
        return this.bR;
    }

    public void setResourcePack(String s, String s1) {
        this.playerConnection.sendPacket(new PacketPlayOutResourcePackSend(s, s1));
    }

    public BlockPosition getChunkCoordinates() {
        return new BlockPosition(this.locX, this.locY + 0.5D, this.locZ);
    }

    public void resetIdleTimer() {
        this.bT = MinecraftServer.az();
    }

    public ServerStatisticManager getStatisticManager() {
        return this.bK;
    }

    public void d(Entity entity) {
        if (entity instanceof EntityHuman) {
            this.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[] { entity.getId()}));
        } else {
            this.removeQueue.add(Integer.valueOf(entity.getId()));
        }

    }

    protected void B() {
        if (this.isSpectator()) {
            this.bj();
            this.setInvisible(true);
        } else {
            super.B();
        }

        this.u().getTracker().a(this);
    }

    public Entity C() {
        return (Entity) (this.bU == null ? this : this.bU);
    }

    public void setSpectatorTarget(Entity entity) {
        Entity entity1 = this.C();

        this.bU = (Entity) (entity == null ? this : entity);
        if (entity1 != this.bU) {
            this.playerConnection.sendPacket(new PacketPlayOutCamera(this.bU));
            this.enderTeleportTo(this.bU.locX, this.bU.locY, this.bU.locZ);
        }

    }

    public void attack(Entity entity) {
        if (this.playerInteractManager.getGameMode() == WorldSettings.EnumGamemode.SPECTATOR) {
            this.setSpectatorTarget(entity);
        } else {
            super.attack(entity);
        }

    }

    public long D() {
        return this.bT;
    }

    public IChatBaseComponent getPlayerListName() {
        return null;
    }
}
