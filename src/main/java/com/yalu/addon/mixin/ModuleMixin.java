package com.yalu.addon.mixin;

import com.yalu.addon.TranslateAddon;
import com.yalu.addon.Translator;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.logging.Logger;

import static com.yalu.addon.TranslateAddon.MC;
import static com.yalu.addon.TranslateAddon.TRANSLATOR;

@Mixin(value = Module.class,remap = false,priority = 999)
public abstract class ModuleMixin {
    @Final
    @Mutable
    @Shadow
    public String title;
    @Mutable
    @Shadow
    @Final
    public String description;
    @Inject(method = "<init>*", at = @At("RETURN"))
    public void onInit(CallbackInfo ci){
        // 保存原始的title值作为模块名称
        String originalName = this.title;
        if(originalName == null || originalName.isEmpty()) {
            return;
        }
        
        // 将模块名称转换为语言文件中的key格式：小写，空格替换为连字符
        // 例如 "Anchor Aura" -> "anchor-aura"
        String moduleKeyName = originalName.toLowerCase().replace(' ', '-');
        
        TRANSLATOR.reload(MC.getResourceManager());
        String ModuleKey = "Module.Meteor." + moduleKeyName;
        String DescriptionKey = "Module.Meteor." + moduleKeyName + ".Description";
        this.title = TRANSLATOR.Translate(ModuleKey, originalName);
        this.description = TRANSLATOR.Translate(DescriptionKey,this.description);
    }
}
