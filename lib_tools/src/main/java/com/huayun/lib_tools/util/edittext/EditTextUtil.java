package com.huayun.lib_tools.util.edittext;

import android.content.Context;
import android.os.IBinder;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.huayun.lib_tools.util.number.PointLengthFilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 输入框工具
 */
public class EditTextUtil {

    /**
     * 禁止EditText输入空格和换行符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText) {
        editText.setFilters(new InputFilter[]{getInputFilterSpaceNewline()});
    }

    /**
     * 过滤 空格和换行符 过滤器
     *
     * @return
     */
    public static InputFilter getInputFilterSpaceNewline() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n"))
                    return "";
                else {
                    return null;
                }
            }
        };
    }

    /**
     * 过滤空格
     *
     * @return
     */
    public static InputFilter getInputFilterSpace() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    int type = Character.getType(source.charAt(i));
                    if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                        return "";
                    }
                }
                return null;
            }
        };
    }

    /**
     * 禁止EditText输入空格、换行符和特殊字符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText) {
        editText.setFilters(new InputFilter[]{getSpecialSymbolsFilter()});
    }

    /**
     * 获取特殊符号过滤器
     *
     * @return
     */
    public static InputFilter getSpecialSymbolsFilter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[￥_\'\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) return "";

                if (source.equals(" ") || source.toString().contentEquals("\n"))
                    return "";
                else {
                    return null;
                }
            }
        };
    }

    /**
     * 获取特殊符号过滤器
     *
     * @return
     */
    public static InputFilter getEmojiFilter() {
        return new InputFilter() {
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher emojiMatcher = emoji.matcher(source);
                if (emojiMatcher.find()) {
                    return "";
                }
                return null;
            }
        };
    }

    /**
     * 过滤长度
     * @param length
     * @return
     */
    public static InputFilter getLengthFilter(int length) {
        return new InputFilter.LengthFilter(length);
    }

    /**
     * 过滤表情+特殊符号+设置长度
     *
     * @param length
     * @return
     */
    public static InputFilter[] getEmojiAndSpecialSymbolsFilter(int length) {
        return new InputFilter[]{
                getEmojiFilter(),
                getSpecialSymbolsFilter(),
                getInputFilterSpace(),
                getInputFilterSpaceNewline(),
                getLengthFilter(length)
        };
    }

    public static void hideSoftKeyBoard(IBinder token, Context context) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static boolean isShouldHideSoftKeyBoard(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] l = {0, 0};
            view.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + view.getHeight(), right = left + view.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    /**
     * 设置小数位数
     */
    public static void setPointLength(EditText editText,int length){
        editText.setFilters(new InputFilter[]{new PointLengthFilter(length)});
    }

}
