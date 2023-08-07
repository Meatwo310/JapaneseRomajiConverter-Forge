# Japanese Romaji Converter

Japanese version is available in [#概要](#概要).  
日本語版は[#概要](#概要)から閲覧可能です。


# English Version

## Description

This is a mod that automatically converts Romaji sent in chat to Hiragana.


## Usage

When you send a message in chat, it will be automatically converted to Hiragana. Example: `konnnitiha` → `こんにちは`  
If you put one of the symbols `!#;` at the beginning of the message, it will not be converted. Example: `#Hello, world!` → `Hello, world!`  
Also, messages with less than 5 characters will not be converted. To force conversion, put one of the symbols `\¥￥` at the beginning of the message. Example: `hi` → `hi`, `\yaa` → `やあ`  


## Credits

In this mod, when converting Romaji to Hiragana,
[`romaji_to_hiragana.csv`](https://github.com/andree-surya/moji4j/blob/ea0168f125da8791e951eab7cdf18b06a7db705b/src/main/resources/romaji_to_hiragana.csv) in [andree-surya/moji4j](https://github.com/andree-surya/moji4j) for use in this project. Special thanks to andree-surya! A copy of the license is as follows:

```
© Copyright 2016 Andree Surya

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


## Contributors

Special thanks to those who contributed to the creation of this mod!
- [@Hiiragi283](https://github.com/Hiiragi283)
- [@daizu-007](https://github.com/daizu-007)


## License
This project is licensed under the Apache License 2.0. See [LICENSE](./LICENSE) for more details.


# Japanese Version

## 概要

チャットで送信されたローマ字を、自動でひらがなに変換するMODです。


## 使い方

チャットでローマ字を送信すると自動的にひらがなに変換されます。 例:`konnnitiha`→`こんにちは`  
`!#;`のいずれかの記号をメッセージの先頭につけると、変換されなくなります。 例:`#Hello, world!`→`Hello, world!`  
また、5文字未満のメッセージは変換されません。 強制的に変換するには、`\¥￥`のいずれかの記号をメッセージの先頭につけてください。 例:`hi`→`hi`,`\yaa`→`やあ`  


## クレジット

このMODでは、ローマ字をひらがなに変換する際に、 [andree-surya/moji4j](https://github.com/andree-surya/moji4j) の [`romaji_to_hiragana.csv`](https://github.com/andree-surya/moji4j/blob/ea0168f125da8791e951eab7cdf18b06a7db705b/src/main/resources/romaji_to_hiragana.csv) を変換辞書のベースとして使用しています。andree-surya氏にスペシャルなサンキューを。ライセンスのコピーは[#Credits](#Credits)を参照してください。


## 貢献者

このMODの作成に貢献してくださった方々にスペシャルなサンキューを!
- [@Hiiragi283](https://github.com/Hiiragi283)
- [@daizu-007](https://github.com/daizu-007)


## ライセンス
このプロジェクトは、Apache License 2.0の下でライセンスされています。詳細は[LICENSE](./LICENSE)を参照してください。
