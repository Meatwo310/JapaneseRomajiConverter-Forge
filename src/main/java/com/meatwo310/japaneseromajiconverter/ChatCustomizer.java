package com.meatwo310.japaneseromajiconverter;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChatCustomizer {
    // private static final Logger LOGGER = LogUtils.getLogger();

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

        // String resultText = "<" + entity.getDisplayName().getString() + "> ";
        String resultText;

        // メッセージを条件に応じて変換
        if (baseText.isEmpty()) {
            // 空文字の場合は変換しない
            return;
        }  else if (baseText.length() < 5 && !baseText.matches("^[\\\\¥￥].*$")) {
            // 短すぎる場合は変換しない(\や¥などで始まる場合は変換)
            return;
        } else if (baseText.startsWith(":")) {
            // :で始まる場合は変換しない
            return;
        } else if (!baseText.matches("^[!-~\\s¥￥]+$")) {
            // ASCII文字や\¥￥以外の文字が含まれる場合は変換しない
            return;
        } else if (baseText.matches("^[!#;].*$")) {
            // !#;で始まる場合は変換しないが、頭文字を取り除いて括弧付きで元のメッセージを示す
            resultText = baseText.substring(1) + " §7(" + baseText + ")";
        } else if (baseText.matches("^[\\\\¥￥].*$")) {
            // \¥￥で始まる場合は、頭文字を取り除いて変換し、括弧付きで元のメッセージを示す
            resultText = RomajiToHiragana.convert(baseText.substring(1)) + " §7(" + baseText + ")";
        } else {
            // 条件に当てはまらない普通のローマ字メッセージは変換
            resultText = RomajiToHiragana.convert(baseText) + " §7(" + baseText + ")";
        }

        // 変換結果を送信
        event.setMessage(Component.literal(resultText));
    }
}
