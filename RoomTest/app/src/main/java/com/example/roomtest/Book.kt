package com.example.roomtest

import androidx.room.*

// @Entity를 지정하면서 tableName 속성을 사용하여 생성할 테이블의 이름을 지정할 수 있다.
// 데이터베이스의 생성될 테이블의 이름이므로 너무 길지 않으며 직관적인 이름을 사용하여야 한다.
@Entity(tableName = "books")
//data class 를 사용하여 Book 클래스를 정의하고 있다.
//Book 클래스의 생성자를 보면 (name, writer, price)를 인자로 갖는다.
// @ColumnInfo(name = "book_name")을 사용하여 해당 열(애트리뷰트)의 이름을 지정해줄 수 있다.
// 이는 따로 지정하지 않는다면 변수명이 사용된다. ( writer -> writer , price -> price )
data class Book(@ColumnInfo(name = "book_name") var name: String?, var writer: String?, var price: Int) {

    //@PrimaryKey를 사용하여 기본키를 지정해줄 수 있으며
    //autoGenerate = true를 줌으로써 자동으로 증가하는 값을 갖도록 할 수 있다.
    //id 값은 자동으로 증가하는 값을 갖음으로써 생성자에서 따로 값을 입력을 받을 필요가 없다.
    //따라서 data class Book 내부에 변수를 선언해준다.
    @PrimaryKey(autoGenerate = true) var id: Long = 0

    override fun toString(): String {
        return  "id = $id, name = $name, writer = $writer, price = $price"
    }
}