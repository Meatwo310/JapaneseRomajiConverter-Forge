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

        System.out.println(event.getComponent());

        // クライアントサイドでは実行しない
        if (world.isClientSide()) {
            return;
        }

        String resultText;

        if (baseText.isEmpty()) {
            // 空文字は変換しない
            return;
        } else if (baseText.length() < 5 && !baseText.matches("^[\\\\¥￥].*$")) {
            // 短すぎる場合は変換しない(\¥￥で始まる場合を除く)
            return;
        } else if (baseText.startsWith(":")) {
            // :で始まる場合は変換しない
            return;
        } else if (!baseText.matches("^[!-~\\s¥￥]+$")) {
            // ASCII文字や\¥￥以外の文字が含まれる場合は変換しない
            return;
        } else if (baseText.matches("^[!#;].*$")) {
            // !#;で始まる場合は変換しないが、頭文字を取り除き、括弧付きで元のメッセージを示す
            resultText = baseText.substring(1) + " §7(" + baseText + ")";
        } else if (baseText.matches("^[\\\\¥￥].*$")) {
            // \¥￥で始まる場合は、頭文字を取り除いて変換し、括弧付きで元のメッセージを示す
            resultText = RomajiToHiragana.convert(baseText.substring(1)) + " §7(" + baseText + ")";
        } else {
            // 条件に当てはまらない普通のローマ字メッセージは変換
            resultText = RomajiToHiragana.convert(baseText) + " §7(" + baseText + ")";
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
