InventoryAllocation プログラム設計書
1. 概要
在庫と注文情報を受け取り、指定された在庫割り当て戦略に従って在庫を割り当てるプログラムです。

2. クラス構造
InventoryAllocation
メインクラス
allocateInventoryメソッドを提供
Inventory
在庫情報を表すクラス
id、itemCode、quantity、unitPriceフィールドを持つ
Order
注文情報を表すクラス
id、itemCode、quantityフィールドを持つ
3. メソッド
3.1 allocateInventory
引数
List<Inventory> inventories: 在庫情報のリスト
List<Order> orders: 注文情報のリスト
String strategy: 在庫割り当て戦略
戻り値: なし
機能
指定された戦略に従って、注文に対して在庫を割り当てる
割り当て戦略は以下の通り
FIFO: 先入れ先出し
LIFO: 後入れ先出し
AVERAGE: 平均単価
SPECIFIC: 単価が低い在庫から割り当て
TOTAL_AVERAGE: 全体の平均単価
MOVING_AVERAGE: 移動平均単価
割り当て処理の詳細は、各戦略に対応するメソッドに委譲される
3.2 allocateFifo
引数
Order order: 注文情報
List<Inventory> inventories: 在庫情報のリスト
戻り値: なし
機能
FIFOの戦略に従って、注文に対して在庫を割り当てる
3.3 allocateLifo
引数
Order order: 注文情報
List<Inventory> inventories: 在庫情報のリスト
戻り値: なし
機能
LIFOの戦略に従って、注文に対して在庫を割り当てる
3.4 allocateAverage
引数
Order order: 注文情報
List<Inventory> inventories: 在庫情報のリスト
戻り値: なし
機能
平均単価の戦略に従って、注文に対して在庫を割り当てる
3.5 allocateSpecific
引数
Order order: 注文情報
List<Inventory> inventories: 在庫情報のリスト
戻り値: なし
機能
単価が低い在庫から割り当てる戦略に従って、注文に対して在庫を割り当てる
3.6 allocateTotalAverage
引数
Order order: 注文情報
List<Inventory> inventories: 在庫情報のリスト
戻り値: なし
機能
全体の平均単価の戦略に従って、注文に対して在庫を割り当てる
3.7 allocateMovingAverage
引数
Order order: 注文情報
List<Inventory> inventories: 在庫情報のリスト
戻り値: なし
機能
移動平均単価の戦略に従って、注文に対して在庫を割り当てる
3.8 calculateMovingAverage
引数
double[] prices: 単価の配列
戻り値: double 移動平均単価
機能
与えられた単価の配列から移動平均単価を計算する
3.9 getInventoriesByItemCode
引数
List<Inventory> inventories: 在庫情報のリスト
String itemCode: 商品コード
戻り値: List<Inventory> 指定された商品コードの在庫情報のリスト
機能
指定された商品コードに一致する在庫情報のリストを返す
4. その他
ログ出力機能を持つ
例外処理を行う
この設計書では、プログラムの概要、クラス構造、各メソッドの機能と引数、戻り値を記載しています。実装の詳細は省略していますが、この設計書を基に実装を進めることができます。