package com.modernfactions;

import java.util.UUID;

/**
 * Represents 32x32 chunks of claims
 */
public class ClaimSector {
    UUID[][] fuuid = new UUID[32][32];
    String world;
    int sx;
    int sz;

    boolean needsSaving = false;

    public ClaimSector(String world, int sx, int sz) {
        this.world = world;
        this.sx = sx;
        this.sz = sz;
    }
}
