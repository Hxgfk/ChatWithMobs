package com.hxgfk.cwm.mixin;

import com.hxgfk.cwm.chat.IMobChat;
import com.hxgfk.cwm.main.CWM;
import com.hxgfk.cwm.util.MessageEntry;
import com.hxgfk.cwm.util.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;
import java.util.LinkedList;

@Mixin(LivingEntity.class)
public abstract class LivingMixin implements IMobChat {
    @Shadow public abstract Brain<?> getBrain();

    @Unique
    private LinkedList<MessageEntry> messages = new LinkedList<>();
    @Unique
    private boolean isChatting = false;
    @Unique
    private float temperature = 0.25f;

    @Override
    public LinkedList<MessageEntry> getMessages() {
        return this.messages;
    }

    @Override
    public void addMessage(MessageEntry msg) {
        this.messages.add(msg);
    }

    @Override
    public void clearMsgList() {
        this.messages.clear();
    }

    @Override
    public void setAll(LinkedList<MessageEntry> messages) {
        this.messages = messages;
    }

    @Override
    public boolean isFirstChat() {
        return messages.isEmpty();
    }

    @Override
    public void setChatting(boolean isChatting) {
        this.isChatting = isChatting;
    }

    @Override
    public float getTemperature() {
        return temperature;
    }

    @Override
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    @Override
    public boolean isChatting() {
        return isChatting;
    }

    @Override
    public void stopAI() {
        ServerLevel sl = ((LivingEntity)(Object)this).getServer().getLevel(((LivingEntity)(Object)this).getLevel().dimension());
        try {
            Method method = Brain.class.getMethod("stopAll", ServerLevel.class, LivingEntity.class);
            method.invoke(this, sl, this);
        } catch (Exception e) {
            CWM.LOGGER.error(e);
        }
    }

    private static final float MAX_TEMPERATURE = 2.0f;
    private static final float MIN_TEMPERATURE = 0.0f;

    @Override
    public float randomTemperature() {
        return MIN_TEMPERATURE + ((LivingEntity) (Object) this).getRandom().nextFloat() * (MAX_TEMPERATURE - MIN_TEMPERATURE);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    public void defineSynchedData(CallbackInfo ci) {
        temperature = randomTemperature();
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
        if (!pCompound.contains("ChatMessageList")) pCompound.put("ChatMessageList", new ListTag());
        ListTag mglist = pCompound.getList("ChatMessageList", ListTag.TAG_COMPOUND);
        for (MessageEntry me : getMessages()) {
            mglist.add(me.toNbt());
        }
        pCompound.putBoolean("isChatting", isChatting);
        pCompound.putFloat("chatTemperature", temperature);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
        if (!pCompound.contains("ChatMessageList")) pCompound.put("ChatMessageList", new ListTag());
        ListTag mglist = pCompound.getList("ChatMessageList", ListTag.TAG_COMPOUND);
        for (Tag t : mglist) {
            if (t instanceof CompoundTag comp) {
                addMessage(new MessageEntry(MessageType.valueOf(comp.getString("type")), comp.getString("content")));
            }
        }
        isChatting = pCompound.getBoolean("isChatting");
        temperature = pCompound.getFloat("chatTemperature");
    }
}
