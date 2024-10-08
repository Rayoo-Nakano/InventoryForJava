# AIプグラム作成 
## 最初にこれから実施する内容をSystemPrompt的に定義を行い、LLMと認識を併せる。

---------------------------------------------
### 私の立場
私はJavaプログラムの単体試験手順書を確認する役割です。
Javaプログラムと単体試験プログラムをGitHub経由でAWSのCodeBuildへアップロードして、Buildspecにもとずいて単体試験を実施させます。
### あなたの立場
AWS及びJavaのプログラム、試験環境を熟知したプロフェッショナルです。

さて始めましょう


### プログラム作成
---------------------------------------------
最初にJavaのプログラムを１００行程度で作成しましょう。
課題は在庫検索です。
日本語のコメントを付与して読みやすいプログラムとしてください。
まだエラー処理や異常系を含めてください。
関数名はInvSearchと致しましょう

追加でプログラム配置の標準ディレクトリも教えてください。


### 試験仕様書作成
---------------------------------------------
試験仕様をカバー率100%で作成してください。仕様書には試験で使うデータも記載してください
フォーマットまMD形式です


### 試験プログラム作成
---------------------------------------------
試験プログラムを作成してください


### プログラム仕様書作成
---------------------------------------------
おっと忘れていました。プログラム仕様書も作成してください


#### pom.xmlファイル作成
---------------------------------------------
この環境でmvnで実行するpom.xmlを作成してください。
mvnのDounloadログは出力しないでください。



-------------------------------------------------------------------------------
# 自動実行結果

### AWSログ
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.inventory.InvSearchTest
一致する在庫品目はありません。
検索キーワードが空です。
検索キーワードが空です。
[ERROR] Tests run: 8, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.236 s <<< FAILURE! - in com.example.inventory.InvSearchTest
[ERROR] testSearchPartialMatch  Time elapsed: 0.144 s  <<< FAILURE!
org.opentest4j.AssertionFailedError: array lengths differ, expected: <4> but was: <1>
    at com.example.inventory.InvSearchTest.testSearchPartialMatch(InvSearchTest.java:12)

[INFO] 
[INFO] Results:
[INFO] 
[ERROR] Failures: 
[ERROR]   InvSearchTest.testSearchPartialMatch:12 array lengths differ, expected: <4> but was: <1>
[INFO] 
[ERROR] Tests run: 8, Failures: 1, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  17.680 s
[INFO] Finished at: 2024-09-05T05:23:20Z
[INFO] ------------------------------------------------------------------------


### AIのエラー解析結果
エラーメッセージから、`testSearchPartialMatch`テストケースが失敗していることがわかります。

```
[ERROR] testSearchPartialMatch  Time elapsed: 0.144 s  <<< FAILURE!
org.opentest4j.AssertionFailedError: array lengths differ, expected: <4> but was: <1>
    at com.example.inventory.InvSearchTest.testSearchPartialMatch(InvSearchTest.java:12)
```

このテストケースでは、キーワード "oo" で検索した場合の期待される結果が `{"Book", "Toy", "Grocery", "Clothing"}` の配列になっていますが、実際の結果は長さ 1 の配列だったため、失敗しています。

原因を特定するには、`InvSearch.search` メソッドの実装を確認する必要があります。おそらく、部分一致の検索ロジックに問題があるか、`addToArray` メソッドの実装に問題があると考えられます。

まず、`InvSearch.search` メソッドの実装を確認してください。部分一致の検索ロジックが正しく実装されているかどうかを確認し、必要に応じて修正してください。

次に、`addToArray` メソッドの実装を確認してください。配列の要素追加が正しく行われているかどうかを確認し、必要に応じて修正してください。

テストケースが失敗した原因を特定し、適切に修正した後、再度テストを実行して、すべてのテストケースが合格することを確認してください。

### AIエラー修正案
コードを確認したところ、`addToArray`メソッドの実装に問題があるようです。

具体的には、以下の行で配列のコピーが正しく行われていません。

```java
System.arraycopy(original, 0, temp, 0, original.length);
```

この行では、`original`配列の要素を`temp`配列にコピーしていますが、コピーする要素数が`original.length`になっています。つまり、`temp`配列の最後の要素には何も代入されていません。

正しくは、以下のようにする必要があります。

```java
System.arraycopy(original, 0, temp, 0, original.length + 1);
```

これで、`temp`配列の最後の要素にも値が代入されるようになります。

修正後の`addToArray`メソッドは以下のようになります。

```java
private static String[] addToArray(String[] original, String element) {
    String[] temp = new String[original.length + 1];
    System.arraycopy(original, 0, temp, 0, original.length + 1);
    temp[temp.length - 1] = element;
    return temp;
}
```

この修正により、`testSearchPartialMatch`テストケースが合格するはずです。
