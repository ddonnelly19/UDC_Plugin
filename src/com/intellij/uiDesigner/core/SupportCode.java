package com.intellij.uiDesigner.core;

final class SupportCode {
// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public static TextWithMnemonic parseText(final String textWithMnemonic) {
//        if (textWithMnemonic == null) {
//            throw new IllegalArgumentException("textWithMnemonic cannot be null");
//        }
//        int index = -1;
//        final StringBuffer plainText = new StringBuffer();
//        for (int i = 0; i < textWithMnemonic.length(); ++i) {
//            char ch = textWithMnemonic.charAt(i);
//            if (ch == '&') {
//                if (++i >= textWithMnemonic.length()) {
//                    break;
//                }
//                ch = textWithMnemonic.charAt(i);
//                if (ch != '&') {
//                    index = plainText.length();
//                }
//            }
//            plainText.append(ch);
//        }
//        return new TextWithMnemonic(plainText.toString(), index);
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public static void setDisplayedMnemonicIndex(final JComponent component, final int index) {
//        try {
//            final Method method = component.getClass().getMethod("setDisplayedMnemonicIndex", Integer.TYPE);
//            method.setAccessible(true);
//            method.invoke(component, new Integer(index));
//        }
//        catch (Exception ex) {}
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public static final class TextWithMnemonic
//    {
//        public final String myText;
//        public final int myMnemonicIndex;
//
//// --Commented out by Inspection START (3/1/2017 3:04 PM):
////        private TextWithMnemonic(final String text, final int index) {
////            if (text == null) {
////                throw new IllegalArgumentException("text cannot be null");
////            }
////            if (index != -1 && (index < 0 || index >= text.length())) {
////                throw new IllegalArgumentException("wrong index: " + index + "; text = '" + text + "'");
////            }
////            this.myText = text;
////            this.myMnemonicIndex = index;
////        }
//// --Commented out by Inspection STOP (3/1/2017 3:04 PM)
//
//// --Commented out by Inspection START (3/1/2017 3:04 PM):
////        public char getMnemonicChar() {
////            if (this.myMnemonicIndex == -1) {
////                throw new IllegalStateException("text doesn't contain mnemonic");
////            }
////            return Character.toUpperCase(this.myText.charAt(this.myMnemonicIndex));
////        }
//// --Commented out by Inspection STOP (3/1/2017 3:04 PM)
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)
}