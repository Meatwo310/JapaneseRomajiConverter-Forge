package com.meatwo310.japaneseromajiconverter;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

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

        if (baseText.isEmpty()) {
            // 空文字は変換しない
            return;
        }

        String resultText;
        final String convertedText = RomajiToHiragana.convert(baseText);

        // メッセージを条件に応じて変換
        if (Objects.equals(convertedText, baseText)) {
            // 変換前後で文字列が変わらない場合は変換しない
            return;
        } else if (baseText.matches("^[!#;].*$")) {
            // !#;で始まる場合は変換しないが、頭文字はグレーに変換する
            resultText = "§7" + baseText.charAt(0) + "§r" + baseText.substring(1);
        } else if (baseText.matches("^[\\\\¥￥].*$")) {
            // \¥￥で始まる場合は頭文字を取り除いて強制的に変換し、括弧付きで原文を示す
            resultText = RomajiToHiragana.convert(baseText.substring(1)) + " §7(" + baseText + ")";
        } else if (baseText.length() < 5) {
            // 短すぎる場合は変換しない
            return;
        } else if (baseText.startsWith(":")) {
            // :で始まる場合は変換しない
            return;
        } else if (!baseText.matches("^[!-~\\s¥￥]+$")) {
            // ASCII文字のみではない場合は変換しない
            return;
        } else if (convertedText.replaceAll("[^a-zA-Z]", "").length() > convertedText.length() * 0.25) {
            // 変換結果にアルファベットが多く含まれる場合は変換しない
            resultText = baseText + " §8(変換失敗)";
        } else {
            // 条件に当てはまらない普通のローマ字メッセージは変換を試みる
            resultText = convertedText + " §7(" + baseText + ")";
        }

        // 変換結果を送信
        event.setMessage(Component.literal(resultText));
    }
}
