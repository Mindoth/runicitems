package net.mindoth.runicitems.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mindoth.runicitems.registries.RunicItemsParticles;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class ColorParticleTypeData implements IParticleData {

    private ParticleType<ColorParticleTypeData> type;
    public static final Codec<ColorParticleTypeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(d -> d.color.getRed()),
                    Codec.FLOAT.fieldOf("g").forGetter(d -> d.color.getGreen()),
                    Codec.FLOAT.fieldOf("b").forGetter(d -> d.color.getBlue()),
                    Codec.FLOAT.fieldOf("scale").forGetter(d -> d.scale),
                    Codec.BOOL.fieldOf("disableDepthTest").forGetter(d-> d.disableDepthTest)
            )
            .apply(instance, ColorParticleTypeData::new));

    public ParticleColor color;
    public float scale;
    public boolean disableDepthTest;


    static final IParticleData.IDeserializer<ColorParticleTypeData> DESERIALIZER = new IParticleData.IDeserializer<ColorParticleTypeData>() {
        @Override
        public ColorParticleTypeData fromCommand(ParticleType<ColorParticleTypeData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new ColorParticleTypeData(type, ParticleColor.deserialize(reader.readString()), reader.readBoolean());
        }

        @Override
        public ColorParticleTypeData fromNetwork(ParticleType<ColorParticleTypeData> type, PacketBuffer buffer) {
            return new ColorParticleTypeData(type, ParticleColor.deserialize(buffer.readUtf()), buffer.readBoolean());
        }
    };
    public ColorParticleTypeData(float r, float g, float b, float scale){
        this.color = new ParticleColor(r, g, b, scale);
        this.scale = scale;
        this.type = RunicItemsParticles.GLOW_TYPE;
        this.disableDepthTest = false;
    }

    public ColorParticleTypeData(ParticleType<ColorParticleTypeData> particleTypeData, ParticleColor color, boolean disableDepthTest){
        this.type = particleTypeData;
        this.color = color;
        this.disableDepthTest = disableDepthTest;
    }

    public ColorParticleTypeData(float r, float g, float b, float scale, boolean disableDepthTest){
        this(r,g,b,scale);
        this.disableDepthTest = disableDepthTest;
    }



    @Override
    public ParticleType<ColorParticleTypeData> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(PacketBuffer packetBuffer) {
        packetBuffer.writeUtf(color.serialize());
    }

    @Override
    public String writeToString() {
        return type.getRegistryName().toString() + " " + color.serialize();
    }
}