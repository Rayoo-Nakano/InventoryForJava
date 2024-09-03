# プログラム設計書

## 1. 概要

このプログラムは、在庫の割り当てを行うためのものです。注文情報と在庫情報を取得し、指定された戦略に基づいて在庫を割り当てます。割り当て結果は `AllocationResult` オブジェクトとして保存されます。

## 2. クラス構造

### `InventoryAllocation`

- `allocateInventory(Session session, String strategy)`: 在庫割り当ての実行メソッド
  - 未割り当ての注文を取得
  - 注文ごとに在庫を取得し、指定された戦略に基づいて割り当てを実行
  - 割り当て結果を `AllocationResult` オブジェクトとして保存
- `allocateFifo(Session session, Order order, List<Inventory> inventories)`: FIFO (先入れ先出し) 戦略による割り当てメソッド
- `allocateLifo(Session session, Order order, List<Inventory> inventories)`: LIFO (後入れ先出し) 戦略による割り当てメソッド
- `allocateAverage(Session session, Order order, List<Inventory> inventories)`: 平均価格戦略による割り当てメソッド
- `allocateSpecific(Session session, Order order, List<Inventory> inventories)`: 指定在庫戦略による割り当てメソッド
- `allocateTotalAverage(Session session, Order order, List<Inventory> inventories)`: 総平均価格戦略による割り当てメソッド
- `allocateMovingAverage(Session session, Order order, List<Inventory> inventories)`: 移動平均価格戦略による割り当てメソッド
- `calculateMovingAverage(double[] prices)`: 移動平均価格を計算するメソッド
- `createAllocationResult(Session session, Order order, int allocatedQuantity, double allocatedPrice)`: 割り当て結果を `AllocationResult` オブジェクトとして保存するメソッド

### `Order`

- `id`: 注文ID
- `itemCode`: 商品コード
- `quantity`: 注文数量
- `allocated`: 割り当て済みフラグ

### `Inventory`

- `id`: 在庫ID
- `itemCode`: 商品コード
- `quantity`: 在庫数量
- `unitPrice`: 単価

### `AllocationResult`

- `orderId`: 注文ID
- `allocatedQuantity`: 割り当て数量
- `allocatedPrice`: 割り当て価格

## 3. 処理の流れ

1. `allocateInventory` メソッドが呼び出される
2. 未割り当ての注文を取得する
3. 注文ごとに以下の処理を行う
   1. 注文の商品コードに合致する在庫を取得する
   2. 指定された戦略に応じて、割り当てメソッドを呼び出す
   3. 割り当て結果を `AllocationResult` オブジェクトとして保存する
   4. 注文の割り当て済みフラグを `true` に設定する
4. セッションをフラッシュする

## 4. 割り当て戦略

### FIFO (先入れ先出し)

在庫を古い順に割り当てていきます。

### LIFO (後入れ先出し)

在庫を新しい順に割り当てていきます。

### 平均価格

すべての在庫の平均価格を計算し、その価格で割り当てを行います。

### 指定在庫

注文数量を満たす最初の在庫を割り当てます。

### 総平均価格

すべての在庫の総平均価格を計算し、その価格で割り当てを行います。

### 移動平均価格

直近の一定数の在庫の移動平均価格を計算し、その価格で割り当てを行います。

## 5. 備考

- 割り当て戦略は拡張可能な設計となっており、新しい戦略を追加することができます。
- エラーハンドリングやロギングの実装が不足している可能性があります。
- テストコードの作成が必要です。