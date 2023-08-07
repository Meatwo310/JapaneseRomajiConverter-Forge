package com.meatwo310.japaneseromajiconverter;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChatCustomizer {
//    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onChat(ServerChatEvent.Submitted event) {
        ServerPlayer player = event.getPlayer();
        String baseText = event.getRawText();
        LevelAccessor world = player.level;

        // クライアントサイドでは実行しない
        // 注: シングルプレイでも内部サーバーが建っているためこのMODは機能する
        if (world.isClientSide()) {
            return;
        }

//        String resultText = "<" + entity.getDisplayName().getString() + "> ";
        String resultText;

        // 空文字の場合は変換しない
        if (baseText.isEmpty()) {
//            LOGGER.debug("baseText is empty");
//            resultText += baseText;
            return;
        }
        // 短すぎる場合は変換しない(\や¥などで始まる場合は変換)
        else if (baseText.length() < 5 && !baseText.matches("^[\\\\¥￥].*$")) {
//            LOGGER.debug("baseText is too short");
//            resultText += baseText;
            return;
        }
        // :で始まる場合は変換しない
        else if (baseText.startsWith(":")) {
//            LOGGER.debug("baseText starts with :");
//            resultText += baseText;
            return;
        }
        // ASCII文字や\¥￥以外の文字が含まれる場合は変換しない
        else if (!baseText.matches("^[!-~\\s¥￥]+$")) {
//            LOGGER.debug("baseText contains non-ASCII characters");
//            resultText += baseText;
            return;
        }
        // !#;で始まる場合は変換しないが、頭文字を取り除いて括弧付きで元のメッセージを示す
        else if (baseText.matches("^[!#;].*$")) {
//            LOGGER.debug("baseText starts with !#;");
            resultText = baseText.substring(1) + " §7(" + baseText + ")";
        }
        // \¥￥で始まる場合は、頭文字を取り除いて変換し、括弧付きで元のメッセージを示す
        else if (baseText.matches("^[\\\\¥￥].*$")) {
//            LOGGER.debug("baseText starts with \\¥￥");
            resultText = RomajiToHiragana.convert(baseText.substring(1)) + " §7(" + baseText + ")";
        }
        // 条件に当てはまらない普通のローマ字メッセージは変換
        else {
//            LOGGER.debug("baseText is normal");
            resultText = RomajiToHiragana.convert(baseText) + " §7(" + baseText + ")";
        }

        /*
        MinecraftServer _mcserv = ServerLifecycleHooks.getCurrentServer();
        if (_mcserv != null) {
            _mcserv.getPlayerList().broadcastSystemMessage(Component.literal(resultText), false);
        }
        entity.sendSystemMessage(Component.literal(resultText));
        */

        event.setMessage(Component.literal(resultText));

        /*
        if (event.isCancelable()) {
            event.setCanceled(true);
        }
        */
    }
}
