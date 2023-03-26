package io.swagger.v3.core.util;

import com.fasterxml.jackson.dataformat.yaml.util.StringQuotingChecker;

import java.io.Serializable;

public class QuotingChecker extends StringQuotingChecker implements Serializable {
    private static final long serialVersionUID = 1L;

    protected boolean valueHasQuotableChar(String inputStr) {
        int end = inputStr.length();

        for(int i = 0; i < end; ++i) {
            char d;
            switch(inputStr.charAt(i)) {
                case '#':
                    if (i > 0) {
                        d = inputStr.charAt(i - 1);
                        if (' ' == d || '\t' == d) {
                            return true;
                        }
                    }
                    break;
                case '[':
                case ']':
                case '{':
                case '}':
                    return true;
                case ':':
                    if (i < end - 1) {
                        d = inputStr.charAt(i + 1);
                        if (' ' == d || '\t' == d) {
                            return true;
                        }
                    }
            }
        }

        return false;
    }

    public boolean needToQuoteName(String name) {
        return this.isReservedKeyword(name) || this.looksLikeYAMLNumber(name) || this.nameHasQuotableChar(name);
    }

    public boolean needToQuoteValue(String value) {
        return this.isReservedKeyword(value) || this.valueHasQuotableChar(value);
    }
}
