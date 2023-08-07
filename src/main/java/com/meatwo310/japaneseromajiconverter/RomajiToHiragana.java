package com.meatwo310.japaneseromajiconverter;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.net.URL;
import java.util.LinkedHashMap;

public class RomajiToHiragana {
    private static final LinkedHashMap<String, String[]> romajiToJapaneseMap = new LinkedHashMap<>();
    private static final Logger LOGGER = LogUtils.getLogger();

    private static void setupRomajiMapFromResourceName(String resourceName) {
        URL pathToTable = RomajiToHiragana.class.getResource(resourceName);

        if (pathToTable == null) {
            System.out.println("Could not find resource: " + resourceName);
            return;
        }

        try (java.io.InputStream stream = pathToTable.openStream()) {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String[] body = new String[2];
                switch (parts.length) {
                    case 2 -> {
                        body[0] = parts[1];
                        body[1] = "0";
                    }
                    case 3 -> {
                        // Int型に変換可能か確認
                        try {
                            Integer.parseInt(parts[2]);
                        } catch (Exception e) {
                            LOGGER.warn("Invalid line in " + resourceName + "; This line will be ignored: " + line);
                            continue;
                        }
                        body[0] = parts[1];
                        body[1] = parts[2];
                    }
                    default -> {
                        // throw new RuntimeException("Invalid line in romaji_to_hiragana.csv: " + line);
                        if (!line.matches("")) {
                            LOGGER.warn("Invalid line in " + resourceName + "; This line will be ignored: " + line);
                        }
                        continue;
                    }
                }

                romajiToJapaneseMap.put(parts[0], body);
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        // setupRomajiMapFromResourceName("/assets/japaneseromajiconverter/romaji_to_hiragana_0.csv");
        setupRomajiMapFromResourceName("/assets/japaneseromajiconverter/romaji_to_hiragana.csv");
        setupRomajiMapFromResourceName("/assets/japaneseromajiconverter/romaji_to_hiragana_2.csv");
    }

    public static String convert(String romaji) {
        romaji = romaji.toLowerCase();
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < romaji.length()) {
            boolean found = false;
            for (int j = 4; j >= 1; j--) {
                if (i + j <= romaji.length()) {
                    String substring = romaji.substring(i, i + j);
                    if (romajiToJapaneseMap.containsKey(substring)) {
                        result.append(romajiToJapaneseMap.get(substring)[0]);
                        i += j + Integer.parseInt(romajiToJapaneseMap.get(substring)[1]);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                result.append(romaji.charAt(i));
                i++;
            }
        }
        return result.toString();
    }
}
