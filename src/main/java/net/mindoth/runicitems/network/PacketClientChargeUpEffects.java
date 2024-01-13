package net.mindoth.runicitems.network;

import net.mindoth.runicitems.client.particle.EmberParticleData;
import net.mindoth.runicitems.client.particle.ParticleColor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketClientChargeUpEffects {

    public int r;
    public int g;
    public int b;
    public float size;
    public int age;
    public double x;
    public double y;
    public double z;

    public PacketClientChargeUpEffects(int r, int g, int b, float size, int age, double x, double y, double z) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.size = size;
        this.age = age;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PacketClientChargeUpEffects(PacketBuffer buf) {
        this.r = buf.readInt();
        this.g = buf.readInt();
        this.b = buf.readInt();
        this.size = buf.readFloat();
        this.age = buf.readInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public void encode(PacketBuffer buf) {
        buf.writeInt(this.r);
        buf.writeInt(this.g);
        buf.writeInt(this.b);
        buf.writeFloat(this.size);
        buf.writeInt(this.age);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.level.addParticle(EmberParticleData.createData(new ParticleColor(this.r, this.g, this.b), this.size, this.age),
                    this.x, this.y, this.z, 0, 0, 0);
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
