package com.meatwo310.japaneseromajiconverter;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

@Mod.EventBusSubscriber
public class ChatCustomizer {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onChat(ServerChatEvent.Submitted event) {
        ServerPlayer entity = event.getPlayer();
        String text = event.getRawText();
        LevelAccessor world = entity.level;

        // クライアントサイドでは重複実行させない
        if (world.isClientSide()) {
            return;
        }

        String resultText = "<" + entity.getDisplayName().getString() + "> ";

        // TODO: コンポーネントについて調べる

        // 空文字の場合は変換しない
        if (text.isEmpty()) {
//            LOGGER.debug("text is empty");
            resultText += text;
        }
        // 短すぎる場合は変換しない(\や¥などで始まる場合は変換)
        else if (text.length() < 5 && !text.matches("^[\\\\¥￥].*$")) {
//            LOGGER.debug("text is too short");
            resultText += text;
        }
        // :で始まる場合は変換しない
        else if (text.startsWith(":")) {
//            LOGGER.debug("text starts with :");
            resultText += text;
        }
        // ASCII文字や\¥￥以外の文字が含まれる場合は変換しない
        else if (!text.matches("^[!-~\\s¥￥]+$")) {
//            LOGGER.debug("text contains non-ASCII characters");
            resultText += text;
        }
        // !#;で始まる場合は変換しないが、頭文字を取り除いて括弧付きで元のメッセージを示す
        else if (text.matches("^[!#;].*$")) {
//            LOGGER.debug("text starts with !#;");
            resultText += text.substring(1) + " §7(" + text + ")";
        }
        // \¥￥で始まる場合は、頭文字を取り除いて変換し、括弧付きで元のメッセージを示す
        else if (text.matches("^[\\\\¥￥].*$")) {
//            LOGGER.debug("text starts with \\¥￥");
            resultText += RomajiToHiragana.convert(text.substring(1)) + " §7(" + text + ")";
        }
        // 条件に当てはまらない普通のローマ字メッセージは変換
        else {
//            LOGGER.debug("text is normal");
            resultText += RomajiToHiragana.convert(text) + " §7(" + text + ")";
        }

        MinecraftServer _mcserv = ServerLifecycleHooks.getCurrentServer();
        if (_mcserv != null) {
            _mcserv.getPlayerList().broadcastSystemMessage(Component.literal(resultText), false);
        }
//        entity.sendSystemMessage(Component.literal(resultText));

        if (event.isCancelable()) {
            event.setCanceled(true);
        }
    }
}
