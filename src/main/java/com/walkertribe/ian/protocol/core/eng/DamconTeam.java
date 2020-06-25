package com.walkertribe.ian.protocol.core.eng;

import com.walkertribe.ian.util.GridCoord;

/**
 * A single DAMCON team.
 * @author rjwut
 */
public class DamconTeam {
    private byte id;
    private GridCoord location;
    private GridCoord goal;
    private float progress;
    private int members;

    /**
     * Creates a new DAMCON team with the given status.
     */
    public DamconTeam(byte id, GridCoord location, GridCoord goal, float progress, int members) {
        this.id = id;
        this.location = location;
        this.goal = goal;
        this.progress = progress;
        this.members = members;
    }

    /**
     * Clones the given DAMCON team.
     */
    public DamconTeam(DamconTeam original) {
        id = original.id;
        location = original.location;
        goal = original.goal;
        progress = original.progress;
        members = original.members;
    }

    /**
     * The DAMCON team's ID.
     */
    public byte getId() {
        return id;
    }

    /**
     * The DAMCON team's current location in the nodes.
     */
    public GridCoord getLocation() {
        return location;
    }

    /**
     * The DAMCON team's destination in the nodes.
     */
    public GridCoord getGoal() {
        return goal;
    }

    /**
     * The DAMCON team's progress toward the goal. This value starts at 1.0 when the goal is first
     * set, and gradually decreases to 0.0 as the team moves toward the goal.
     */
    public float getProgress() {
        return progress;
    }

    /**
     * The number of engineers in the DAMCON team.
     */
    public int getMembers() {
        return members;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("DAMCON #").append(id)
        .append(" (").append(members).append("): ")
        .append(location)
        .append(" => ")
        .append(goal)
        .append(" (").append(progress).append(")");
        return b.toString();
    }
}
