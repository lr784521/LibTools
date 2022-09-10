package com.huayun.lib_tools.util.number;


import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表情过滤器
 */
public class EmojiFilter {



    public static InputFilter[] getEmojiFilter(int length) {
        return new InputFilter[]{
                new InputFilter() {
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
                },
                new InputFilter() {
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
                },
                new InputFilter.LengthFilter(length)
        };
    }
}
