#! /usr/bin/env python3

import cgi
import csv
import sqlite3
import pprint

# FieldStorageクラスのインスタンス化で、フォームの内容を取得
form = cgi.FieldStorage()
title_str = form["query"].value

db_path = "bookdb.db"  # データベースファイル名を指定
con = sqlite3.connect(db_path)  # データベースに接続
cur = con.cursor()  # カーソルを取得

# テーブルの定義
#cur.execute("""create table BOOKLIST
#             (ID int primary key,
#             AUTHOR varchar(256),
#             TITLE varchar(512),
#             PUBLISHER varchar(256),
#             PRICE int,
#             ISBN char(10));""")

# csvファイルの読み込み、insert
#with open('cgi-bin/BookList.csv') as f:
#     reader = csv.reader(f)
#     for row in reader:
#         # tableに各行のデータを挿入する
#         cur.execute('insert into BOOKLIST values (?,?,?,?,?,?);', row)

print("Content-type: text/json; charset=utf-8\n")
book_list = []

try:
	# SQL文の実行
    cur.execute("select * from BOOKLIST where TITLE like ?", ('%'+title_str+'%',))
    rows = cur.fetchall()
    if not rows:
	    print("No books you looked for")
    else:
        for row in rows:
            book_dict = {'ID':str(row[0]), 'AUTHOR':str(row[1]), 'TITLE':str(row[2]), 'PUBLISHER':str(row[3]), 'PRICE':str(row[4]), 'ISBN':str(row[5])}
            book_list.append(book_dict)
    print(book_list)
    
except sqlite3.Error as e:		# エラー処理
	print("Error occurred:", e.args[0])

con.commit()  # データベース更新の確定
con.close()  # データベースを閉じる



