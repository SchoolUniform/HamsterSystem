package com.github.schooluniform.hamstersystem.data.entity;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.github.schooluniform.hamstersystem.HamsterSystem;
import com.github.schooluniform.hamstersystem.data.Data;
import com.github.schooluniform.hamstersystem.entity.EntityAttribute;
import com.github.schooluniform.hamstersystem.util.Util;

public class FightEntity{
	private UUID entityUUID;
	private EntityType entityType;
	
	private double health;
	private double shield ;
	private double energy;
	protected HashMap<EntityAttribute, Double> attributes;
	
	//Clone
	public FightEntity(FightEntity entity){
		this.entityUUID = entity.entityUUID;
		this.entityType = entity.entityType;
		this.health = entity.health;
		this.shield = entity.shield;
		this.energy = entity.energy;
		
		HashMap<EntityAttribute,Double> attributes = new HashMap<>();
		attributes.putAll(entity.attributes);
		
		this.attributes = attributes;
		if(!attributes.containsKey(EntityAttribute.Armor))
			attributes.put(EntityAttribute.Armor, 1D);
	}
	
	public FightEntity(UUID entityUUID, EntityType entityType, double health, double shield, double energy,
			HashMap<EntityAttribute, Double> attributes) {
		super();
		this.entityUUID = entityUUID;
		this.entityType = entityType;
		this.health = ((LivingEntity)Bukkit.getEntity(entityUUID)).getHealth();
		this.shield = shield;
		this.energy = energy;
		this.attributes = attributes;
		if(!attributes.containsKey(EntityAttribute.Armor))
			attributes.put(EntityAttribute.Armor, 1D);
	}
	
	//create new
	public static FightEntity getFightEntity(LivingEntity entity){
		if(entity instanceof Player) return Data.getPlayerData(entity.getName()).getFight();
		
		HashMap<EntityAttribute, Double> attributes;
		if(entity.hasMetadata(EntityTag.LinkTo.name())){
			String linkTo = entity.getMetadata(EntityTag.LinkTo.name()).get(0).asString();
			YamlConfiguration data = YamlConfiguration.loadConfiguration(new File(HamsterSystem.plugin.getDataFolder()+"/mob/"+linkTo+".yml"));
			attributes = new HashMap<>();
			for(String attribute : data.getKeys(false))
				attributes.put(EntityAttribute.valueOf(attribute), data.getDouble(attribute));
		}else{
			attributes = Util.getDefaultEntityAttributes();
		}
		
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(attributes.get(EntityAttribute.Health));
		entity.setHealth(attributes.get(EntityAttribute.Health));
		return new FightEntity(entity.getUniqueId(),entity.getType(),
				attributes.get(EntityAttribute.Health),
				attributes.get(EntityAttribute.Shield),
				0,
				attributes);
		
	}

	public LivingEntity getEntity(){
		return (LivingEntity)Bukkit.getEntity(entityUUID);
	}
	
	public EntityType getEntityType(){
		return entityType;
	}
	
	public double getAttribute(EntityAttribute attribute){
		if(!attributes.containsKey(attribute))return 0;
		return attributes.get(attribute);
	}
	
	public void setAttribute(EntityAttribute attribute,double value){
		attributes.replace(attribute, value);
	}
	
	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
		this.health = health;
	}
	
	public void modifyHealth(double health) {
		this.health += health;
	}

	public double getShield() {
		return shield;
	}

	public void setShield(double shield) {
		this.shield = shield;
	}
	
	public void modifyShield(double shield) {
		this.shield += shield;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}
	
	public void modifyEnergy(double energy) {
		this.energy += energy;
	}
	
	
}
