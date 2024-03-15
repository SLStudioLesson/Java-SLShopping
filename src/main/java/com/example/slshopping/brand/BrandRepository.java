package com.example.slshopping.brand;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.slshopping.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    /**
     * ブランド情報検索クエリ
     *
     * @param name ブランド名
     * @return ブランド情報
     */
    public Brand findByName(String name);

    /**
     * ブランド情報検索クエリ
     *
     * @param keyword 検索キーワード
     * @return ブランド情報のリスト
     */
    @Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
    public List<Brand> search(String keyword);

}
