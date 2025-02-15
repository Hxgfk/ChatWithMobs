package com.hxgfk.cwm.gui;

import com.hxgfk.cwm.chat.IMobChat;
import com.hxgfk.cwm.main.Config;
import com.hxgfk.cwm.network.MessagePacket;
import com.hxgfk.cwm.network.ModDispatcher;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class SendMessage extends ChatScreen {
    public LivingEntity owner;

    public SendMessage(@NotNull LivingEntity owner) {
        super("");
        this.owner = owner;
    }

    @Override
    protected void init() {
        this.input = new EditBox(this.minecraft.fontFilterFishy, 4, this.height - 12, this.width - 4, 12, Component.translatable("chat.editBox"));
        this.input.setMaxLength(Config.maximum_inputs);
        this.input.setBordered(false);
        this.addWidget(this.input);
    }

    public void send() {
        ModDispatcher.INSTANCE.sendToServer(new MessagePacket(owner.getLevel().dimension(), input.getValue(), owner.getId()));
        this.minecraft.setScreen(null);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (this.input.keyPressed(pKeyCode, pScanCode, pModifiers)) {
            return true;
        } else if (pKeyCode == GLFW.GLFW_KEY_ESCAPE && shouldCloseOnEsc()) {
            this.onClose();
            ((IMobChat)owner).setChatting(false);
            return true;
        } else if (pKeyCode == GLFW.GLFW_KEY_ENTER) {
            send();
            return true;
        } else {
            return false;
        }
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.setFocused(this.input);
        this.input.setFocus(true);
        fill(pPoseStack, 2, this.height - 14, this.width - 2, this.height - 2, this.minecraft.options.getBackgroundColor(Integer.MIN_VALUE));
        this.input.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        for(Widget widget : this.renderables) {
            widget.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return true;
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return true;
    }

    @Override
    public void onClose() {
        super.onClose();
        ((IMobChat)owner).setChatting(false);
    }

    @Override
    public void tick() {
        this.input.tick();
    }
}
