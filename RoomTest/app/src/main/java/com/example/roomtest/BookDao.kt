package com.example.roomtest

import androidx.room.*

// DAO란 Data Access Object 로서 데이터에 접근하기 위하여 사용하는 객체를 의미한다.
// Room에는 @Dao라는 어노테이션이 존재하여 간단하게 DAO를 정의할 수 있다.

@Dao
interface BookDao {
    //@Insert, @Delete, @Update, @Query 와 같은 어노테이션도 존재하여
    // SQL 문법의 관한 지식이 있다면 간단하게 DAO 기능들을 구현 가능하다.
    // 삽입
    // OnConflictStrategy : 충돌 처리 방식
    // Insert 할때 PrimaryKey가 겹치는 것이 있으면 덮어 쓴다는 의미이다
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBooks(vararg books: Book)

    @Insert
    fun insertBook(book : Book)

    @Query("INSERT INTO books(book_name, writer, price) VALUES(:name, :writer, :price)")
    fun myInsertBook(name: String, writer: String, price: Int)

    // 삭제
    @Query("DELETE FROM books where id = :id")
    fun deleteBook(id: Long)

    @Query("DELETE FROM books")
    fun deleteAll()

    // 업데이트
    @Query("UPDATE books SET book_name = :name WHERE id = :id")
    fun updateName(id: Long, name: String)

    @Query("UPDATE books SET writer = :writer WHERE id = :id")
    fun updateWriter(id: Long, writer: String)

    @Query("UPDATE books SET price = :price WHERE id = :id")
    fun updatePrice(id: Long, price: Int)

    // 탐색
    @Query("SELECT * FROM books")
    fun getAll(): List<Book>

    @Query("SELECT book_name FROM books")
    fun getNameAll() : List<String>

    @Query("SELECT * FROM books WHERE id = :id")
    fun getBook(id: Long): Book

    @Query("SELECT COUNT(*) FROM books")
    fun getCount(): Int

    @Query("SELECT EXISTS ( SELECT * FROM books where id = :id)")
    fun isBook(id: Long): Int

    //@Insert를 사용하여서 간단하게 테이블에 데이터를 삽입하는 DAO 기능을 만들어 낼 수 있다.
    //마찬가지로 @Delete와 @Update를 사용하여서 삭제와 업데이트를 할 수도 있다.

    //@Query 어노테이션을 사용하여 해당 쿼리문에 따른 동작을 할 수 있다.
    //탐색을 할 때뿐만 아니라 삽입, 삭제, 업데이트 또한 할 수 있으며 SQL 문법이 익숙하다면 Query를 사용하는 것이 더욱 편리하다.

    //함수들을 보면 인자를 갖는 함수들이 존재한다.
    //이 인자 값을 쿼리문에서 이용하기 위해서는 ':'를 붙인 후 인자명을 지정해 주면 된다.
}