package com.example.slshopping.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.slshopping.entity.Role;
import com.example.slshopping.entity.User;
import com.example.slshopping.security.SLShopUserDetails;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * ロールリストをmodelに追加する処理
     * 複数のハンドラメソッドでlistRolesをmodelに追加する必要があるため、共通化する
     *
     * @return
     */
    @ModelAttribute(name = "listRoles")
    public List<Role> setRolesToModel() {
        return userService.listRoles();
    }

    /**
     * 管理者一覧画面表示
     *
     * @param model
     * @return 管理者一覧画面
     */
    @GetMapping
    public String listUsers(@RequestParam(required = false) String keyword, Model model) {
        // 全管理者情報の取得
        List<User> listUsers = userService.listAll(keyword);
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("keyword", keyword);
        return "users/users";
    }

    /**
     * 管理者新規登録画面表示
     *
     * @param model
     * @return 管理者新規登録画面
     */
    @GetMapping("/new")
    public String newUserForm(Model model) {
        // 新規登録用に、空の管理者情報作成
        User user = new User();
        model.addAttribute("user", user);
        return "users/user_form";
    }

    /**
     * 管理者登録処理
     *
     * @param user 管理者情報
     * @param result
     * @param model
     * @param ra
     * @return 管理者一覧 or 管理者登録画面
     */
    @PostMapping("/save")
    public String newUser(@Valid @ModelAttribute User user, BindingResult result, Model model, RedirectAttributes ra) {

        // 入力値のチェック
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "users/user_form";
        }

        // 重複チェック
        if (!userService.checkUnique(user)) {
            model.addAttribute("error_message", "重複しています");
            return "users/user_form";
        }

        // 管理者情報の登録
        userService.save(user);
        // 登録成功のメッセージを格納
        ra.addFlashAttribute("success_message", "登録に成功しました");
        return "redirect:/users";
    }

    /**
     * 管理者詳細画面表示
     *
     * @param id 管理者ID
     * @param model
     * @param ra
     * @return 管理者詳細画面
     */
    @GetMapping("/detail/{id}")
    public String detailUser(@PathVariable(name = "id") Long id, Model model, RedirectAttributes ra) {
        try {
            // 管理者IDに紐づく管理者情報取得
            User user = userService.get(id);
            model.addAttribute("user", user);
            return "users/user_detail";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("error_message", "対象のデータが見つかりませんでした");
            return "redirect:/users";
        }
    }

    /**
     * 管理者編集画面表示
     *
     * @param id 管理者ID
     * @param model
     * @param ra
     * @return 管理者編集画面
     */
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable(name = "id") Long id, Model model, RedirectAttributes ra) {
        try {
            // 管理者IDに紐づく管理者情報取得
            User user = userService.get(id);
            model.addAttribute("user", user);
        } catch (NotFoundException e) {
            ra.addFlashAttribute("error_message", "対象のデータが見つかりませんでした");
            return "redirect:/users";
        }
        return "users/user_edit";
    }

    /**
     * 管理者更新処理
     *
     * @param user 管理者情報
     * @param result
     * @param model
     * @param ra
     * @return 管理者一覧 or 管理者更新画面
     */
    @PostMapping("/edit/{id}")
    public String editUser(@Valid @ModelAttribute User user, BindingResult result, Model model, RedirectAttributes ra) {

        // 入力値のチェック
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "users/user_edit";
        }

        // 重複チェック
        if (!userService.checkUnique(user)) {
            model.addAttribute("error_message", "重複しています");
            return "users/user_edit";
        }

        // 管理者情報の更新
        userService.save(user);
        // 更新成功のメッセージを格納
        ra.addFlashAttribute("success_message", "更新に成功しました");
        return "redirect:/users";
    }

    /**
     * 管理者削除処理
     *
     * @param id 管理者ID
     * @param userDetails ログイン者情報
     * @param model
     * @param ra
     * @return 管理者一覧画面
     */
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id, @AuthenticationPrincipal SLShopUserDetails userDetails, Model model, RedirectAttributes ra) {
        // 削除対象が、ログイン中の管理者自身だった場合は削除しない
        if (userDetails.getUser().getId().equals(id)) {
            // 削除失敗のメッセージを格納
            ra.addFlashAttribute("error_message", "削除に失敗しました");
            return "redirect:/users";
        }
        // 管理者情報削除
        try {
            userService.delete(id);
            ra.addFlashAttribute("success_message", "削除に成功しました");
        } catch (NotFoundException e) {
            ra.addFlashAttribute("error_message", "対象のデータが見つかりませんでした");
        }
        return "redirect:/users";
    }

}
