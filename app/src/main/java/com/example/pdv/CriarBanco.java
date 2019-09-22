package com.example.pdv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import com.example.pdv.adapter.ListProdutoVendaAdapter;
import com.example.pdv.model.Produto;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class CriarBanco extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "pdv.db";
    private static final int VERSAO = 1;
    private List<Produto> produtos;

    Locale myLocale = new Locale("pt", "BR");
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(myLocale);

    public CriarBanco(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "CREATE TABLE PRODUTO (CODIGO TEXT PRIMARY KEY, "
                + " DESCRICAO TEXT, UNIDADE INTEGER, TIPO INTEGER, PRECO REAL)";
        sqLiteDatabase.execSQL(sql);

        sql = "CREATE TABLE VENDA (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "DATA TEXT, CODIGO_VENDA TEXT, CODIGO TEXT, QTD INTEGER, VALOR REAL)";
        sqLiteDatabase.execSQL(sql);

        sql = "CREATE TABLE COMPRA (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "DATA TEXT, CODIGO_VENDA TEXT, CODIGO TEXT, QTD INTEGER, VALOR REAL)";
        sqLiteDatabase.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS PRODUTO");
        onCreate(sqLiteDatabase);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS VENDA");
        onCreate(sqLiteDatabase);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS COMPRA");
        onCreate(sqLiteDatabase);

    }

    public long insertProduto(Produto produto){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CODIGO", produto.getCODIGO());
        cv.put("DESCRICAO", produto.getDESCRICAO());
        cv.put("UNIDADE", produto.getUNIDADE());
        cv.put("TIPO", produto.getTIPO());
        cv.put("PRECO", produto.getPRECO());
        long id = db.insert("PRODUTO", null, cv);
        db.close();
        return id;
    }

    public void insertVenda(List<ListProdutoVendaAdapter.ItemVenda> itemsVenda){

        String codigo_venda = UUID.randomUUID().toString();
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();

        for (ListProdutoVendaAdapter.ItemVenda itemVenda:
                itemsVenda) {

            ContentValues cv = new ContentValues();

            cv.put("DATA", data);
            cv.put("CODIGO_VENDA", codigo_venda);
            cv.put("CODIGO", itemVenda.getProduto().getCODIGO());
            cv.put("QTD", itemVenda.getQtd());
            cv.put("VALOR", itemVenda.getPreco());

            long id = db.insert("VENDA", null, cv);
        }

        db.close();

    }

    public void insertCompra(List<ListProdutoVendaAdapter.ItemVenda> itemsVenda){

        String codigo_venda = UUID.randomUUID().toString();
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();

        for (ListProdutoVendaAdapter.ItemVenda itemVenda:
                itemsVenda) {

            ContentValues cv = new ContentValues();

            cv.put("DATA", data);
            cv.put("CODIGO_VENDA", codigo_venda);
            cv.put("CODIGO", itemVenda.getProduto().getCODIGO());
            cv.put("QTD", itemVenda.getQtd());
            cv.put("VALOR", itemVenda.getPreco());

            long id = db.insert("COMPRA", null, cv);
        }

        db.close();

    }

    public int updateData(Produto produto){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("DESCRICAO", produto.getDESCRICAO());
        cv.put("UNIDADE", produto.getUNIDADE());
        cv.put("TIPO", produto.getTIPO());
        cv.put("PRECO", produto.getPRECO());
        return db.update("PRODUTO", cv, "CODIGO = ?", new String[]{produto.getCODIGO()});
    }

    public Produto getProduto(String codigo){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("PRODUTO",
                new String[]{"CODIGO", "DESCRICAO", "UNIDADE", "TIPO", "PRECO"},
                "CODIGO = ?",
                new String[]{codigo}, null, null, null, null
                );

        Produto produto = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                produto = new Produto();
                produto.setCODIGO(cursor.getString(cursor.getColumnIndex("CODIGO")));
                produto.setDESCRICAO(cursor.getString(cursor.getColumnIndex("DESCRICAO")));
                produto.setUNIDADE(cursor.getInt(cursor.getColumnIndex("UNIDADE")));
                produto.setTIPO(cursor.getInt(cursor.getColumnIndex("TIPO")));
                produto.setPRECO(cursor.getFloat(cursor.getColumnIndex("PRECO")));
            }
            cursor.close();
        }
        return produto;
    }

    public List<Produto> getAllProdutos(){
        this.produtos = new ArrayList<>();

        String selectQuery = "SELECT * FROM PRODUTO";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do{
                Produto produto = new Produto();
                produto.setCODIGO(cursor.getString(cursor.getColumnIndex("CODIGO")));
                produto.setDESCRICAO(cursor.getString(cursor.getColumnIndex("DESCRICAO")));
                produto.setUNIDADE(cursor.getInt(cursor.getColumnIndex("UNIDADE")));
                produto.setTIPO(cursor.getInt(cursor.getColumnIndex("TIPO")));
                produto.setPRECO(cursor.getFloat(cursor.getColumnIndex("PRECO")));

                produtos.add(produto);
            } while (cursor.moveToNext());
        }

        db.close();

        return produtos;

    }

    public List<Produto> filterProduto(final String filtro, List<Integer> tipo){
        List<Produto> fProdutos = new ArrayList<>();

        for (Produto p:produtos
             ) {
            if (p.getDESCRICAO().toLowerCase().contains(filtro.toLowerCase())
                    || p.getCODIGO().toLowerCase().contains(filtro.toLowerCase())){
                if (tipo.contains(p.getTIPO())){
                    fProdutos.add(p);
                }
            }
        }

        return fProdutos;
    }

    public int getProdutoCount(){
        String countQuery = "SELECT * FROM PRODUTO";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public void deleteProduto(Produto produto){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PRODUTO", "CODIGO = ?", new String[]{produto.getCODIGO()});
        db.close();
    }

    public ArrayList<String> getVendas(String mes){
        ArrayList<String> ret = new ArrayList<>();
        String query;

        if (mes.isEmpty()){
            query = "select strftime('%m/%Y', DATA), sum(VALOR * QTD) MES from VENDA group by strftime('%m/%Y', DATA)";
        }else {
            query = "select strftime('%d/%m/%Y', DATA), sum(VALOR * QTD) MES from VENDA where strftime('%m/%Y', DATA)='" + mes + "' group by strftime('%d/%m/%Y', DATA)";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()){
            do{
                String mesR =cursor.getString(0);
                Float valorVenda = cursor.getFloat(1);
                if (mesR != null)
                    ret.add(mesR + " - " + numberFormat.format(valorVenda));
            } while (cursor.moveToNext());
        }

        return ret;
    }
}
