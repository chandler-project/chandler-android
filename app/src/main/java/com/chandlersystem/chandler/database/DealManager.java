package com.chandlersystem.chandler.database;

public class DealManager {
    private static final String TAG = DealManager.class.getSimpleName();

    private DealManager() {
    }

    /*public static Observable<List<Deal>> getCoinList() {
        return RXSQLite.rx(
                SQLite.select().from(Coin.class).orderBy(Coin_Table.rank, true))
                .queryList().toObservable()
                .subscribeOn(Schedulers.io());
    }

    public static void addAddCoins(List<Coin> vouchers) {
        FlowManager.getModelAdapter(Coin.class).saveAll(vouchers);
    }*/
}
