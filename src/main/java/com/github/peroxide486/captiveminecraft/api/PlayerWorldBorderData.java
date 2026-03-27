package com.github.peroxide486.captiveminecraft.api;

/**
 *  This class is responsible for handling Player WorldBorder Data
 **/
public class PlayerWorldBorderData {
    private double centerX;
    private double centerZ;
    private double size;
    private double damageAmount;
    private double damageBuffer;
    private double warningDistance;
    private double warningTime;
    private double distance;
    private double addDistanceTime;

    /**
     * Set the center of the player world border
     *
     * @param centerX The x coordinate of the center position as a double value
     * @param centerZ The z coordinate of the center position as a double value
     */
    public void setPlayerWorldCenter(double centerX, double centerZ) {
        this.centerX = centerX;
        this.centerZ = centerZ;
    }

    /**
     * Get the center of the player world border
     *
     * @return The content is concatenated in string format, with the format being (x, z)
     */
    public String getPlayerWorldCenter() {
        return String.valueOf(centerX)+ ", " + String.valueOf(centerZ);
    }

    /**
     * Set the new size of the player world border
     *
     * @param newSize The new size of the world border as a double value.
     */
    public void setPlayerWorldSize(double newSize) {
        this.size = newSize;
    }

    /**
     * Get the size of the player world border
     *
     * @return Size of the player world border
     */
    public double getPlayerWorldSize() {
        return size;
    }

    /**
     * Set the damage amount of the player world border
     *
     * @param newDamageAmount The amount of damage players receive per second when outside the world border buffer zone
     */
    public void setPlayerDamageAmount(double newDamageAmount) {
        this.damageAmount = newDamageAmount;
    }

    /**
     * Get the damage amount of the player world border
     *
     * @return The amount of damage players receive per second when outside the world border buffer zone
     */
    public double getPlayerDamageAmount() {
        return damageAmount;
    }

    /**
     * Set the damage buffer of the player world border
     *
     * @param newDamageBuffer A safe buffer distance where players remain unharmed even after crossing the world border
     */
    public void setPlayerDamageBuffer(double newDamageBuffer) {
        this.damageBuffer = newDamageBuffer;
    }

    /**
     * Get the damage buffer of the player world border
     *
     * @return A safe buffer distance where players remain unharmed even after crossing the world border
     */
    public double getPlayerDamageBuffer() {
        return damageBuffer;
    }

    /**
     * Set the warning distance of the player world border
     *
     * @param newWarningDistance Distance approaching the world border warning
     */
    public void setPlayerWarningDistance(double newWarningDistance) {
        this.warningDistance = newWarningDistance;
    }

    /**
     * Get the warning distance of the player world border
     *
     * @return Distance approaching the world border warning
     */
    public double getPlayerWarningDistance() {
        return warningDistance;
    }

    /**
     * Set the warning time of the player world border
     *
     * @param newWarningTime The time for the world border proximity warning
     */
    public void setPlayerWarningTime(double newWarningTime) {
        this.warningTime = newWarningTime;
    }

    /**
     * Get the warning time of the player world border
     *
     * @return The time for the world border proximity warning
     */
    public double getPlayerWarningTime() {
        return warningTime;
    }

    /**
     * Set the world distance of the player world border
     *
     * @param newDistance The length of the player world border
     */
    public void setPlayerDistance(double newDistance) {
        this.distance = newDistance;
    }

    /**
     * Add the world distance of the player world border
     *
     * @param addDistance Add length of the player world border
     * @return The new length of the player world border
     */
    public double addPlayerDistance(double addDistance) {
        return this.distance + addDistance;
    }

    /**
     * Get the world distance of the player world border
     *
     * @return The length of the player world border
     */
    public double getPlayerDistance() {
        return distance;
    }

    /**
     * Set the world distance time of the player world border
     *
     * @param newAddDistanceTime The duration of time for the transition of the player world border from old border to new border
     */
    public void setPlayerAddDistanceTime(double newAddDistanceTime) {
        this.addDistanceTime = newAddDistanceTime;
    }

    /**
     * Get the world distance time of the player world border
     * 
     * @return The duration of time for the transition of the player world border from old border to new border
     */
    public double getPlayerAddDistanceTime() {
        return addDistanceTime;
    }
}
