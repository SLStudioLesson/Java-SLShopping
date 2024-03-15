package com.example.slshopping.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.example.slshopping.entity.Brand;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    /**
     * ブランド情報全件取得
     *
     * @return ブランド情報のリスト
     */
    public List<Brand> listAll() {
        return brandRepository.findAll();
    }

    /**
     * ブランド情報検索処理
     *
     * @param keyword 検索キーワード
     * @return ブランド情報のリスト
     */
    public List<Brand> listAll(String keyword) {
        // 検索キーワードがあった場合
        if (keyword != null && !keyword.isEmpty()) {
            return brandRepository.search(keyword);
        }
        // それ以外の場合
        else {
            return brandRepository.findAll();
        }
    }

    /**
     * IDに紐づくブランド情報取得処理
     *
     * @param id ブランドID
     * @return ブランド情報
     * @throws NotFoundException
     */
    public Brand get(Long id) throws NotFoundException {
        return brandRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
    }

    /**
     * ブランド情報登録処理
     *
     * @param brand 保存したいブランド情報
     * @return 保存したブランド情報
     */
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    /**
     * ブランド名の重複チェック
     *
     * @param name 重複確認したいブランド情報
     * @return true:重複なし false:重複あり
     */
    public boolean checkUnique(Brand brand) {
        boolean isCreatingNew = (brand.getId() == null || brand.getId() == 0);
        Brand brandByName = brandRepository.findByName(brand.getName());

        if (isCreatingNew) {
            if (brandByName != null) {
                return false;
            }
        } else {
            if (brandByName != null && brandByName.getId() != brand.getId()) {
                return false;
            }
        }
        return true;
    }

    /**
     * IDに紐づくブランド情報削除処理
     *
     * @param id ブランドID
     * @throws NotFoundException
     */
    public void delete(Long id) throws NotFoundException {
        // IDに紐づくブランド情報が存在するか確認するため、getメソッドを呼び出す
        Brand brand = get(id);
        brandRepository.deleteById(brand.getId());
    }

}
