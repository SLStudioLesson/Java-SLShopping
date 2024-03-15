package com.example.slshopping.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.slshopping.entity.Role;
import com.example.slshopping.entity.User;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 管理者情報検索処理
     *
     * @param keyword 検索キーワード
     * @return 管理者情報のリスト
     */
    public List<User> listAll(String keyword) {
        // 検索キーワードがあった場合
        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.search(keyword);
        }
        // それ以外
        else {
            return userRepository.findAll();
        }
    }

    /**
     * ロール情報全件取得処理
     *
     * @return ロール情報のリスト
     */
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    /**
     * IDに紐づく管理者情報取得処理
     *
     * @param id 管理者ID
     * @return 管理者情報
     * @throws NotFoundException
     */
    public User get(Long id) throws NotFoundException {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
    }

    /**
     * 管理者情報登録処理
     *
     * @param user 保存したい管理者情報
     * @return 保存した管理者情報
     */
    @Transactional
    public User save(User user) {
        // 管理者情報を更新する場合
        if (user.getId() != null) {
            // 更新対象の管理者情報を取得
            User existingUser = userRepository.findById(user.getId()).get();
            // 保存したい管理者情報のパスワードが空の場合
            if (user.getPassword().isEmpty()) {
                // 保存したい管理者情報に以前のパスワードを格納
                user.setPassword(existingUser.getPassword());
            } else {
                // パスワードのハッシュ化
                String encodedPassword = encodePassword(user.getPassword());
                // ハッシュ化したパスワードを格納
                user.setPassword(encodedPassword);
            }
        }
        // 管理者情報を新規登録する場合
        else {
            // パスワードのハッシュ化
            String encodedPassword = encodePassword(user.getPassword());
            // ハッシュ化したパスワードを格納
            user.setPassword(encodedPassword);
        }
        return userRepository.save(user);
    }

    /**
     * 管理者情報のメールアドレス重複チェック
     *
     * @param name 重複確認したい管理者情報
     * @return true:重複なし false:重複あり
     */
    public boolean checkUnique(User user) {
        boolean isCreatingNew = (user.getId() == null || user.getId() == 0);
        User userByEmail = userRepository.findByEmail(user.getEmail());

        if (isCreatingNew) {
            if (userByEmail != null) {
                return false;
            }
        } else {
            if (userByEmail != null && userByEmail.getId() != user.getId()) {
                return false;
            }
        }
        return true;
    }

    /**
     * IDに紐づく管理者情報削除処理
     *
     * @param id 管理者ID
     * @throws NotFoundException
     */
    public void delete(Long id) throws NotFoundException {
        // IDに紐づく管理者情報が存在するか確認するため、getメソッドを呼び出す
        User user = get(id);
        userRepository.deleteById(user.getId());
    }

    /**
     * パスワードのハッシュ化
     *
     * @param rawPassword
     */
    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
