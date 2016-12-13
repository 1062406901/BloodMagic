package WayofTime.bloodmagic.demonAura;

import WayofTime.bloodmagic.api.soul.DemonWillHolder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.chunk.Chunk;

import java.lang.ref.WeakReference;

@Getter
@Setter
public class WillChunk
{
    PosXY loc;
    private short base;
    private DemonWillHolder currentWill = new DemonWillHolder();
    private WeakReference<Chunk> chunkRef;

    public WillChunk(PosXY loc)
    {
        this.loc = loc;
    }

    public WillChunk(Chunk chunk, short base, DemonWillHolder currentWill)
    {
        this.loc = new PosXY(chunk.xPosition, chunk.zPosition);
        this.chunkRef = new WeakReference(chunk);
        this.base = base;
        this.currentWill = currentWill;
    }

    public boolean isModified() {
        return (this.chunkRef != null) && (this.chunkRef.get() != null) && this.chunkRef.get().needsSaving(false);
    }
}
