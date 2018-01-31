package com.walkertribe.ian.vesseldata;

import com.walkertribe.ian.Context;

public class MutableVessel extends Vessel {
	public MutableVessel(Context ctx, int uniqueID, int side, String className, String broadType) {
		super(ctx, uniqueID, side, className, broadType);
	}
}
