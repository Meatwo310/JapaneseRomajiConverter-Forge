package com.meatwo310.japaneseromajiconverter;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber
public class ChatCustomizer {
    private static final Logger LOGGER = LogUtils.getLogger();
    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        String baseText = event.getMessage();
        LevelAccessor world = player.level;

//        System.out.println(event.getComponent());

        // クライアントサイドでは実行しない
        if (world.isClientSide()) {
            return;
        }

        // 空文字は変換しない
        if (baseText.isEmpty()) {
            return;
        }

        String resultText;
        final String convertedText = RomajiToHiragana.convert(baseText);

        if (convertedText.equals(baseText)) {
            // 変換前後で文字列が変わらない場合は変換しない
            return;
        } else if (baseText.matches("^[!#;].*$")) {
            // !#;で始まる場合は変換しないが、頭文字はグレーに変換する
//            resultText = baseText.substring(1) + " §7(" + baseText + ")";
            resultText = "§7" + baseText.charAt(0) + "§r" + baseText.substring(1);
        } else if (baseText.matches("^[\\\\¥￥].*$")) {
            // \¥￥で始まる場合は頭文字を取り除いて強制的に変換し、括弧付きで原文を示す
//            resultText = RomajiToHiragana.convert(baseText.substring(1)) + " §7(" + baseText + ")";
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
        event.setComponent(new TranslatableComponent("chat.type.text",
                new TextComponent("")
                    .setStyle(event.getComponent().plainCopy().getStyle())
                    .append(player.getDisplayName())
                ).append(new TextComponent(resultText))
        );
    }
}
