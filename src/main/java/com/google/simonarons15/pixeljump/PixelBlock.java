package com.google.simonarons15.pixeljump;

import org.bukkit.block.Block;

public class PixelBlock {

    private int hp;
    private Block thisBlock;

    public PixelBlock(int hp, Block thisBlock) {
        this.hp = hp;
        this.thisBlock = thisBlock;
        thisBlock.setType(BlockPutter.materials.get(hp));
    }

    public void update()
    {
        if(hp == 0) return;
        hp--;
        thisBlock.setType(BlockPutter.materials.get(hp), false);
    }

    public int getHp() {
        return hp;
    }
}
