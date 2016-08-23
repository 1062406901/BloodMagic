package WayofTime.bloodmagic.entity.mob;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import WayofTime.bloodmagic.block.BlockMimic;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.tile.TileMimic;

public class EntityMimic extends EntityMob
{
    /**
     * Copy of EntitySpider's AI (should be pretty evident...)
     */
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.<Byte>createKey(EntityMimic.class, DataSerializers.BYTE);
//    private static final DataParameter<Optional<ItemStack>> ITEMSTACK = EntityDataManager.<Optional<ItemStack>>createKey(EntityMimic.class, DataSerializers.OPTIONAL_ITEM_STACK);

    public boolean dropItemsOnBreak = true;
    public NBTTagCompound tileTag = new NBTTagCompound();
    public int metaOfReplacedBlock = 0;

//    public ItemStack heldStack = null;

    public EntityMimic(World worldIn)
    {
        super(worldIn);
        this.setSize(1.4F, 0.9F);
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityMimic.AISpiderAttack(this));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityMimic.AISpiderTarget(this, EntityPlayer.class));
        this.targetTasks.addTask(3, new EntityMimic.AISpiderTarget(this, EntityIronGolem.class));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);

        tag.setBoolean("dropItemsOnBreak", dropItemsOnBreak);
        tag.setTag("tileTag", tileTag);
        tag.setInteger("metaOfReplacedBlock", metaOfReplacedBlock);

//        NBTTagCompound itemTag = new NBTTagCompound();
//        if (heldStack != null)
//        {
//            heldStack.writeToNBT(itemTag);
//        }
//
//        tag.setTag("heldItem", itemTag);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);

        dropItemsOnBreak = tag.getBoolean("dropItemsOnBreak");
        tileTag = tag.getCompoundTag("tileTag");
        metaOfReplacedBlock = tag.getInteger("metaOfReplacedBlock");
//        NBTTagCompound itemTag = tag.getCompoundTag("heldItem");
//
//        if (!itemTag.hasNoTags())
//        {
//            heldStack = ItemStack.loadItemStackFromNBT(itemTag);
//        }
//        mimicedTile = getTileFromStackWithTag(worldObj, pos, getStackInSlot(0), tileTag, metaOfReplacedBlock);
    }

    public ItemStack getItemStack()
    {
//        System.out.println(heldStack);
        return this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
//        return ItemStack.copyItemStack(heldStack);
    }

    public void setItemStack(ItemStack stack)
    {
        this.setItemStackToSlot(EntityEquipmentSlot.CHEST, stack);
    }

    public boolean spawnHeldBlockOnDeath(World world, BlockPos pos)
    {
        return world.isAirBlock(pos) && TileMimic.replaceMimicWithBlockActual(world, pos, getItemStack(), tileTag, metaOfReplacedBlock);
    }

    public boolean spawnMimicBlockAtPosition(World world, BlockPos pos)
    {
        if (world.isAirBlock(pos))
        {
            IBlockState mimicState = ModBlocks.mimic.getStateFromMeta(BlockMimic.sentientMimicMeta);
            world.setBlockState(pos, mimicState, 3);
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileMimic)
            {
                TileMimic mimic = (TileMimic) tile;
                mimic.metaOfReplacedBlock = metaOfReplacedBlock;
                mimic.tileTag = tileTag;
                mimic.setInventorySlotContents(0, getItemStack());
                mimic.dropItemsOnBreak = dropItemsOnBreak;
                mimic.refreshTileEntity();
            }
        }

        return false;
    }

    public void initializeMimic(ItemStack heldStack, NBTTagCompound tileTag, boolean dropItemsOnBreak, int metaOfReplacedBlock)
    {
        this.setItemStack(heldStack);
        this.tileTag = tileTag;
        this.dropItemsOnBreak = dropItemsOnBreak;
        this.metaOfReplacedBlock = metaOfReplacedBlock;
    }

    @Override
    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);

        if (!worldObj.isRemote)
        {
            BlockPos centerPos = this.getPosition();

            int horizontalRadius = 1;
            int verticalRadius = 1;

            for (int hR = 0; hR <= horizontalRadius; hR++)
            {
                for (int vR = 0; vR <= verticalRadius; vR++)
                {
                    for (int i = -hR; i <= hR; i++)
                    {
                        for (int k = -hR; k <= hR; k++)
                        {
                            for (int j = -vR; j <= vR; j += 2 * vR + (vR > 0 ? 0 : 1))
                            {
                                if (!(Math.abs(i) == hR || Math.abs(k) == hR))
                                {
                                    continue;
                                }

                                BlockPos newPos = centerPos.add(i, j, k);
                                if (spawnHeldBlockOnDeath(worldObj, newPos))
                                {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding
     * this one.
     */
    @Override
    public double getMountedYOffset()
    {
        return (double) (this.height * 0.5F);
    }

    /**
     * Returns new PathNavigateGround instance
     */
    @Override
    protected PathNavigate getNewNavigator(World worldIn)
    {
        return new PathNavigateClimber(this, worldIn);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(CLIMBING, Byte.valueOf((byte) 0));
//        this.dataManager.register(ITEMSTACK, null);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote)
        {
            this.setBesideClimbableBlock(this.isCollidedHorizontally);
        }
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound()
    {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    @Override
    public boolean isOnLadder()
    {
        return this.isBesideClimbableBlock();
    }

    /**
     * Sets the Entity inside a web block.
     */
    @Override
    public void setInWeb()
    {

    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    public boolean isPotionApplicable(PotionEffect potioneffectIn)
    {
        return potioneffectIn.getPotion() == MobEffects.POISON ? false : super.isPotionApplicable(potioneffectIn);
    }

    /**
     * Returns true if the WatchableObject (Byte) is 0x01 otherwise returns
     * false. The WatchableObject is updated using setBesideClimableBlock.
     */
    public boolean isBesideClimbableBlock()
    {
        return (((Byte) this.dataManager.get(CLIMBING)).byteValue() & 1) != 0;
    }

    /**
     * Updates the WatchableObject (Byte) created in entityInit(), setting it to
     * 0x01 if par1 is true or 0x00 if it is false.
     */
    public void setBesideClimbableBlock(boolean climbing)
    {
        byte b0 = ((Byte) this.dataManager.get(CLIMBING)).byteValue();

        if (climbing)
        {
            b0 = (byte) (b0 | 1);
        } else
        {
            b0 = (byte) (b0 & -2);
        }

        this.dataManager.set(CLIMBING, Byte.valueOf(b0));
    }

    public float getEyeHeight()
    {
        return 0.65F;
    }

    static class AISpiderAttack extends EntityAIAttackMelee
    {
        public AISpiderAttack(EntityMimic spider)
        {
            super(spider, 1.0D, true);
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean continueExecuting()
        {
            float f = this.attacker.getBrightness(1.0F);

            if (f >= 0.5F && this.attacker.getRNG().nextInt(100) == 0)
            {
                this.attacker.setAttackTarget((EntityLivingBase) null);
                return false;
            } else
            {
                return super.continueExecuting();
            }
        }

        protected double getAttackReachSqr(EntityLivingBase attackTarget)
        {
            return (double) (4.0F + attackTarget.width);
        }
    }

    static class AISpiderTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T>
    {
        public AISpiderTarget(EntityMimic spider, Class<T> classTarget)
        {
            super(spider, classTarget, true);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            float f = this.taskOwner.getBrightness(1.0F);
            return f >= 0.5F ? false : super.shouldExecute();
        }
    }
}