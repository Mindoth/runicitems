package net.mindoth.runicitems.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mindoth.runicitems.RunicItems;
import net.mindoth.runicitems.item.rune.SpellRuneItem;
import net.mindoth.runicitems.item.spellbook.SpellbookItem;
import net.mindoth.runicitems.network.PacketSelectSpellbookSlot;
import net.mindoth.runicitems.network.RunicItemsNetwork;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.opengl.GL11;

import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class GuiSpellSelector extends Screen {

    private static final float PRECISION = 5.0F;
    private final boolean closing;
    final float OPEN_ANIMATION_LENGTH = 0.5F;
    private float totalTime;
    private float prevTick;
    private float extraTick;
    private final IItemHandler itemHandler;
    private final CompoundNBT nbt;
    private int selectedItem;
    private final List<Item> itemList = Lists.newArrayList();

    //TODO Look into toolbelt itemhandler in gui class
    public GuiSpellSelector(IItemHandler itemHandler, CompoundNBT nbt) {
        super(new StringTextComponent(""));
        this.itemHandler = itemHandler;
        this.nbt = nbt;
        this.closing = false;
        this.minecraft = Minecraft.getInstance();
        this.selectedItem = -1;

        for ( int i = 0; i < this.itemHandler.getSlots(); i++ ) {
            if ( !this.itemHandler.getStackInSlot(i).isEmpty() ) this.itemList.add(this.itemHandler.getStackInSlot(i).getItem());
        }
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        if ( this.selectedItem != -1 ) {
            SpellbookItem.setSlot(nbt, this.selectedItem);
            RunicItemsNetwork.CHANNEL.sendToServer(new PacketSelectSpellbookSlot(nbt));
            minecraft.player.closeContainer();
        }
        return true;
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        super.render(ms, mouseX, mouseY, partialTicks);
        if ( this.itemHandler == null ) return;
        float openAnimation = closing ? 1.0F - totalTime / OPEN_ANIMATION_LENGTH : totalTime / OPEN_ANIMATION_LENGTH;
        float currTick = minecraft.getFrameTime();
        totalTime += (currTick + extraTick - prevTick) / 20F;
        extraTick = 0;
        prevTick = currTick;

        float animProgress = MathHelper.clamp(openAnimation, 0, 1);
        animProgress = (float) (1 - Math.pow(1 - animProgress, 3));
        float radiusIn = Math.max(0.1F, 45 * animProgress);
        float radiusOut = radiusIn * 2;
        float itemRadius = (radiusIn + radiusOut) * 0.5F;
        int x = width / 2;
        int y = height / 2;

        int numberOfSlices = Math.max(1, this.itemList.size());

        double a = Math.toDegrees(Math.atan2(mouseY - y, mouseX - x));
        double d = Math.sqrt(Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2));
        float s0 = ((-0.5F / (float)numberOfSlices) + 0.25F) * 360;
        if ( a < s0 ) {
            a += 360;
        }

        RenderSystem.pushMatrix();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        boolean hasMouseOver = false;
        int mousedOverSlot = -1;

        if ( !closing ) {
            this.selectedItem = -1;
            for ( int i = 0; i < numberOfSlices; i++ ) {
                float s = (((i - 0.5F) / (float) numberOfSlices) + 0.25F) * 360;
                float e = (((i + 0.5F) / (float) numberOfSlices) + 0.25F) * 360;
                if ( a >= s && a < e && d >= radiusIn && d < radiusOut ) {
                    this.selectedItem = i;
                    break;
                }
            }
        }

        for ( int i = 0; i < numberOfSlices; i++ ) {
            float s = (((i - 0.5F) / (float) numberOfSlices) + 0.25F) * 360;
            float e = (((i + 0.5F) / (float) numberOfSlices) + 0.25F) * 360;
            if ( this.selectedItem == i ) {
                drawSlice(buffer, x, y, numberOfSlices, radiusIn, radiusOut, s, e, 63, 161, 191, 60);
                hasMouseOver = true;
                mousedOverSlot = this.selectedItem;
            }
            else drawSlice(buffer, x, y, 10, radiusIn, radiusOut, s, e, 0, 0, 0, 64);
        }

        tessellator.end();
        RenderSystem.enableTexture();

        if ( hasMouseOver && mousedOverSlot != -1 ) {
            String name = new TranslationTextComponent("tooltip.runicitems.empty").getString();
            if ( !this.itemList.isEmpty() ) name = new TranslationTextComponent("item.runicitems." + this.itemList.get(mousedOverSlot).getItem()).getString();
            drawCenteredString(ms, font, name, width / 2, (height - font.lineHeight) / 2, 16777215);
        }

        RenderHelper.turnBackOn();
        RenderSystem.popMatrix();

        for ( int i = 0; i < numberOfSlices; i++ ) {
            int magnifier = 24;
            float middle = ((i / (float)numberOfSlices) + 0.25F) * 2 * (float)Math.PI;
            float posX = x - ((float)magnifier / 2) + itemRadius * (float)Math.cos(middle);
            float posY = y - ((float)magnifier / 2) + itemRadius * (float)Math.sin(middle);

            String resourceIcon;
            if ( !this.itemList.isEmpty() && this.itemList.get(i) instanceof SpellRuneItem ) resourceIcon = this.itemList.get(i).getRegistryName().getPath();
            else resourceIcon = "";

            RenderSystem.disableRescaleNormal();
            RenderHelper.turnOff();
            RenderSystem.disableLighting();
            RenderSystem.disableDepthTest();
            if ( !resourceIcon.isEmpty() ) {
                drawItemTexture(new ResourceLocation(RunicItems.MOD_ID, "textures/items/" + resourceIcon + ".png"),
                        (int)posX, (int)posY, 0, 0, magnifier, magnifier, magnifier, magnifier, ms);
            }
        }
    }

    private void drawSlice(BufferBuilder buffer, float x, float y, float z, float radiusIn, float radiusOut, float startAngle, float endAngle,
                           int r, int g, int b, int a) {
        float angle = endAngle - startAngle;
        int sections = Math.max(1, MathHelper.ceil(angle / PRECISION));

        startAngle = (float)Math.toRadians(startAngle);
        endAngle = (float)Math.toRadians(endAngle);
        angle = endAngle - startAngle;

        for ( int i = 0; i < sections; i++ ) {
            float middle = startAngle + (i / (float)sections) * angle;
            float middle2 = startAngle + ((i + 1) / (float)sections) * angle;

            float pos1InX = x + radiusIn * (float)Math.cos(middle);
            float pos1InY = y + radiusIn * (float)Math.sin(middle);
            float pos1OutX = x + radiusOut * (float)Math.cos(middle);
            float pos1OutY = y + radiusOut * (float)Math.sin(middle);
            float pos2OutX = x + radiusOut * (float)Math.cos(middle2);
            float pos2OutY = y + radiusOut * (float)Math.sin(middle2);
            float pos2InX = x + radiusIn * (float)Math.cos(middle2);
            float pos2InY = y + radiusIn * (float)Math.sin(middle2);

            buffer.vertex(pos1OutX, pos1OutY, z).color(r, g, b, a).endVertex();
            buffer.vertex(pos1InX, pos1InY, z).color(r, g, b, a).endVertex();
            buffer.vertex(pos2InX, pos2InY, z).color(r, g, b, a).endVertex();
            buffer.vertex(pos2OutX, pos2OutY, z).color(r, g, b, a).endVertex();
        }
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        int adjustedKey = key - 48;
        if ( adjustedKey >= 0 && adjustedKey < 10 ) {
            this.selectedItem = adjustedKey == 0 ? 10 : adjustedKey;
            mouseClicked(0,0,0);
            return true;
        }
        return super.keyPressed(key, scanCode, modifiers);
    }

    @SubscribeEvent
    public static void overlayEvent(RenderGameOverlayEvent.Pre event) {
        if ( Minecraft.getInstance().screen instanceof GuiSpellSelector ) {
            if ( event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS ) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public void tick() {
        if ( this.itemHandler == null ) {
            Minecraft.getInstance().setScreen(null);
            return;
        }
        if ( totalTime != OPEN_ANIMATION_LENGTH ) {
            extraTick++;
        }
    }

    @SubscribeEvent
    public static void updateInputEvent(InputUpdateEvent event) {
        if ( Minecraft.getInstance().screen instanceof GuiSpellSelector ) {
            GameSettings settings = Minecraft.getInstance().options;
            MovementInput eInput = event.getMovementInput();
            eInput.up = InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyUp.getKey().getValue());
            eInput.down = InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyDown.getKey().getValue());
            eInput.left = InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyLeft.getKey().getValue());
            eInput.right = InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyRight.getKey().getValue());

            eInput.forwardImpulse = eInput.up == eInput.down ? 0.0F : (eInput.up ? 1.0F : -1.0F);
            eInput.leftImpulse = eInput.left == eInput.right ? 0.0F : (eInput.left ? 1.0F : -1.0F);
            eInput.jumping = InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyJump.getKey().getValue());
            eInput.shiftKeyDown = InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), settings.keyShift.getKey().getValue());
            if ( Minecraft.getInstance().player.isMovingSlowly() ) {
                eInput.leftImpulse = (float)((double)eInput.leftImpulse * 0.3D);
                eInput.forwardImpulse = (float)((double)eInput.forwardImpulse * 0.3D);
            }
        }
    }

    public static void drawItemTexture(ResourceLocation resourceLocation, int x, int y, int u, int v, int w, int h, int fileWidth, int fileHeight, MatrixStack stack) {
        Minecraft.getInstance().textureManager.bind(resourceLocation);
        blit(stack,x, y, u, v, w, h, fileWidth, fileHeight);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
