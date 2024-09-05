// パッケージ宣言
package com.example.inventory;

/**
 * 在庫検索クラス
 */
public class InvSearch {

    // 在庫データ
    private static final String[] inventory = {
        "Book", "Game", "Toy", "Grocery", "Clothing"
    };

    /**
     * 在庫検索メソッド
     *
     * @param keyword 検索キーワード
     * @return 検索結果の在庫品目リスト
     */
    public static String[] search(String keyword) {
        // 結果を格納する配列
        String[] results = new String[0];

        // 入力値チェック
        if (keyword == null || keyword.isEmpty()) {
            System.out.println("検索キーワードが空です。");
            return results;
        }

        // 在庫データから部分一致する品目を検索
        for (String item : inventory) {
            if (item.toLowerCase().contains(keyword.toLowerCase())) {
                results = addToArray(results, item);
            }
        }

        // 検索結果がない場合
        if (results.length == 0) {
            System.out.println("一致する在庫品目はありません。");
        }

        return results;
    }

    /**
     * 配列に要素を追加する
     *
     * @param original 元の配列
     * @param element 追加する要素
     * @return 要素が追加された新しい配列
     */
    private static String[] addToArray(String[] original, String element) {
        String[] temp = new String[original.length + 1];
        System.arraycopy(original, 0, temp, 0, original.length);
        temp[temp.length - 1] = element;
        return temp;
    }

    /**
     * メインメソッド
     *
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        String keyword = "oo"; // 検索キーワード
        String[] results = search(keyword);
        for (String item : results) {
            System.out.println(item);
        }
    }
}
