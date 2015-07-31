package WayofTime.alchemicalWizardry.common.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.common.IDemon;
import WayofTime.alchemicalWizardry.common.items.DemonCrystal;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class EntityDemon extends EntityTameable implements IDemon
{
    protected boolean isAggro;
    protected String demonID;
    
    protected float maxTamedHealth = 100.0F;
    protected float maxUntamedHealth = 200.0F;
    protected int attackTimer;
    
    protected boolean dropCrystal = true;

    public EntityDemon(World par1World, String demonID)
    {
        super(par1World);
        this.demonID = demonID;
    }
    
    @Override
    public boolean getDoesDropCrystal()
    {
    	return dropCrystal;
    }
    
    @Override
    public void setDropCrystal(boolean crystal)
    {
    	this.dropCrystal = crystal;
    }

    @Override
    public void setSummonedConditions()
    {
        this.setAggro(true);
    }

    @Override
    public boolean isAggro()
    {
        return this.isAggro;
    }

    @Override
    public void setAggro(boolean aggro)
    {
        this.isAggro = aggro;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable entityageable)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
    	super.writeToNBT(tag);
    	
    	tag.setBoolean("dropCrystal", this.getDoesDropCrystal());
    	tag.setBoolean("isAggro", isAggro);
    	tag.setString("demonID", demonID);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
    	super.readFromNBT(tag);
    	
    	this.setDropCrystal(tag.getBoolean("dropCrystal"));
    	isAggro = tag.getBoolean("isAggro");
    	demonID = tag.getString("demonID");
    }

    @Override
    protected void dropFewItems(boolean par1, int par2)
    {
    	if(this.getDoesDropCrystal())
    	{
    		ItemStack drop = new ItemStack(ModItems.demonPlacer);
            
            DemonCrystal.setDemonString(drop, this.getDemonID());

            if ((this.getOwner() instanceof EntityPlayer))
            {
                DemonCrystal.setOwnerName(drop, SpellHelper.getUsername((EntityPlayer) this.getOwner()));
            }

            if (this.hasCustomName())
            {
                drop.setStackDisplayName(this.getCustomNameTag());
            }

            this.entityDropItem(drop, 0.0f);
    	} 
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!this.isAggro() && worldObj.getWorldTime() % 100 == 0)
        {
            this.heal(1);
        }
    }

    public void sendSittingMessageToPlayer(EntityPlayer owner, boolean isSitting)
    {
        if (owner != null && owner.worldObj.isRemote)
        {
            ChatComponentTranslation chatmessagecomponent;

            if (isSitting)
            {
                chatmessagecomponent = new ChatComponentTranslation("message.demon.willstay");
            } else
            {
                chatmessagecomponent = new ChatComponentTranslation("message.demon.shallfollow");
            }

            owner.addChatComponentMessage(chatmessagecomponent);
        }
    }

    public String getDemonID()
    {
        return this.demonID;
    }
    
    protected void setDemonID(String id)
    {
    	this.demonID = id;
    }
    

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(EntityLivingBase par1EntityLivingBase)
    {
        super.setAttackTarget(par1EntityLivingBase);

        if (par1EntityLivingBase == null)
        {
            this.setAngry(false);
        } else if (!this.isTamed())
        {
            this.setAngry(true);
        }
    }

    @Override
    /**
     * main AI tick function, replaces updateEntityActionState
     */
    protected void updateAITick()
    {
        this.dataWatcher.updateObject(18, this.getHealth());
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(18, this.getHealth());
        this.dataWatcher.addObject(19, 0);
        //this.dataWatcher.addObject(20, new Byte((byte) BlockColored.getBlockFromDye(1)));
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        this.playSound("mob.zombie.step", 0.15F, 1.0F);
    }

    @Override
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("Angry", this.isAngry());
        par1NBTTagCompound.setByte("attackTimer", (byte) attackTimer);
    }

    @Override
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.setAngry(par1NBTTagCompound.getBoolean("Angry"));

        attackTimer = par1NBTTagCompound.getByte("attackTimer");
    }

    @Override
    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "none";
    }

    @Override
    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.irongolem.hit";
    }

    @Override
    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.irongolem.death";
    }

    @Override
    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 1.0F;
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return -1;
    }

    public int getAttackTimer()
    {
        return attackTimer;
    }

    @Override
    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
    }

    @Override
    public float getEyeHeight()
    {
        return this.height * 0.8F;
    }

    @Override
    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }

    @Override
    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
//        if (this.isEntityInvulnerable())
//        {
//            return false;
//        } else
        {
            Entity entity = par1DamageSource.getEntity();
            this.aiSit.setSitting(false);

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
            {
                par2 = (par2 + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(par1DamageSource, par2);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity par1Entity)
    {
        this.attackTimer = 10;
        this.worldObj.setEntityState(this, (byte) 4);
        boolean flag = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) (7 + this.rand.nextInt(15)));

        if (flag)
        {
            par1Entity.motionY += 0.4000000059604645D;
        }

        this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
        return flag;
    }

    @Override
    public void setTamed(boolean par1)
    {
        super.setTamed(par1);

        if (par1)
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxTamedHealth);
        } else
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxUntamedHealth);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

        if (this.isTamed())
        {
            if (itemstack != null)
            {
                if (itemstack.getItem() instanceof ItemFood)
                {
                    ItemFood itemfood = (ItemFood) itemstack.getItem();

                    if (itemfood.isWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectFloat(18) < maxTamedHealth)
                    {
                        if (!par1EntityPlayer.capabilities.isCreativeMode)
                        {
                            --itemstack.stackSize;
                        }

                        this.heal((float) itemfood.getHealAmount(itemstack));

                        if (itemstack.stackSize <= 0)
                        {
                            par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
                        }

                        return true;
                    }
                }
            }

            if (this.getOwner() instanceof EntityPlayer && SpellHelper.getUsername(par1EntityPlayer).equalsIgnoreCase(SpellHelper.getUsername((EntityPlayer) this.getOwner())) && !this.isBreedingItem(itemstack))
            {
                if (!this.worldObj.isRemote)
                {
                    this.aiSit.setSitting(!this.isSitting());
                    this.isJumping = false;
//                    this.setPathToEntity(null);
//                    this.setTarget(null);
                    this.setAttackTarget(null);
                }

                this.sendSittingMessageToPlayer(par1EntityPlayer, !this.isSitting());
            }
        } else if (itemstack != null && itemstack.getItem().equals(ModItems.weakBloodOrb) && !this.isAngry())
        {
            if (!par1EntityPlayer.capabilities.isCreativeMode)
            {
                --itemstack.stackSize;
            }

            if (itemstack.stackSize <= 0)
            {
                par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
            }

            if (!this.worldObj.isRemote)
            {
                if (this.rand.nextInt(1) == 0)
                {
                    this.setTamed(true);
//                    this.setPathToEntity(null);
                    this.setAttackTarget(null);
                    this.aiSit.setSitting(true);
                    this.setHealth(maxTamedHealth);
                    this.func_152115_b(par1EntityPlayer.getUniqueID().toString());
                    this.playTameEffect(true);
                    this.worldObj.setEntityState(this, (byte) 7);
                } else
                {
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte) 6);
                }
            }

            return true;
        }

        return super.interact(par1EntityPlayer);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return false;
    }

    /**
     * Determines whether this wolf is angry or not.
     */
    public boolean isAngry()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    /**
     * Sets whether this wolf is angry or not.
     */
    public void setAngry(boolean par1)
    {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (par1)
        {
            this.dataWatcher.updateObject(16, b0 | 2);
        } else
        {
            this.dataWatcher.updateObject(16, b0 & -3);
        }
    }

    @Override
    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(EntityAnimal par1EntityAnimal)
    {
        return false;
    }

    public boolean func_70922_bv()
    {
        return this.dataWatcher.getWatchableObjectByte(19) == 1;
    }

    @Override
    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return false;
    }

    @Override
    public boolean func_142018_a(EntityLivingBase par1EntityLivingBase, EntityLivingBase par2EntityLivingBase)
    {
        if (!(par1EntityLivingBase instanceof EntityCreeper) && !(par1EntityLivingBase instanceof EntityGhast))
        {
            if (par1EntityLivingBase instanceof EntityBileDemon)
            {
                EntityBileDemon entitywolf = (EntityBileDemon) par1EntityLivingBase;

                if (entitywolf.isTamed() && entitywolf.getOwner() == par2EntityLivingBase)
                {
                    return false;
                }
            }

            return par1EntityLivingBase instanceof EntityPlayer && par2EntityLivingBase instanceof EntityPlayer && !((EntityPlayer) par2EntityLivingBase).canAttackPlayer((EntityPlayer) par1EntityLivingBase) ? false : !(par1EntityLivingBase instanceof EntityHorse) || !((EntityHorse) par1EntityLivingBase).isTame();
        } else
        {
            return false;
        }
    }
}
