
新しいUIでは、以下の手順でリポジトリエンドポイントを確認できます。

1. CodeArtifactコンソールを開き、左側のナビゲーションペインで「リポジトリ」を選択します。

2. リポジトリの一覧から、作成したMavenリポジトリを選択します。

3. 「接続手順」タブを選択します。

4. 「Maven」セクションで、「リポジトリエンドポイント」の値をコピーします。これが `CODEARTIFACT_REPOSITORY_URL` に相当します。

5. 同じ「接続手順」タブで、下の方にある「認証トークン」セクションから「認証トークンの作成」をクリックします。

6. 「認証トークンの作成」ダイアログで、トークンの説明を入力し、「認証トークンの作成」をクリックします。

7. 生成された認証トークンをコピーします。これが `CODEARTIFACT_AUTH_TOKEN` に相当します。

このように、新しいUIでは「接続手順」タブからリポジトリエンドポイントと認証トークンを取得する必要があります。

手順自体は同じですが、UIの変更に伴い一部ステップが異なるようですので、ご確認ください。

## 接続手順

### オペレーティングシステム
    Mac & Linux # AWS上のLinuxで利用

### ステップ 1: パッケージマネージャーのクライアントを選択する
    mvn

### ステップ 2: 設定方法を選択する
    リポジトリからプルする

### ステップ 3: リポジトリに対する承認のために、希望するシェルから CodeArtifact 承認トークンをエクスポートします。
トークンは 12 時間で失効します。

    export CODEARTIFACT_AUTH_TOKEN=`aws codeartifact get-authorization-token --domain rayoo-domain --domain-owner 047403811176 --region ap-northeast-1 --query authorizationToken --output text`

### ステップ 4: settings.xml のサーバーリストにサーバーを追加します。
settings.xml は通常 ~/.m2/settings.xml にあります。以下のスニペットセクションを追加すると、Maven は CODEARTIFACT_AUTH_TOKEN 環境変数を HTTP リクエストでトークンとして渡すことができます。
<servers>
    <server>
        <id>rayoo-domain-maven_store</id>
        <username>aws</username>
        <password>${env.CODEARTIFACT_AUTH_TOKEN}</password>
    </server>
</servers>

### ステップ 5: リポジトリを含むプロファイルを settings.xml に追加します。
<id> 要素では任意の値を使用できますが、ステップ 4 の <server> 要素と <repository> 要素の両方で同じである必要があります。これにより、指定された認証情報を CodeArtifact に対するリクエストに含めることができます。
<profiles>
  <profile>
    <id>rayoo-domain-maven_store</id>
    <activation>
      <activeByDefault>true</activeByDefault>
    </activation>
    <repositories>
      <repository>
        <id>rayoo-domain-maven_store</id>
        <url>https://rayoo-domain-047403811176.d.codeartifact.ap-northeast-1.amazonaws.com/maven/maven_store/</url>
      </repository>
    </repositories>
  </profile>
</profiles>

### ステップ 6: (任意) すべての接続をキャプチャしてご使用のリポジトリ (パブリックリポジトリではない) にルーティングするミラーを settings.xml に設定します。
<mirrors>
  <mirror>
    <id>rayoo-domain-maven_store</id>
    <name>rayoo-domain-maven_store</name>
    <url>https://rayoo-domain-047403811176.d.codeartifact.ap-northeast-1.amazonaws.com/maven/maven_store/</url>
    <mirrorOf>*</mirrorOf>
  </mirror>
</mirrors>

