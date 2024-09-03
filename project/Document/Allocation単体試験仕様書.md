以下は、単体試験仕様に投入データの定義を含めた内容です。

**単体試験仕様**

1. `allocateInventory`メソッド:
   - 正常な注文と在庫データに対する各割り当て戦略の動作確認
     - 投入データ:
       - 注文: `Order(id=1, itemCode="ITEM001", quantity=5, allocated=false)`
       - 在庫: `[Inventory(id=1, itemCode="ITEM001", quantity=3, unitPrice=5.0), Inventory(id=2, itemCode="ITEM001", quantity=2, unitPrice=5.0)]`
   - 未知の割り当て戦略が指定された場合の例外処理確認
     - 投入データ: 割り当て戦略 = "UNKNOWN"

2. `allocateFifo`メソッド:
   - 在庫が十分な場合の割り当て
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=3)`, 在庫 `[Inventory(id=1, itemCode="ITEM001", quantity=5, unitPrice=10.0)]`
   - 在庫が不足している場合の部分割り当て
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=8)`, 在庫 `[Inventory(id=1, itemCode="ITEM001", quantity=5, unitPrice=10.0)]`
   - 在庫がない場合の動作確認
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=3)`, 在庫 `[]`

3. `allocateLifo`メソッド:
   - 在庫が十分な場合の割り当て
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=3)`, 在庫 `[Inventory(id=1, itemCode="ITEM001", quantity=5, unitPrice=10.0)]`
   - 在庫が不足している場合の部分割り当て
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=8)`, 在庫 `[Inventory(id=1, itemCode="ITEM001", quantity=5, unitPrice=10.0)]`
   - 在庫がない場合の動作確認
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=3)`, 在庫 `[]`

4. `allocateAverage`メソッド:
   - 在庫が十分な場合の割り当て
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=5)`, 在庫 `[Inventory(id=1, itemCode="ITEM001", quantity=3, unitPrice=5.0), Inventory(id=2, itemCode="ITEM001", quantity=2, unitPrice=10.0)]`
   - 在庫が不足している場合の部分割り当て
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=8)`, 在庫 `[Inventory(id=1, itemCode="ITEM001", quantity=3, unitPrice=5.0), Inventory(id=2, itemCode="ITEM001", quantity=2, unitPrice=10.0)]`
   - 在庫がない場合の動作確認
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=3)`, 在庫 `[]`
   - 平均価格の計算確認
     - 投入データ: 在庫 `[Inventory(id=1, itemCode="ITEM001", quantity=3, unitPrice=5.0), Inventory(id=2, itemCode="ITEM001", quantity=2, unitPrice=10.0)]`

5. `allocateSpecific`メソッド:
   - 在庫が十分な場合の割り当て
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=3)`, 在庫 `[Inventory(id=1, itemCode="ITEM001", quantity=5, unitPrice=10.0)]`
   - 在庫が不足している場合の動作確認
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=8)`, 在庫 `[Inventory(id=1, itemCode="ITEM001", quantity=5, unitPrice=10.0)]`

6. `allocateTotalAverage`メソッド:
   - 在庫が十分な場合の割り当て
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=5)`, 在庫 `[Inventory(id=1, itemCode="ITEM001", quantity=3, unitPrice=5.0), Inventory(id=2, itemCode="ITEM001", quantity=2, unitPrice=10.0)]`
   - 在庫が不足している場合の部分割り当て
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=8)`, 在庫 `[Inventory(id=1, itemCode="ITEM001", quantity=3, unitPrice=5.0), Inventory(id=2, itemCode="ITEM001", quantity=2, unitPrice=10.0)]`
   - 在庫がない場合の動作確認
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=3)`, 在庫 `[]`
   - 総平均価格の計算確認
     - 投入データ: 在庫 `[Inventory(id=1, itemCode="ITEM001", quantity=3, unitPrice=5.0), Inventory(id=2, itemCode="ITEM001", quantity=2, unitPrice=10.0)]`

7. `allocateMovingAverage`メソッド:
   - 在庫が十分な場合の割り当て
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=5)`, 在庫 `[Inventory(id=1, itemCode="ITEM001", quantity=3, unitPrice=5.0), Inventory(id=2, itemCode="ITEM001", quantity=2, unitPrice=10.0), Inventory(id=3, itemCode="ITEM001", quantity=2, unitPrice=8.0)]`
   - 在庫が不足している場合の部分割り当て
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=8)`, 在庫 `[Inventory(id=1, itemCode="ITEM001", quantity=3, unitPrice=5.0), Inventory(id=2, itemCode="ITEM001", quantity=2, unitPrice=10.0), Inventory(id=3, itemCode="ITEM001", quantity=2, unitPrice=8.0)]`
   - 在庫がない場合の動作確認
     - 投入データ: 注文 `Order(id=1, itemCode="ITEM001", quantity=3)`, 在庫 `[]`
   - 移動平均価格の計算確認
     - 投入データ: 在庫 `[Inventory(id=1, itemCode="ITEM001", quantity=3, unitPrice=5.0), Inventory(id=2, itemCode="ITEM001", quantity=2, unitPrice=10.0), Inventory(id=3, itemCode="ITEM001", quantity=2, unitPrice=8.0)]`

8. `calculateMovingAverage`メソッド:
   - 移動平均価格の計算確認
     - 投入データ: `[5.0, 10.0, 8.0]`

この仕様書には、各テストケースで使用される投入データが明記されています。これにより、テストの実行時に適切な入力値を使用できるようになります。